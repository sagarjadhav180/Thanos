package com.convirza.tests.core.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.swing.text.html.parser.Entity;

public class PropertiesReader {
	InputStream input;
	Properties prop = new Properties();
	
	public void loadProperties(String path) {
		try {
			input = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Properties readProperties(String path) {
		loadProperties(path);
		try {
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	public void writeProperties(String path, Map<String, String> properties) {
		loadProperties(path);
		try {
			prop.load(input);
			for (Map.Entry<String, String> property : properties.entrySet()) {
				prop.setProperty(property.getKey(), property.getValue());
			}
			prop.store(new FileOutputStream(path), "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
