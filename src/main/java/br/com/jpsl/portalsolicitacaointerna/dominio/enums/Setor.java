package br.com.jpsl.portalsolicitacaointerna.dominio.enums;

import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum Setor {
    ATENDIMENTO("Atendimento"),
    COMERCIAL("Comercial"),
    COMPRAS("Compras"),
    FINANCEIRO("Financeiro"),
    JURIDICO("Jurídico"),
    MARKETING("Marketing"),
    OPERACOES("Operações"),
    RH("Recursos Humanos"),
    TI("Tecnologia da Informação");

    private final String descricao;

    Setor(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Setor converterSetorSTRING(String setor) {
       return  Setor.fromString(setor)
                .orElseThrow(() -> new NegocioException(
                        "Setor inválido. Valores válidos: " + java.util.Arrays.toString(Setor.values())));
    }

    public static Optional<Setor> fromString(String valor) {
        if (valor == null || valor.isBlank()) {
            return Optional.empty();
        }

        String valorNormalizado = normalizar(valor);

        return Arrays.stream(values())
                .filter(setor -> normalizar(setor.name()).equals(valorNormalizado)
                        || normalizar(setor.descricao).equals(valorNormalizado))
                .findFirst();
    }

    private static String normalizar(String valor) {
        return Normalizer.normalize(valor.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[\\s-]+", "_")
                .toUpperCase(Locale.ROOT);
    }
}
