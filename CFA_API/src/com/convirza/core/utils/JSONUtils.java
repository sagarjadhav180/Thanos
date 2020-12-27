package com.convirza.core.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {
	
	public static JsonNode convertToJsonObject(String jsonStr) {
		ObjectMapper mapper = new ObjectMapper(); 
		JsonNode jsonObj = null;
		try {
			jsonObj = mapper.readTree(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj;
	}
}
