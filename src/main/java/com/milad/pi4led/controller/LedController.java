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

//	@RequestMapping(value = "/{num1}/{num2}", method = RequestMethod.GET)
//	@ApiOperation("Afficher le message")
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
//	public JSONObject getMessage(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {
//
//		System.out.println(num1.toString());
//		
//		JSONObject messageRetour = new JSONObject();
//		messageRetour.put("num1", num1);
//		messageRetour.put("num2", num2);
//
//		//return "Numero du Gpio à traiter :" + num1 + num2;
//		return messageRetour;
//	}

	@RequestMapping(value = "/toggle/{num}", method = RequestMethod.GET)
	@ApiOperation("Changement etat Port GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String toggle(@PathVariable("num") int num) {

		getPin(num).toggle();

		return "OK";

	}

	@RequestMapping(value = "/checkState/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Afficher l'etat du GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	private String checkState(@PathVariable("numPin") int numPin) {
		return (getPin(numPin).isHigh() ? "Light is on" : "Light is off");
	}

	@RequestMapping(value = "/on/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Allumer le port")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String on(@PathVariable("numPin") int numPin) {
		getPin(numPin).high();

		return checkState(numPin);
	}

	@RequestMapping(value = "/off/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Eteindre le port")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String off(@PathVariable("numPin") int numPin) {
		getPin(numPin).low();

		return checkState(numPin);
	}

	@RequestMapping(value = "/blink/{numPin}/{delay}/{duration}", method = RequestMethod.GET)
	@ApiOperation("Initier un allumage avec intermediaire")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String blink(@PathVariable("numPin") int numPin, @PathVariable("delay") long delay, @PathVariable("duration") long duration) {
		getPin(numPin).blink(delay, duration);
		return "Light is blinking";
	}

	@RequestMapping(value = "/pulse/{numPin}/{duration}", method = RequestMethod.GET)
	@ApiOperation("Mettre en place des pulsion")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String pulse(@PathVariable("numPin") int numPin, @PathVariable("duration") long duration) {
		getPin(numPin).pulse(duration);
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
	
//	@RequestMapping(value = "/test/{numPin}", method = RequestMethod.GET)
//	@ApiOperation("API de test")
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
//	public String testGetPin(@PathVariable("numPin") int numPin) {
//		
//		gpio.shutdown();
//		
//		ListGPIO listGpio = new ListGPIO();
//		listGpio.getPin(numPin);
//		
//		//GpioController gpio = GpioFactory.getInstance();
//		pin = gpio.provisionDigitalOutputPin(listGpio.getPin(numPin), "MyLED", PinState.LOW);
//		
//		return "Methode testGetPin OK";
//	}

	
	public GpioPinDigitalOutput getPin( int numPin ) {
		
		ListGPIO listGpio = new ListGPIO();
		listGpio.getPin(numPin);

		//if (pin == null) {
			GpioController gpio = GpioFactory.getInstance();
			pin = gpio.provisionDigitalOutputPin(listGpio.getPin(numPin), "MyLED", PinState.LOW);
		//}

		return pin;
	}

}