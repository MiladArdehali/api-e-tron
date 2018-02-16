package com.milad.pi4led.serviceMetier;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import util.ListGPIO;

public class CommandeService {
	
	public static GpioPinDigitalOutput pin;


	public GpioPinDigitalOutput getPin(int numPin) {

		ListGPIO listGpio = new ListGPIO();
		listGpio.getPin(numPin);

		// if (pin == null) {
		GpioController gpio = GpioFactory.getInstance();
		pin = gpio.provisionDigitalOutputPin(listGpio.getPin(numPin), "MyLED", PinState.LOW);
		// }

		return pin;
	}

}
