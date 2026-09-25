package br.com.jpsl.portalsolicitacaointerna.dominio.component;

import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioBean {
    public String getLoginUsuarioLogado() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Usuario getUsuarioLogado() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            return (Usuario) authentication.getPrincipal();
        }

        return null;
    }
}
