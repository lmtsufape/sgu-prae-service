package br.edu.ufape.sguPraeService.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum NaturezaBeneficio {
    BOLSA("Bolsa"),
    AUXILIO("Auxílio"),
    ISENCAO("Isenção"),
    OUTROS("Outros");

    private final String descricao;

    NaturezaBeneficio(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static NaturezaBeneficio fromValue(String value) {
        if (value == null) return null;
        for (NaturezaBeneficio nb : NaturezaBeneficio.values()) {
            if (nb.name().equalsIgnoreCase(value.trim())
                    || nb.descricao.equalsIgnoreCase(value.trim())) {
                return nb;
            }
        }
        throw new IllegalArgumentException("NaturezaBeneficio inválida: " + value);
    }

}
