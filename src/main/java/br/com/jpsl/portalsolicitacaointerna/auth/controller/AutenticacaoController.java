package br.com.jpsl.portalsolicitacaointerna.auth.controller;

import br.com.jpsl.portalsolicitacaointerna.auth.DTO.request.LoginRequest;
import br.com.jpsl.portalsolicitacaointerna.auth.DTO.response.LoginResponse;
import br.com.jpsl.portalsolicitacaointerna.auth.excecao.AutenticacaoException;
import br.com.jpsl.portalsolicitacaointerna.auth.excecao.CredencialException;
import br.com.jpsl.portalsolicitacaointerna.auth.service.TokenService;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {


    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@RequestBody @Valid LoginRequest request) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponse(token));
        } catch (BadCredentialsException e) {
            throw new CredencialException("Credencias inválidas.");
        }
    }


}
