package com.convirza.tests.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class YamlReader {
	
	private String yamlFile;
	
	public String getYamlFile() {
		return yamlFile;
	}
	
	public void setYamlFile(String yamlFile) {
		this.yamlFile = yamlFile;
	}
	
	public Map<String,Object> readYaml(String yamlFile) {
		Yaml yaml = new Yaml();
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(yamlFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> obj = yaml.load(inputStream);
		return obj;
	}
}