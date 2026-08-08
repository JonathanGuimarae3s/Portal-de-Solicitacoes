package br.com.jpsl.portalsolicitacaointerna.auth.service;

import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public AuthorizationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails usuario = usuarioRepository.findUserDetailsByEmail(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario.js nao encontrado com email: " + username);
        }

        return usuario;
    }
}
