package com.garage.ticket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.garage.ticket.constants.LogType;

@Service
public class LogService {

	public void log(LogType type, Class clazz, Exception e) {
		log(type, clazz, null, null, e);
	}

	public void log(LogType type, Class clazz, String method, String message) {
		log(type, clazz, method, message, null);
	}

	public void log(LogType type, Class clazz, String method, String message, Exception e) {
		Logger log = LoggerFactory.getLogger(clazz);

		if (type == LogType.INFO) {
			log.info("method[{}] - [{}]", method, message);
		} else if (type == LogType.ERROR) {

			if (method != null && message != null) {
				log.error(String.format("method[%s] - [%s]", method, message));
			} else {
				log.error("exception:", e);
			}
		}
	}

}
