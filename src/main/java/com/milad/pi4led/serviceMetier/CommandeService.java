package com.milad.pi4led.serviceMetier;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.ListGPIO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandeService {
	
	public static GpioPinDigitalOutput pin;
	private static GpioController gpio = GpioFactory.getInstance();
	public static HashMap<Integer, GpioPinDigitalOutput> listPin = new HashMap<Integer, GpioPinDigitalOutput>();
	public static HashMap<Integer, String> infoPinActive = new HashMap<Integer, String>();
	private List<Integer> portDisponible = Arrays.asList(8,9,7,0,2,3,12,13,14,30,21,22,23,24,25,15,16,1,4,5,6,10,11,31,26,27,28,29);


	public Boolean readJsonInstruction (JSONArray instructionJson) {

		for (int i = 0; i < instructionJson.size(); i++ ) {
			JSONObject jsonObject = (JSONObject) instructionJson.get(i);
			if((Integer)jsonObject.get("temps") == 0){
				listPin.get(jsonObject.get("port")).low();
			} else {
				listPin.get(jsonObject.get("port")).pulse((Integer) jsonObject.get("temps"));
			}
		}

		return true;
	}

	public Boolean attribuerPort (JSONArray attributionPortJSON) {

		ListGPIO listGpio = new ListGPIO();
		Boolean attribution = true;

		//Interpretation du JSON
		for (int i = 0; i < attributionPortJSON.size(); i++) {
			JSONObject jsonObject = (JSONObject) attributionPortJSON.get(i);
			if (portDisponible.contains((Integer) jsonObject.get("port"))
					&& listPin.get((Integer) jsonObject.get("port")) == null
					&& attribution) {
				pin = gpio.provisionDigitalOutputPin(listGpio.getPin((Integer) jsonObject.get("port")), (String) jsonObject.get("nom"), PinState.LOW);
				listPin.put((Integer) jsonObject.get("port"), pin);
			} else {
				attribution = false;
				return attribution;
			}
		}

		return attribution;
	}

}
