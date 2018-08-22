package com.redhat.syseng.soleng.rhpam.processmigration.serializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateSerializer extends StdSerializer<Date> {

    private static final long serialVersionUID = -4733510908672603197L;

    public DateSerializer() {
	super(Date.class);
    }

    @Override
    public void serialize(Date date, JsonGenerator generator, SerializerProvider provider)
	    throws IOException, JsonProcessingException {
	if (date != null) {
	    LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC.normalized());
	    generator.writeString(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
	}
    }

}
