package br.com.jpsl.portalsolicitacaointerna.api.exceptionhandler;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Field{

    private String field;
    private String userMessage;
}
