package com.milad.pi4led.move;

public class MoveBO {

    public Integer port;
    public Integer time;

    public MoveBO(Integer port, Integer time){
        this.port = port;
        this.time = time;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}