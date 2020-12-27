package com.convirza.tests.core.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YamlWriter {
	private String yamlFile;
	
	public String getYamlFile() {
		return yamlFile;
	}
	
	public void setYamlFile(String yamlFile) {
		this.yamlFile = yamlFile;
	}
	
	public void write(Map<String, Object> data, String filePath) {
	    Yaml yaml = new Yaml();
	    String updatedYaml = yaml.dump(data);
	    try {
				FileWriter writer = new FileWriter(new File(filePath));
				writer.write(updatedYaml);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
