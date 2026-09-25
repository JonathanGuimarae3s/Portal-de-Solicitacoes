package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.response.UsuarioResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.component.UsuarioBean;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PerfilUsuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.Setor;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Test
    void deveBuscarUsuarioPorIdComPerfilESetor() {
        UsuarioService usuarioService = mock(UsuarioService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UsuarioBean usuarioBean = mock(UsuarioBean.class);

        UsuarioController controller = new UsuarioController(usuarioService, passwordEncoder, usuarioBean);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Administrador Demo");
        usuario.setEmail("admin@empresa.com");
        usuario.setSetor(Setor.TI);
        usuario.setPerfil(PerfilUsuario.ADMIN);
        usuario.setAtivo(true);

        when(usuarioService.buscarPorId(1L)).thenReturn(usuario);

        UsuarioResponse response = controller.buscarPorId(1L);

        assertAll(
                () -> assertEquals(1L, response.id()),
                () -> assertEquals("Administrador Demo", response.nome()),
                () -> assertEquals("admin@empresa.com", response.email()),
                () -> assertEquals("TI", response.setor()),
                () -> assertEquals("ADMIN", response.perfil()),
                () -> assertTrue(response.ativo())
        );
        verify(usuarioService).buscarPorId(1L);
    }
}
