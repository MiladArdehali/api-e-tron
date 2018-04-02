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
import java.util.Date;
import java.util.HashMap;

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
	private CommandeService commandeService;

	@RequestMapping(value = "listerPinActive", method = RequestMethod.GET)
	@ApiOperation("Afficher les ports GPIO sollicité")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public JSONObject listerPinActive() {

		System.out.println("API : " + "listerPinActive");
		JSONObject listeRetour = new JSONObject(infoPinActive);

		return listeRetour;
	}

	@RequestMapping(value = "/on/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Allumer un port manuellement")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String on(@PathVariable("numPin") int numPin) {

		System.out.println("API : " + "on");
		System.out.println("variable : " + numPin);
		ListGPIO listGpio = new ListGPIO();
		String nomPin = "port N° " + numPin;
		gpio.provisionDigitalOutputPin(listGpio.getPin(numPin), nomPin, PinState.HIGH);

			return "le port GPIO n° " + numPin + " est activé";


	}

	@RequestMapping(value = "/reservePort/{ArrayList}", method = RequestMethod.GET)
	@ApiOperation("Validation et reservation des ports")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String ValidationPort(@PathVariable("ArrayList") ArrayList<Integer> instruction) {

		System.out.println("API : " + "ValidationPort");
		System.out.println("variable : " + instruction.toString());
		if(commandeService.attribuerPort(instruction)) {
			return "validation et reservation terminé";
		} else {
			return "echec lors de la validation ou la reservation des ports";
		}
	}

	@RequestMapping(value = "/loadInstruction/{json}", method = RequestMethod.GET)
	@ApiOperation("Chargement des instructions")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String LoadInstruction(@PathVariable("json") JSONArray instruction) {

		System.out.println("API : " + "LoadInstruction");
		System.out.println("variable : " + instruction.toString());
		if(commandeService.loadJsonInstruction(instruction))
			return " instruction chargé avec succes ";
		else
			return "echec lors du chargements des instructions, verifier les données à traiter";

	}

	@RequestMapping(value = "/execInstruction/{json}", method = RequestMethod.GET)
	@ApiOperation("Exectuter les instructions")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String ExecuteInstruction() {

		System.out.println("API : " + "ExecuteInstruction");
		if(commandeService.isInstructionExist())
			commandeService.execute();
		else
			return "Aucune instruction n'est chargé";

		return "instruction terminé";


	}

	@RequestMapping(value = "/off/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Eteindre un port manuellement")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String off(@PathVariable("numPin") int numPin) {

		System.out.println("API : " + "off");
		System.out.println("variable : " + numPin);
		ListGPIO listGpio = new ListGPIO();
		String nomPin = "port N° " + numPin;
		gpio.provisionDigitalOutputPin(listGpio.getPin(numPin), nomPin, PinState.LOW);

		return "le port GPIO n° " + numPin + " est eteind";


	}

	@RequestMapping(value = "/pulse/{numPin}/{duration}", method = RequestMethod.GET)
	@ApiOperation("Mettre en place des pulsion")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ErrorMessages.class) })
	public String pulse(@PathVariable("numPin") int numPin, @PathVariable("duration") long duration) {

		System.out.println("API : " + "pulse");
		System.out.println("variable : " + duration);
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
	public void stopALL() {

	//TODO: Methode a faire

	}

	@RequestMapping(value = "/infoTechniqueGPIO/{numPin}", method = RequestMethod.GET)
	@ApiOperation("Obtenir les information technique d'un GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	public String numPin(@PathVariable("numPin") int numPin) {

		System.out.println("API : " + "numPin");
		System.out.println("variable : " + numPin);
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
	@ApiOperation("API d'activation isolé d'un port GPIO")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = String.class) })
	public String attribuerGpioPin(@PathVariable("numPin") Integer numPin, @PathVariable("nomDuPin") String nomDuPin) {

		System.out.println("API : " + "attribuerGpioPin");
		System.out.println("variable : " + nomDuPin);
		ListGPIO listGpio = new ListGPIO();
		listGpio.getPin(numPin);

		if (listPin.get(numPin) == null) {
			pin = gpio.provisionDigitalOutputPin(listGpio.getPin(numPin), nomDuPin, PinState.LOW);
			listPin.put(numPin, pin);
			infoPinActive.put(numPin, nomDuPin);
			return "Attribution du port GPIO " + nomDuPin + " OK";
		} else {
			return "Ooops! Port n° " + numPin + " est inexistant";
		}

	}
/*
	@RequestMapping(value = "/intercepterChangementSignal/{numPin}", method = RequestMethod.GET)
	@ApiOperation("intercepterChangementSignal")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK") })
	public String intercepterChangementSignal(@PathVariable("numPin") Integer numPin) throws InterruptedException {
		String result = inPut(numPin);
		return result;
	}

	public String inPut(Integer numPin) throws InterruptedException {

		ListGPIO listGpio = new ListGPIO();

		if (listGpio.getPin(numPin) == null) {

			final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(listGpio.getPin(numPin),
					PinPullResistance.PULL_DOWN);

			myButton.setShutdownOptions(true);

			myButton.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

					Date date = new Date();

					String reponse = " --> GPIO PIN STATE CHANGE: " + event.getPin().toString() + " = "
							+ event.getState().toString() + " a la date :" + date.toString();
					System.out.println(reponse);

				}

			});
			while (true) {
				Thread.sleep(20);
			}
			
		} else {
			return "Le GPIO est deja utilisé pour en sortie";
		}
	}
*/
}