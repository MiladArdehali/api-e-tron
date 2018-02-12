package com.milad.pi4led.controller;

import com.pi4j.io.gpio.*;
import io.swagger.annotations.*;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@RestController
@RequestMapping(value = "/api/products")
@Api(value="ProductsControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class LedController {

    private static GpioPinDigitalOutput pin;
/*
    public String greeting() {

        return "Hello world!";
    }
*/
    @RequestMapping(value = "/{num}", method = RequestMethod.GET)
    @ApiOperation("Afficher le message")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = ErrorMessages.class)})
    public String getMessage(@PathVariable("num") String num) {

        System.out.println(num.toString());

        return "Numero du Gpio à traiter :" + num;
    }
/*
    @RequestMapping("/toggle")
    public String toggle() {

        getPin().toggle();

        return "OK";

    }

    private String checkState() {
        return (getPin().isHigh() ? "Light is on" : "Light is off");
    }

    @RequestMapping("/on")
    public String on() {
        getPin().high();

        return checkState();
    }

    @RequestMapping("/off")
    public String off() {
        getPin().low();

        return checkState();
    }

    @RequestMapping("/blink")
    public String blink() {
        getPin().blink(200L, 2000L);
        return "Light is blinking";
    }

    @RequestMapping("/pulse")
    public String pulse() {
        getPin().pulse(5000L);
        return "Light is pulsing";
    }

    public GpioPinDigitalOutput getPin() {


        if (pin == null) {
            GpioController gpio = GpioFactory.getInstance();
            pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "MyLED", PinState.LOW);
        }

        return pin;
    }
*/
}