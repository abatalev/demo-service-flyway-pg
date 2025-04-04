package com.abatalev.demo.dbservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Thing {
    private String name;

    @JsonCreator
    public Thing(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
