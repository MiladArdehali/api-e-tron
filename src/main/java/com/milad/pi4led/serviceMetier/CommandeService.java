package com.milad.pi4led.serviceMetier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.milad.pi4led.move.MoveBO;

import util.ConstantVal;

import java.util.*;

public class CommandeService {
	

	private List<Integer> portDisponible = Arrays.asList(8,9,7,0,2,3,12,13,14,30,21,22,23,24,25,15,16,1,4,5,6,10,11,31,26,27,28,29);
	private List<MoveBO> listMoveBO;





	public Boolean loadJsonInstruction (JsonArray instructionJson) {

		for (int i = 0; i < (instructionJson.size())-1; i++ ) {
			JsonObject jsonObject = (JsonObject) instructionJson.get(i);
				listMoveBO.add(jsonObjectToListMoveBO(jsonObject));
		}
		return true;
	}

	public MoveBO jsonObjectToListMoveBO(JsonObject jsonObject) {

		MoveBO moveBO = new MoveBO(jsonObject.get(ConstantVal.port).getAsInt(), jsonObject.get(ConstantVal.time).getAsInt());
		listMoveBO.add(moveBO);

		return moveBO;
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
