package br.com.jpsl.portalsolicitacaointerna.dominio.repositorio;

import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
