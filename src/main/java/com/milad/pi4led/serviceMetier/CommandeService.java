package com.milad.pi4led.serviceMetier;

import com.milad.pi4led.move.MoveBO;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.ListGPIO;

import java.util.*;

public class CommandeService {
	
	public static GpioPinDigitalOutput pin;
	private static GpioController gpio = GpioFactory.getInstance();
	public static HashMap<Integer, GpioPinDigitalOutput> listPinNeed = new HashMap<Integer, GpioPinDigitalOutput>();
	private List<Integer> portDisponible = Arrays.asList(8,9,7,0,2,3,12,13,14,30,21,22,23,24,25,15,16,1,4,5,6,10,11,31,26,27,28,29);
	private List<MoveBO> listMoveBO;


	public Boolean attribuerPort (JSONArray attributionPortJSON) {

		ListGPIO listGpio = new ListGPIO();
		Boolean attribution = true;

		System.out.println(attributionPortJSON.toString());

		for (int i=0; i < (attributionPortJSON.size())-1; i++) {
			System.out.println("Methode AttribuerPort, boucle for qui parcours les elements du JSONarray - n° " + i);

			if(portDisponible.contains(attributionPortJSON.get(i))
					&& listPinNeed.get(attributionPortJSON.get(i)) == null
					&& attribution) {
				String nomGPIO = "GPIO n° " + attributionPortJSON.get(i);
				pin = gpio.provisionDigitalOutputPin(listGpio.getPin((Integer) attributionPortJSON.get(i)), nomGPIO, PinState.LOW);
				listPinNeed.put((Integer) attributionPortJSON.get(i), pin);

			} else {
				attribution = false;
				System.out.println(attribution);
				return attribution;
			}
		}

		System.out.println(attribution);
		return attribution;
	}

	public Boolean loadJsonInstruction (JSONArray instructionJson) {

		System.out.println("Methode loadJsonInstruction");
		System.out.println(instructionJson.toString());


		for (int i = 0; i < (instructionJson.size())-1; i++ ) {
			System.out.println("Methode loadJsonInstruction, boucle for qui parcours les elements du JSONarray - n° " + i);
			JSONObject jsonObject = (JSONObject) instructionJson.get(i);
			if (listPinNeed.containsKey(jsonObject.get("port"))){
				listMoveBO.add(jsonObjectToListMoveBO(jsonObject));
			} else {
				System.out.println("false");
				return false;
			}
		}
		System.out.println("true");
		return true;
	}

	public MoveBO jsonObjectToListMoveBO(JSONObject jsonObject) {

		System.out.println("Methode jsonObjectToListMoveBO");
		System.out.println(jsonObject.toString());

		MoveBO moveBO = new MoveBO((Integer) jsonObject.get("port"), (Integer)jsonObject.get("time"));
		listMoveBO.add(moveBO);

		System.out.println("Retour : " + moveBO.toString());

		return moveBO;
	}

	public Boolean execute () {

		System.out.println("Methode execute");

		Boolean exectution = true;

		for (MoveBO item : listMoveBO) {
			listPinNeed.get(item.getPort()).pulse(item.time);
			try {
				pin.wait(item.time);
			} catch (InterruptedException e){
				exectution = false;
				e.printStackTrace();
			}
		}

		System.out.println("Retour : " + exectution);

		return exectution;
	}

	public Boolean isInstructionExist (){

		System.out.println("Methode isInstructionExist");

		if (listMoveBO != null) {
			System.out.println("true");
			return true;
		} else {
			System.out.println("false");
			return false;
		}
	}

}
