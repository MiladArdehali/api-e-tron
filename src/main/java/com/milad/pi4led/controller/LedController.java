package com.milad.pi4led.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milad.pi4led.serviceMetier.CommandeService;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import io.swagger.annotations.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import util.ListGPIO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Api(value = "ProductsControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class LedController {

	private static GpioPinDigitalOutput pin;
	public static HashMap<Integer, GpioPinDigitalOutput> listPin = new HashMap<Integer, GpioPinDigitalOutput>();
	public static HashMap<Integer, String> infoPinActive = new HashMap<Integer, String>();
	private static GpioController gpio = GpioFactory.getInstance();
	final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

	@RequestMapping(value = "listerPinActive", method = RequestMethod.GET)
	@ApiOperation("Afficher le message")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public JSONObject listerPinActive() {

		JSONObject listeRetour = new JSONObject(infoPinActive);

		return listeRetour;
	}

	@RequestMapping(value = "/toggle/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Changement etat Port GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String toggle(@PathVariable("num") int numPin) {

		if (listPin.get(numPin) != null) {
			listPin.get(numPin).toggle();
			return "OK";
		} else {
			return "le port GPIO n° " + numPin + " n'est pas activé pour n'existe pas";
		}

	}

	@RequestMapping(value = "/checkState/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Afficher l'etat du GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	private String checkState(@PathVariable("numPin") int numPin) {

		if (listPin.get(numPin) != null) {
			return ((listPin.get(numPin)).isHigh() ? "Light is on" : "Light is off");
		} else {
			return "le port GPIO n° " + numPin + " n'est pas activé pour n'existe pas";
		}

	}

	@RequestMapping(value = "/on/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Allumer le port")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String on(@PathVariable("numPin") int numPin) {

		if (listPin.get(numPin) != null) {
			listPin.get(numPin).high();
			return checkState(numPin);
		} else {
			return "le port GPIO n° " + numPin + " n'est pas activé pour n'existe pas";
		}

	}

	@RequestMapping(value = "/off/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Eteindre le port")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String off(@PathVariable("numPin") int numPin) {

		if (listPin.get(numPin) != null) {
			listPin.get(numPin).low();
			return checkState(numPin);
		} else {
			return "le port GPIO n° " + numPin + " n'est pas activé pour n'existe pas";
		}
	}

	@RequestMapping(value = "/blink/{numPin}/{delay}/{duration}", method = RequestMethod.GET)
	@ApiOperation("Initier un allumage avec intermediaire")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String blink(@PathVariable("numPin") int numPin, @PathVariable("delay") long delay,
			@PathVariable("duration") long duration) {

		if (listPin.get(numPin) != null) {
			listPin.get(numPin).blink(delay, duration);
			return "Light is blinking";
		} else {
			return "le port GPIO n° " + numPin + " n'est pas activé pour n'existe pas";
		}

	}

	@RequestMapping(value = "/pulse/{numPin}/{duration}", method = RequestMethod.GET)
	@ApiOperation("Mettre en place des pulsion")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String pulse(@PathVariable("numPin") int numPin, @PathVariable("duration") long duration) {

		if (listPin.get(numPin) != null) {
			listPin.get(numPin).pulse(duration);
			return "Light is pulsing";
		} else {
			return "le port GPIO n° " + numPin + " n'est pas activé pour n'existe pas";
		}

	}

	@RequestMapping(value = "/stopALL", method = RequestMethod.GET)
	@ApiOperation("Arret de tous les ports GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public JSONObject stopALL() {

		HashMap<String, String> listeStop = new HashMap<String, String>();
		if (listPin != null) {
			for (Entry<Integer, GpioPinDigitalOutput> entry : listPin.entrySet()) {
				entry.getValue().low();
				listeStop.put("Port stoppé num: ", entry.getKey().toString());
			}
			JSONObject rapportExtinction = new JSONObject(listeStop);
			return rapportExtinction;
		} else {
			listeStop.put("Erreur: ", "Aucun port actif");
			JSONObject rapportExtinction = new JSONObject(listeStop);
			return rapportExtinction;
		}

	}

	@RequestMapping(value = "/infoTechniqueGPIO/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Obtenir les information technique d'un GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	public String numPin(@PathVariable("numPin") int numPin) {

		Pin pin = RaspiPin.getPinByName("GPIO " + numPin);

		if (pin != null) {
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

	@RequestMapping(value = "/attribuerGpioPin/{numPin}/{nomDuPin}", method = RequestMethod.GET)
	@ApiOperation("API d'activation de port GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	public String attribuerGpioPin(@PathVariable("numPin") Integer numPin, @PathVariable("nomDuPin") String nomDuPin) {

		ListGPIO listGpio = new ListGPIO();
		listGpio.getPin(numPin);

		if (listPin.get(numPin) == null) {
			// GpioController gpio = GpioFactory.getInstance();
			pin = gpio.provisionDigitalOutputPin(listGpio.getPin(numPin), nomDuPin, PinState.LOW);
			listPin.put(numPin, pin);
			infoPinActive.put(numPin, nomDuPin);
			return "Attribution du port GPIO " + nomDuPin + " OK";
		} else {
			return "Ooops! Port n° " + numPin + " est inexistant";
		}

	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ApiOperation("Test GPIO input")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	public String test() throws InterruptedException {
		String reponse = "je suis dans la methode test";
		inPut();
		Thread.sleep(10000);
		return reponse;
	}

	public void inPut() throws InterruptedException {

		myButton.setShutdownOptions(true);

		myButton.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

				String reponse = " --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState();
				System.out.println(reponse);

			}

		});
		 while(true) {
	            Thread.sleep(500);
	        }

	}

}