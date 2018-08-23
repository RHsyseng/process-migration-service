package com.redhat.syseng.soleng.rhpam.processmigration.serializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateDeserializer extends StdDeserializer<Date> {

    private static final long serialVersionUID = -313263515019496294L;

    public DateDeserializer() {
	super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	if (p == null || p.getValueAsString() == null) {
	    return null;
	}
	LocalDateTime localDateTime = LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ISO_DATE_TIME);
	return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }

}
