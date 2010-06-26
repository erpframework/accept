package org.accept.domain;

public class Story {
	
	String context;
	String description;
	String acceptance;

	public Story(String context, String description, String acceptance) {
		this.context = context;
		this.description = description;
		this.acceptance = acceptance;
	}

	public String getContext() {
		return context;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getAcceptance() {
		return acceptance;
	}
}
