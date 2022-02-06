package com.garage.ticket.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garage.ticket.dto.ParkCarRequest;

public abstract class GarageTicketTestBase {

	private final String HOST_BASE_ADDRESS = "http://localhost";
	private ObjectMapper mapper = new ObjectMapper();

	public String getHostBaseAddress() {
		return HOST_BASE_ADDRESS;
	}

	public HttpHeaders getHttpHeaders(MediaType contentType, MediaType acceptContentType) {

		HttpHeaders httpHeaderList = new HttpHeaders();

		if(contentType != null) {
			httpHeaderList.setContentType(contentType);
		}
		httpHeaderList.setAccept(Arrays.asList(acceptContentType));

		return httpHeaderList;

	}

	public  HttpEntity<String> getParkReqHttpEntity(ParkCarRequest parkReq) throws JsonProcessingException {

		return  new HttpEntity<String>(convertRequest2json(parkReq),
				getHttpHeaders(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));

	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> convertjson2ParkStatusResponseList(String jsonValue) throws JsonMappingException, JsonProcessingException {
		return mapper.readValue(jsonValue, List.class);
	}

	public String convertRequest2json(ParkCarRequest paringCarReq) throws JsonProcessingException {
		return mapper.writeValueAsString(paringCarReq);
	}
}
