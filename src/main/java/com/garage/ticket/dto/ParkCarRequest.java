package com.garage.ticket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParkCarRequest {

	private String plate;
	private String colour;
	private String carType;
	
	public ParkCarRequest() {
		
	}
	
	public ParkCarRequest(String plate, String colour, String carType) {
		this.plate = plate;
		this.colour = colour;
		this.carType = carType;
	}

	@JsonProperty("plate")
	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	@JsonProperty("colour")
	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	@JsonProperty("cartype")
	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}
	
}
