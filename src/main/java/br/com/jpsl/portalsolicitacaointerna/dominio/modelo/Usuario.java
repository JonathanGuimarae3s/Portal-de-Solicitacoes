package br.com.jpsl.portalsolicitacaointerna.dominio.modelo;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

@Getter
@Setter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @Email
    private String email;

    @NotBlank
    private String setor;

    @Column(nullable = false)
    @Convert(converter = NumericBooleanConverter.class)
    private boolean ativo = true;

}
