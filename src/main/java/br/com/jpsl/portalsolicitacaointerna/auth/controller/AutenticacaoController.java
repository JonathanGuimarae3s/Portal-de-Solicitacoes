package br.com.jpsl.portalsolicitacaointerna.auth.controller;

import br.com.jpsl.portalsolicitacaointerna.auth.DTO.request.LoginRequest;
import br.com.jpsl.portalsolicitacaointerna.auth.DTO.response.LoginResponse;
import br.com.jpsl.portalsolicitacaointerna.auth.DTO.response.UsuarioAutenticadoResponse;
import br.com.jpsl.portalsolicitacaointerna.auth.excecao.CredencialException;
import br.com.jpsl.portalsolicitacaointerna.auth.service.TokenService;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173/")

public class AutenticacaoController {


    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário")
    @SecurityRequirements
    public ResponseEntity<?> autenticar(@RequestBody @Valid LoginRequest request) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            Usuario usuario = (Usuario) auth.getPrincipal();

            var token = tokenService.gerarToken(usuario);

            var usuarioAutenticado = new UsuarioAutenticadoResponse(
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getPerfil().name(),
                    usuario.getSetor()
            );

            var expiresInMillis = tokenService.getExpiresInMillis();

            return ResponseEntity.ok(new LoginResponse(token, expiresInMillis, usuarioAutenticado));
        } catch (BadCredentialsException e) {
            throw new CredencialException("Credencias inválidas ou inexistente.");
        }
    }


}
