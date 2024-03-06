package com.platform.pomodoropro.util;

import org.dozer.CustomConverter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMapper implements CustomConverter {

    @Override
    public Object convert(Object destination, Object source, Class destinationClass, Class sourceClass) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        if(source instanceof Long){
            long sourceTime = (long) source;
            return OffsetDateTime.ofInstant(Instant.ofEpochMilli(sourceTime), dateTimeFormatter.getZone());
        } else if (source instanceof  OffsetDateTime) {
            OffsetDateTime offsetDateTime = (OffsetDateTime) source;
            return offsetDateTime.toEpochSecond();
        }
        return null;
    }
}