package com.milad.pi4led.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.milad.pi4led.serviceMetier.CommandeService;

import io.swagger.annotations.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Api(value = "ProductsControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class LedController {

	public static HashMap<Integer, String> infoPinActive = new HashMap<Integer, String>();
	private CommandeService commandeService;


	@RequestMapping(value = "/loadInstruction/{json}", method = RequestMethod.GET)
	@ApiOperation("Chargement des instructions")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ErrorMessages.class)})
	public String LoadInstruction(@PathVariable("json") String instruction) {


		JsonParser jsonParser = new JsonParser();
		JsonArray objectFromString = jsonParser.parse(instruction).getAsJsonArray();

		JsonObject jsonObject  = objectFromString.get(0).getAsJsonObject();

		//Commencer les appels

	return "OK";
	}
}