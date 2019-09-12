package org.desarrolladorslp.technovation.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//TODO: if not used remove it
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return (Objects.isNull(localDateTime) ? null : Timestamp.valueOf(localDateTime));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return (Objects.isNull(sqlTimestamp) ? null : sqlTimestamp.toLocalDateTime());
    }
}
