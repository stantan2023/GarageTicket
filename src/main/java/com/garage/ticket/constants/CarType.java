package com.garage.ticket.constants;

public enum CarType {

	Car("Car"), Jeep("Jeep"), Truck("Truck");

	private final String name;

	private CarType(String s) {
		name = s;
	}

	public boolean equalsName(String typeName) {
		return name.equals(typeName);
	}

	public static CarType forNameIgnoreCase(String value) {
		for (CarType cartype : CarType.values()) {
			if (cartype.name().trim().equalsIgnoreCase(value.trim()))
				return cartype;
		}
		return null;
	}

	public String toString() {
		return this.name;
	}

}
