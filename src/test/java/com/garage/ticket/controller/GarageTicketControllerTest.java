package com.garage.ticket.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.garage.ticket.constants.GarageTicketConstants;
import com.garage.ticket.dto.ParkCarRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class GarageTicketControllerTest extends GarageTicketTestBase {

	final Logger LOG = LoggerFactory.getLogger(GarageTicketControllerTest.class);
	final String carResponseString = "Allocated 1 slot.";
	final String jeepResponseString = "Allocated 2 slots.";
	final String truckResponseString = "Allocated 4 slots.";
	final String garageIsFullString = "Garage is full.";
	final String noValidIntheGarageResponse = "the plate of car is in the garage!";
	final String noValidCarTypeResponse = "cartype must be \"Car-Jeep-Truck!\"";

	@LocalServerPort
	int port;

	@Autowired
	TestRestTemplate restTemplate;

	@Value("${com.garage.ticket.context.path}")
	String serverContextPath;

	String serviceEndPointAddress;
	String parkServiceEndPointAddress;
	String leaveServiceEndPointAddress;
	String statusServiceEndPointAddress;

	private ResponseEntity<String> sendPark(ParkCarRequest parkCarReq) throws JsonProcessingException {

		HttpEntity<String> entity = getParkReqHttpEntity(parkCarReq);

		return restTemplate.postForEntity(parkServiceEndPointAddress, entity, String.class);

	}

	@BeforeAll
	public void init() {
		serviceEndPointAddress = String.format("%s:%d%s", this.getHostBaseAddress(), port, serverContextPath);
		parkServiceEndPointAddress = serviceEndPointAddress + "park";
		leaveServiceEndPointAddress = serviceEndPointAddress + "leave";
		statusServiceEndPointAddress = serviceEndPointAddress + "status";
	}

	@Test
	public void parkTest() throws JsonProcessingException {

		ResponseEntity<String> response = null;

		LOG.info("Park Service Endpoint Adress [{}]", parkServiceEndPointAddress);

		ParkCarRequest parkRequestRepeat = new ParkCarRequest("34-SO-1988", "Black", "car");
		response = sendPark(parkRequestRepeat);

		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(carResponseString, response.getBody());

		response = sendPark(parkRequestRepeat);

		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assert.assertEquals(noValidIntheGarageResponse, response.getBody());

		response = sendPark(new ParkCarRequest("34-BO-1987", "Red", "Truck"));

		Assert.assertEquals(truckResponseString, response.getBody());

		response = sendPark(new ParkCarRequest("34-VO-2018", "Blue", "Jeep"));
		Assert.assertEquals(jeepResponseString, response.getBody());

		response = sendPark(new ParkCarRequest("34-HBO-2020", "Black", "truck"));
		Assert.assertEquals(garageIsFullString, response.getBody());

		response = sendPark(new ParkCarRequest("34-HBP-2022", "Black", "ruck"));
		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		Assert.assertEquals(noValidCarTypeResponse, response.getBody());

	}

	@Test
	public void leaveandStatusTest() throws JsonProcessingException {

		LOG.info("Leave Service Endpoint Adress [{}]", leaveServiceEndPointAddress);
		LOG.info("Status Service Endpoint Adress [{}]", statusServiceEndPointAddress);

		ResponseEntity<String> responseStatus = restTemplate.getForEntity(statusServiceEndPointAddress, String.class);

		Assert.assertEquals(HttpStatus.OK, responseStatus.getStatusCode());

		List<Map<String, Object>> parkCarStatusMapList = convertjson2ParkStatusResponseList(responseStatus.getBody());

		int parkStatsMapListSize = parkCarStatusMapList.size();

		if (parkStatsMapListSize == 0) {

			for (int i = 0; i < 5; i++) {
				ParkCarRequest parkRequestRepeat = new ParkCarRequest(String.format("34-AAA-190%d", i), "Black", "car");
				ResponseEntity<String> responseCarPark = sendPark(parkRequestRepeat);
				Assert.assertEquals(HttpStatus.OK, responseCarPark.getStatusCode());
				Assert.assertEquals(carResponseString, responseCarPark.getBody());
				parkStatsMapListSize++;
			}

		}

		int ticketNum = 1;
		for (int i = 0; i < parkStatsMapListSize; i++) {
			ResponseEntity<String> responseDelete = restTemplate.exchange(
					this.leaveServiceEndPointAddress + "/" + ticketNum, HttpMethod.DELETE,
					new HttpEntity<String>(GarageTicketConstants.EMPTY_STRING), String.class);
			assertEquals(HttpStatus.GONE, responseDelete.getStatusCode());
			ticketNum++;
		}

	}

}
