package br.edu.ufape.sguPraeService.dados.conversores;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDate;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, LocalDate> {

    @Override
    public LocalDate convertToDatabaseColumn(YearMonth attribute) {
        if (attribute == null) {
            return null;
        }
        // Converte YearMonth (Java) -> LocalDate dia 1 (Banco)
        return attribute.atDay(1);
    }

    @Override
    public YearMonth convertToEntityAttribute(LocalDate dbData) {
        if (dbData == null) {
            return null;
        }
        // Converte LocalDate (Banco) -> YearMonth (Java)
        return YearMonth.from(dbData);
    }
}