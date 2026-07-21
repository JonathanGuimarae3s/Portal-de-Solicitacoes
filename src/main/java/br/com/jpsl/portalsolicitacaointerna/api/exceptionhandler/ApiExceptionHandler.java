package br.com.jpsl.portalsolicitacaointerna.api.exceptionhandler;

import br.com.jpsl.portalsolicitacaointerna.auth.excecao.AutenticacaoException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeEmUsoException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{

    public static final String MENSAGEM_ERRO_GENERICO_USUARIO = "Ocorreu um erro interno inesperado no sistema. " +
            "Tente novamente e se o problema persistir, entre " +
            "em contato com o administrador do sistema.";

    //INICIO DAS EXCECOES CUSTOMIZADAS
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleTratarEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex,
                                                                        WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

        String message = ex.getMessage();
        String userMessage = "Não existe um cadastro com o código informado. Verifique e tente novamente.";

        Problem problem = createProblemBuilder(status, problemType, message)
                .userMessage(userMessage)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleTratarEntidadeEmUso(EntidadeEmUsoException ex, WebRequest request) {

        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;

        String message = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, message)
                .userMessage(message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.NEGOCIO;
        String message = ex.getMessage();


        Problem problem = createProblemBuilder(status, problemType, message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(AutenticacaoException.class)
    public ResponseEntity<?> handleAutenticacaoException(AutenticacaoException ex, WebRequest request) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String message = ex.getMessage();


        Problem problem = createProblemBuilder(status, problemType, message)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;

        String detail = MENSAGEM_ERRO_GENERICO_USUARIO;

        Problem problem = createProblemBuilder(status, problemType, detail).
                userMessage(MENSAGEM_ERRO_GENERICO_USUARIO)
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);

    }


    //INICIO DAS EXCECOES SOBREESCRITAS DO SPRING

    @Autowired
    private MessageSource messageSouce;

    @Override
    protected @Nullable ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                                  HttpStatusCode statusCode, WebRequest request) {
        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

        String value = ex.getValue().toString();
        String propertyName = request.getContextPath() + request.getDescription(false)
                .replace("uri=", "");


        final String simpleName = ex.getRequiredType().getSimpleName();

        String message = String.format("O parametro de url '%s' recebeu o valor '%s' que é de um tipo inválido." +
                        " Corrija e informe um valor compatível com o tipo %s.",
                propertyName, value, simpleName);

        HttpStatus status = HttpStatus.resolve(statusCode.value());

        Problem problem = createProblemBuilder(status, problemType, message)
                .userMessage(MENSAGEM_ERRO_GENERICO_USUARIO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                            HttpHeaders headers, HttpStatusCode statusCode,
                                                                            WebRequest request) {
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;

        String messageApi = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        List<Field> fieldList = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> {
                            String message = messageSouce.getMessage(fieldError, LocaleContextHolder.getLocale());

                            return Field.builder()
                                    .field(fieldError.getField())
                                    .userMessage(message)
                                    .build();
                        }
                )
                .collect(Collectors.toList());

        HttpStatus status = HttpStatus.resolve(statusCode.value());

        Problem problem = createProblemBuilder(status, problemType, messageApi)
                .userMessage(messageApi)
                .fields(fieldList)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                             HttpHeaders headers, HttpStatusCode statusCode,
                                                                             WebRequest request) {
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;

        String propertyName = request.getContextPath() + request.getDescription(false)
                .replace("uri=", "");

        String message = String.format("O recurso '%s, que voce tentou acessar, é inexistente.",
                propertyName);

        HttpStatus status = HttpStatus.resolve(statusCode.value());

        Problem problem = createProblemBuilder(status, problemType, message)
                .userMessage(MENSAGEM_ERRO_GENERICO_USUARIO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode statusCode,
                                                                            WebRequest request) {
        HttpStatus status = HttpStatus.resolve(statusCode.value());

        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handleIgnoredPropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.CORPO_MAL_FORMATADO;

        String message = "O corpo da requisição está inválido. Verifique erro de sintaxe.";


        Problem problem = createProblemBuilder(status, problemType, message)
                .userMessage(MENSAGEM_ERRO_GENERICO_USUARIO)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {

        final String simpleName = ex.getTargetType().getSimpleName();
        final Object value = ex.getValue();

        ProblemType problemType = ProblemType.CORPO_MAL_FORMATADO;

        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));

        String message = String.format("A propiedade '%s' recebeu o valor '%s', que é de um tipo inválido. " +
                "Corrija e informe um valor compatível com o tipo %s.", path, value, simpleName);


        Problem problem = createProblemBuilder(status, problemType, message)
                .userMessage(MENSAGEM_ERRO_GENERICO_USUARIO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleIgnoredPropertyBinding(PropertyBindingException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {

        ProblemType problemType = ProblemType.CORPO_MAL_FORMATADO;

        String path = ex.getPath().stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));

        String message = String.format("A propiedade '%s' não existe no corpo da requisição. Corrija ou remova essa" +
                        " propriedade e tente novamente.",
                path);

        Problem problem = createProblemBuilder(status, problemType, message)
                .userMessage(MENSAGEM_ERRO_GENERICO_USUARIO)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected @org.jspecify.annotations.Nullable ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @org.jspecify.annotations.Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode,
            WebRequest request) {

        HttpStatus status = HttpStatus.resolve(statusCode.value());

        if (body == null) {
            body = Problem.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .timestamp(LocalDateTime.now())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .timestamp(LocalDateTime.now())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .timestamp(LocalDateTime.now())
                .detail(detail);
    }

}
