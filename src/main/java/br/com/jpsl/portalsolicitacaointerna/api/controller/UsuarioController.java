package br.com.jpsl.portalsolicitacaointerna.api.controller;


import br.com.jpsl.portalsolicitacaointerna.api.assembler.ApiMapper;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.PageResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request.UsuarioRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request.UsuarioStatusRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request.UsuarioUpdateRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.response.UsuarioResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.component.UsuarioBean;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PerfilUsuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.Setor;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {


    private final UsuarioBean usuarioBean;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, UsuarioBean usuarioBean) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioBean = usuarioBean;
    }

    @GetMapping
    public PageResponse<UsuarioResponse> listarUsuarios(@RequestParam(value = "setor", required = false) String setor,
                                                        Pageable pageable) {

        Usuario usuarioLogado = usuarioBean.getUsuarioLogado();


        if (usuarioLogado.isAdmin() || usuarioLogado.isGestor()) {

            if (StringUtils.hasLength(setor) || usuarioLogado.isGestor()) {

                Setor setorFiltro = usuarioLogado.getSetor();

                if (usuarioLogado.isAdmin()) {
                    setorFiltro = Setor.converterSetorSTRING(setor);
                }

                return PageResponse.from(usuarioService.buscaPorSetor(setorFiltro, pageable)
                        .map(ApiMapper::toResponse));
            }

            return PageResponse.from(usuarioService.listar(pageable)
                    .map(ApiMapper::toResponse));
        } else {
            throw new AccessDeniedException("Você não possui permissão para realizar esta ação.");
        }
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        Usuario usuarioLogado = usuarioBean.getUsuarioLogado();

        return ApiMapper.toResponse(usuarioService.retornaUsuarioPorId(id, usuarioLogado.getSetor(), usuarioLogado.getPerfil(), usuarioLogado.getId()));
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateRequest request) {
        return ApiMapper.toResponse(usuarioService.atualizar(request, id, usuarioBean.getUsuarioLogado()));
    }

    @PostMapping
    @Operation(summary = "Cadastrar usuário")
    @SecurityRequirements
    public ResponseEntity<UsuarioResponse> adicionarUsuario(@RequestBody @Valid UsuarioRequest request) {
        Usuario usuario = new Usuario();

        aplicar(request, usuario);

        Usuario salva = usuarioService.salvar(usuario);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salva.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(ApiMapper.toResponse(salva));
    }

    @PatchMapping("/{id}/status")
    public UsuarioResponse atualizarStatus(@PathVariable Long id, @RequestBody @Valid UsuarioStatusRequest usuarioStatusRequest) {
        return ApiMapper.toResponse(usuarioService.atualizarStatus(id, usuarioStatusRequest.ativo()));
    }


    private void aplicar(UsuarioRequest request, Usuario usuario) {

        if (StringUtils.hasLength(request.nome())) {
            usuario.setNome(request.nome());
        }

        if (StringUtils.hasLength(request.email())) {
            usuario.setEmail(request.email());
        }

        if (StringUtils.hasLength(request.setor())) {
            usuario.setSetor(Setor.converterSetorSTRING(request.setor()));
        }

        if (StringUtils.hasLength(request.senha())) {
            String senhaHash = passwordEncoder.encode(request.senha());
            usuario.setSenha(senhaHash);
        }

        Usuario usuarioLogado = usuarioBean.getUsuarioLogado();

        if (StringUtils.hasLength(request.perfil()) && usuarioLogado != null) {

            PerfilUsuario perfilUsuario = PerfilUsuario.fromString(request.perfil())
                    .orElseThrow(() -> new NegocioException("Perfil de Usuário inválido. Valores válidos: " +
                            "Perfil de Usuário : " + Arrays.toString(PerfilUsuario.values())));

            if (usuarioLogado.isAdmin()) {
                usuario.setPerfil(perfilUsuario);
            } else {
                usuario.setPerfil(PerfilUsuario.USUARIO);
            }

        }
    }


}
