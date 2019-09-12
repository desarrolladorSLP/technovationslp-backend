package org.desarrolladorslp.technovation.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//TODO: if not used remove it
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return (Objects.isNull(localDate) ? null : Date.valueOf(localDate));
    }

    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
        return Objects.isNull(sqlDate) ? null : sqlDate.toLocalDate();
    }
}
