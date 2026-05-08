package br.com.jpsl.portalsolicitacaointerna.dominio.modelo;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Solicitacao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String titulo;

    @NotNull
    private String descricao;

    @Enumerated(EnumType.STRING)
    private PrioridadeSolicitacao prioridade = PrioridadeSolicitacao.BAIXA;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao status = StatusSolicitacao.ABERTA;


    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "tipo_id", nullable = false)
    private TipoSolicitacao tipo;

}
