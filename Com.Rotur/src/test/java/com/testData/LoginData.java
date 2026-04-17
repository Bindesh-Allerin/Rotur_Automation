package com.testData;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginData {

	private String email;
	private String password;
	private String type;

	// For validation errors
	private Map<String, String> errors;

	// For toast message (error cases)
	private String toast;

	// Getters
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getType() {
		return type;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public String getToast() {
		return toast;
	}

	@Override
	public String toString() {
		return """
				{
				    [%s],
				    "email": "%s",
				    "password": "%s"
				}
				""".formatted(type, email, password);

	}

}
