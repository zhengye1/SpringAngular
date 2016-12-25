package com.vincent.springrest.convert;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CustomDateDeserializer extends StdDeserializer<LocalDate> {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = -3889435051679382828L;
	private DateTimeFormatter  formatter = 
      DateTimeFormat.forPattern("yyyy-MM-dd");
 
    public CustomDateDeserializer() {
        this(null);
    }
 
    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }
 
    @Override
    public LocalDate deserialize(JsonParser jsonparser, DeserializationContext context)
      throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        return formatter.parseLocalDate(date);
    }
}