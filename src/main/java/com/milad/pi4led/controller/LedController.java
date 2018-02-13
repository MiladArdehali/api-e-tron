package com.milad.pi4led.controller;

import com.pi4j.io.gpio.*;
import io.swagger.annotations.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
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
	public String getMessage(@PathVariable("num1") String num1, @PathVariable("num2") String num2) {

		System.out.println(num1.toString());

		return "Numero du Gpio à traiter :" + num1 + num2;
	}

	@RequestMapping(value = "/toggle", method = RequestMethod.GET)
	@ApiOperation("Changement etat Port GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String toggle() {

		getPin().toggle();

		return "OK";

	}

	@RequestMapping(value = "/checkState", method = RequestMethod.GET)
	@ApiOperation("Afficher l'etat du GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	private String checkState() {
		return (getPin().isHigh() ? "Light is on" : "Light is off");
	}

	@RequestMapping(value = "/on", method = RequestMethod.GET)
	@ApiOperation("Allumer le port")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String on() {
		getPin().high();

		return checkState();
	}

	@RequestMapping(value = "/off", method = RequestMethod.GET)
	@ApiOperation("Eteindre le port")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String off() {
		getPin().low();

		return checkState();
	}

	@RequestMapping(value = "/blink/{delay}/{duration}", method = RequestMethod.GET)
	@ApiOperation("Initier un allumage avec intermediaire")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String blink(@PathVariable("delay") long delay, @PathVariable("duration") long duration) {
		getPin().blink(delay, duration);
		return "Light is blinking";
	}

	@RequestMapping(value = "/pulse/{duration}", method = RequestMethod.GET)
	@ApiOperation("Mettre en place des pulsion")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String pulse(@PathVariable("duration") long duration) {
		getPin().pulse(duration);
		return "Light is pulsing";
	}

	

	public GpioPinDigitalOutput getPin() {

		if (pin == null) {
			GpioController gpio = GpioFactory.getInstance();
			pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "MyLED", PinState.LOW);
		}

		return pin;
	}

}