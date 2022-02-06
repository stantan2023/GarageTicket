package com.garage.ticket.aop;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garage.ticket.constants.GarageTicketConstants;
import com.garage.ticket.constants.LogType;
import com.garage.ticket.service.LogService;

import jdk.nashorn.internal.runtime.options.LoggingOption.LoggerInfo;

@Aspect
@Component
public class GarageTicketAop {

	@Autowired
	LogService logSvc;

	@Around("execution(* com.garage.ticket.controller.TicketController.*(..))")
	public Object controllerOperation(ProceedingJoinPoint joint) throws Throwable {

		long executedTime = (new Date()).getTime();
		Class clazz = com.garage.ticket.controller.TicketController.class;

		ObjectMapper mapper = new ObjectMapper();

		Object[] args = joint.getArgs();
		String methodName = joint.getSignature().getName();
		String jsonInputValue = GarageTicketConstants.EMPTY_STRING,
				jsonOutputValue = GarageTicketConstants.EMPTY_STRING;

		if (args.length > 0) {
			jsonInputValue = mapper.writeValueAsString(args[0]);
		}

		Object resp = joint.proceed(args);

		if (resp instanceof ResponseEntity) {

			ResponseEntity respEnitity = (ResponseEntity) resp;
			Object responseBody = respEnitity.getBody();
			if (responseBody != null) {
				jsonOutputValue = mapper.writeValueAsString(responseBody);
			}

			HttpStatus respStatus = respEnitity.getStatusCode();

			executedTime = (new Date()).getTime() - executedTime;
			logSvc.log(
					((respStatus == HttpStatus.OK || respStatus == HttpStatus.CREATED || respStatus == HttpStatus.GONE)
							? LogType.INFO
							: LogType.ERROR),
					clazz, methodName, String.format("httpstatus:[%s] - request:[%s] - response:[%s] - executed[%d ms]",respStatus.name(), jsonInputValue, jsonOutputValue,executedTime));

		}

		return resp;
	}

}
