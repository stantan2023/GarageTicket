package com.garage.ticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage.ticket.dto.ParkCarDTOMapper;
import com.garage.ticket.dto.ParkCarRequest;
import com.garage.ticket.dto.ParkCarResponse;
import com.garage.ticket.dto.ParkCarStatusResponse;
import com.garage.ticket.service.TicketService;

@RestController
@RequestMapping("api/v1")
public class TicketController {

	@Autowired
	TicketService ticketSvc;

	@PostMapping("/park")
	public ResponseEntity<String> park(@RequestBody ParkCarRequest carReq) {
		ParkCarResponse parkCarResult = ticketSvc.parkCar(carReq);

		if (!parkCarResult.getValidationMessage().isEmpty()) {
			return new ResponseEntity<String>(parkCarResult.getValidationMessage(), HttpStatus.BAD_REQUEST);
		}

		int numberOfSlots = parkCarResult.getNumberOfSlots();
		String responseMessage = ParkCarDTOMapper.convertParkCarResp(numberOfSlots);
		return ResponseEntity.ok(responseMessage);
	}

	@DeleteMapping("/leave/{ticketnum}")
	public ResponseEntity<Object> leave(@PathVariable(name = "ticketnum") int ticketNum) {
		return (ticketSvc.leaveCar(ticketNum)) ? ResponseEntity.status(HttpStatus.GONE).build()
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/status")
	public ResponseEntity<List<ParkCarStatusResponse>> status() {
		return ResponseEntity.ok(ParkCarDTOMapper.convertParkedCarList(this.ticketSvc.statusCar()));
	}

}
