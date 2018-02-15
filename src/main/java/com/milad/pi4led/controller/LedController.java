package com.milad.pi4led.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pi4j.io.gpio.*;
import io.swagger.annotations.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import util.ListGPIO;

import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/products")
@Api(value = "ProductsControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class LedController {

	private static GpioPinDigitalOutput pin;

	@RequestMapping(value = "/{num1}/{num2}", method = RequestMethod.GET)
	@ApiOperation("Afficher le message")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public JSONObject getMessage(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {

		System.out.println(num1.toString());
		
		JSONObject messageRetour = new JSONObject();
		messageRetour.put("num1", num1);
		messageRetour.put("num2", num2);

		//return "Numero du Gpio à traiter :" + num1 + num2;
		return messageRetour;
	}

	@RequestMapping(value = "/toggle", method = RequestMethod.GET)
	@ApiOperation("Changement etat Port GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String toggle() {

		getPin(26).toggle();

		return "OK";

	}

	@RequestMapping(value = "/checkState", method = RequestMethod.GET)
	@ApiOperation("Afficher l'etat du GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	private String checkState() {
		return (getPin(26).isHigh() ? "Light is on" : "Light is off");
	}

	@RequestMapping(value = "/on", method = RequestMethod.GET)
	@ApiOperation("Allumer le port")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String on() {
		getPin(26).high();

		return checkState();
	}

	@RequestMapping(value = "/off", method = RequestMethod.GET)
	@ApiOperation("Eteindre le port")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String off() {
		getPin(26).low();

		return checkState();
	}

	@RequestMapping(value = "/blink/{delay}/{duration}", method = RequestMethod.GET)
	@ApiOperation("Initier un allumage avec intermediaire")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String blink(@PathVariable("delay") long delay, @PathVariable("duration") long duration) {
		getPin(26).blink(delay, duration);
		return "Light is blinking";
	}

	@RequestMapping(value = "/pulse/{duration}", method = RequestMethod.GET)
	@ApiOperation("Mettre en place des pulsion")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String pulse(@PathVariable("duration") long duration) {
		getPin(26).pulse(duration);
		return "Light is pulsing";
	}
	
	@RequestMapping(value = "/infoGPIO/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Obtenir les information technique d'un GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	public String numPin(@PathVariable("numPin") int numPin) {
		
		JSONObject jsonObject = new JSONObject();
		Pin pin = RaspiPin.getPinByName("GPIO "+numPin);
		
		if(pin != null) {
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = null;
			try {
				jsonInString = mapper.writeValueAsString(pin);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return jsonInString;
		} else {
			return "le pin n'existe pas";
		}
		
	}
	
	@RequestMapping(value = "/test/{num}", method = RequestMethod.GET)
	@ApiOperation("API de test")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	public String testGetPin(@PathVariable("num") int num) {
		
		pin = null;

		ListGPIO listGpio = new ListGPIO();
		listGpio.getPin(num);
		
		GpioController gpio = GpioFactory.getInstance();
		pin = gpio.provisionDigitalOutputPin(listGpio.getPin(num), "MyLED", PinState.LOW);
		
		return "Methode testGetPin OK";
	}

	
	public GpioPinDigitalOutput getPin( int num ) {
		
		ListGPIO listGpio = new ListGPIO();
		listGpio.getPin(num);

		if (pin == null) {
			GpioController gpio = GpioFactory.getInstance();
			pin = gpio.provisionDigitalOutputPin(listGpio.getPin(num), "MyLED", PinState.LOW);
		}

		return pin;
	}

}