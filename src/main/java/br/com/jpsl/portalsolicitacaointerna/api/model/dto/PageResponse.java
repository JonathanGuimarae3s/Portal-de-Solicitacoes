package br.com.jpsl.portalsolicitacaointerna.api.model.dto;

import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collector;

public record PageResponse<T>(List<T> dados, int pagina, int tamanho, long totalDados, int totalPaginas) {
    public static <T> PageResponse<T> from(org.springframework.data.domain.Page<T> result) {
        return new PageResponse<>(result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }




}
