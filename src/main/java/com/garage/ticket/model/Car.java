package com.garage.ticket.model;

import java.util.List;

import com.garage.ticket.constants.CarType;

public class Car {
	
	private String plate;
	private String colour;
	private CarType carType;
	private int ticketNum;
	private List<Integer> slotList;
	
	public Car() {
		
	}

	public Car(String plate, String colour, CarType carType,int ticketNum, List<Integer> slotList) {
		this.plate = plate;
		this.colour = colour;
		this.carType = carType;
		this.ticketNum = ticketNum;
		this.slotList = slotList;		
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public List<Integer> getSlotList() {
		return slotList;
	}

	public void setSlotList(List<Integer> slotList) {
		this.slotList = slotList;
	}

	public int getTickeNum() {
		return ticketNum;
	}

	public void setTickeNum(int ticketNum) {
		this.ticketNum = ticketNum;
	}
	
}
