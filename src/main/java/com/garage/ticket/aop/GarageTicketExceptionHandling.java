package com.garage.ticket.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.garage.ticket.constants.GarageTicketConstants;
import com.garage.ticket.constants.LogType;
import com.garage.ticket.service.LogService;

@ControllerAdvice
public class GarageTicketExceptionHandling {

	@Autowired
	LogService logSvc;

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> generalExceptionHandling(Exception exception) {
		logSvc.log(LogType.ERROR, GarageTicketExceptionHandling.class, exception);
		return new ResponseEntity<>(GarageTicketConstants.EMPTY_STRING, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
