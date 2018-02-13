package util;

import java.util.ArrayList;
import java.util.HashMap;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class ListGPIO {
	
	HashMap<Integer, Pin> listGPIO = new HashMap<Integer, Pin>();
	
	public Pin getPin(Integer num) {
	
		Pin pin;
		
		listGPIO.put(0,RaspiPin.GPIO_00);
		listGPIO.put(1,RaspiPin.GPIO_01);
		listGPIO.put(2,RaspiPin.GPIO_02);
		listGPIO.put(3,RaspiPin.GPIO_03);
		listGPIO.put(4,RaspiPin.GPIO_04);
		listGPIO.put(5,RaspiPin.GPIO_05);
		listGPIO.put(6,RaspiPin.GPIO_06);
		listGPIO.put(7,RaspiPin.GPIO_07);
		listGPIO.put(8,RaspiPin.GPIO_08);
		listGPIO.put(9,RaspiPin.GPIO_09);
		listGPIO.put(10,RaspiPin.GPIO_10);
		listGPIO.put(11,RaspiPin.GPIO_11);
		listGPIO.put(12,RaspiPin.GPIO_12);
		listGPIO.put(13,RaspiPin.GPIO_13);
		listGPIO.put(14,RaspiPin.GPIO_14);
		listGPIO.put(15,RaspiPin.GPIO_15);
		listGPIO.put(16,RaspiPin.GPIO_16);
		listGPIO.put(17,RaspiPin.GPIO_17);
		listGPIO.put(18,RaspiPin.GPIO_18);
		
		pin = listGPIO.get(num);
	
	return pin;
	}

}
