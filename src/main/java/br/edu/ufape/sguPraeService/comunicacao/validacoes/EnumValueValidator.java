package br.edu.ufape.sguPraeService.comunicacao.validacoes;

import br.edu.ufape.sguPraeService.comunicacao.annotations.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class EnumValueValidator
        implements ConstraintValidator<EnumValue, String> {

    private Set<String> accepted;
    private boolean ignoreCase;

    @Override
    public void initialize(EnumValue annotation) {
        this.ignoreCase = annotation.ignoreCase();
        this.accepted = new HashSet<>();

        Class<? extends Enum<?>> enumClass = annotation.enumClass();
        Method descricaoMethod = null;
        try {
            descricaoMethod = enumClass.getMethod("getDescricao");
        } catch (NoSuchMethodException ignored) { }

        for (Enum<?> enumConst : enumClass.getEnumConstants()) {
            String name = enumConst.name();
            accepted.add(normalize(name));

            if (descricaoMethod != null) {
                try {
                    String descricao = (String) descricaoMethod.invoke(enumConst);
                    accepted.add(normalize(descricao));
                } catch (IllegalAccessException | InvocationTargetException ignored) { }
            }
        }
    }

    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String v = ignoreCase
                ? value.toLowerCase()
                : value;
        return accepted.contains(v);
    }

    private String normalize(String s) {
        return ignoreCase
                ? s.toLowerCase(Locale.ROOT)
                : s;
    }
}