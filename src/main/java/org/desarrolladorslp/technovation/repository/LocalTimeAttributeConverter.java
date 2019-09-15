package org.desarrolladorslp.technovation.repository;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//TODO: if not used remove it
@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time> {

    @Override
    public Time convertToDatabaseColumn(LocalTime localTime) {
        return (Objects.isNull(localTime) ? null : Time.valueOf(localTime));
    }

    @Override
    public LocalTime convertToEntityAttribute(Time sqlTime) {
        return Objects.isNull(sqlTime) ? null : sqlTime.toLocalTime();
    }
}
