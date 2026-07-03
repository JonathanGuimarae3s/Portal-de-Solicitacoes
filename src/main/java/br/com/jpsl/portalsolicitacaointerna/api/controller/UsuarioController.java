package br.com.jpsl.portalsolicitacaointerna.api.controller;


import br.com.jpsl.portalsolicitacaointerna.api.assembler.ApiMapper;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.PageResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request.UsuarioRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.response.UsuarioResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {


    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public PageResponse<UsuarioResponse> listarUsuarios(@RequestParam(value = "setor", required = false) String setor,
                                                        Pageable pageable) {
        if (StringUtils.hasLength(setor)) {
            return PageResponse.from(usuarioService.buscaPorSetor(setor, pageable)
                    .map(ApiMapper::toResponse));
        }

        return PageResponse.from(usuarioService.listar(pageable)
                .map(ApiMapper::toResponse));
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioRequest request) {
        return ApiMapper.toResponse(usuarioService.atualizar(request,id));
    }

    @PostMapping
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

    private void aplicar(UsuarioRequest request, Usuario usuario) {

        if (StringUtils.hasLength(request.nome())) {
            usuario.setNome(request.nome());
        }

        if (StringUtils.hasLength(request.email())) {
            usuario.setEmail(request.email());
        }

        if (StringUtils.hasLength(request.setor())) {
            usuario.setSetor(request.setor());
        }
    }


}
