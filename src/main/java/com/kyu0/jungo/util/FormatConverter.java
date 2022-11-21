package com.kyu0.jungo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class FormatConverter {
    
    public static String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;

        try {
            json = mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return json;
    }
}
