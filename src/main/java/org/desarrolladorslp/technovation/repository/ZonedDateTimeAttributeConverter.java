package org.desarrolladorslp.technovation.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime localDateTime) {
        return (Objects.isNull(localDateTime) ? null : Timestamp.valueOf(localDateTime.toLocalDateTime()));
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        LocalDateTime localDateTimeNoTimeZone = sqlTimestamp.toLocalDateTime();

        return localDateTimeNoTimeZone.atZone(ZoneOffset.UTC);
    }
}
