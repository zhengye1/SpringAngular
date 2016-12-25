package com.vincent.springrest.convert;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomDateSerializer extends StdSerializer<LocalDate>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1501806392845528198L;
	private DateTimeFormatter  formatter = 
			DateTimeFormat.forPattern("yyyy-MM-dd");

	public CustomDateSerializer() {
		this(null);
	}

	public CustomDateSerializer(Class<LocalDate> vc) {
		super(vc);
	}
	
    @Override
    public void serialize(LocalDate value, JsonGenerator gen, 
                          SerializerProvider arg2)
        throws IOException, JsonProcessingException {

        gen.writeString(formatter.print(value));
    }
}
