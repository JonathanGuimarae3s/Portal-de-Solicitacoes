package br.com.jpsl.portalsolicitacaointerna.dominio.repositorio;

import br.com.jpsl.portalsolicitacaointerna.api.model.dto.PageResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    @Override
    Page<Usuario> findAll(Pageable pageable);

    Page<Usuario> findAllBySetor(String cargo, Pageable pageable);
}
