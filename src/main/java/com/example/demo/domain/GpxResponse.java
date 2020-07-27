package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GpxResponse {
	@JsonProperty("userId")
	private String userId;
	@JsonProperty("id")
	private Long id;
	@JsonProperty("fileName")
	private String fileName;
	@JsonProperty("name")
	private String name;
	@JsonProperty("message")
	private String message;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
