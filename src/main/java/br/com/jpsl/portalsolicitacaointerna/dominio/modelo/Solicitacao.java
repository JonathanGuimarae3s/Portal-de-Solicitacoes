package br.com.jpsl.portalsolicitacaointerna.dominio.modelo;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class Solicitacao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;


    @Enumerated(EnumType.STRING)
    @NotNull
    private PrioridadeSolicitacao prioridade = PrioridadeSolicitacao.BAIXA;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusSolicitacao status = StatusSolicitacao.ABERTA;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoSolicitacao tipo;

    @NotNull
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

}
