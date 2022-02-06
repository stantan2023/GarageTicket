package com.garage.ticket.dto;

public class ParkCarResponse {
	
	String validationMessage;
	int numberOfSlots;
	
	public ParkCarResponse() {
		
	}

	public ParkCarResponse(String validationMessage, int numberOfSlots) {
		this.validationMessage = validationMessage;
		this.numberOfSlots = numberOfSlots;
	}

	

	public int getNumberOfSlots() {
		return numberOfSlots;
	}

	public void setNumberOfSlots(int numberOfSlots) {
		this.numberOfSlots = numberOfSlots;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	
}
