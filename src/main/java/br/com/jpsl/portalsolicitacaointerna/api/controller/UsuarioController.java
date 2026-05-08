package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.UsuarioRepository;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.buscarPorId(id);

        BeanUtils.copyProperties(usuario, usuarioSalvo, "id");

        return usuarioRepository.save(usuarioSalvo);
    }

    @PostMapping
    public Usuario adicionarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.adicionarUsuario(usuario);
    }


}
