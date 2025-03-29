package com.abatalev.demo.dbservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abatalev.demo.dbservice.model.Thing;
import com.abatalev.demo.dbservice.service.ThingService;

@RestController
public class ThingController {

    private final ThingService service;

    @Autowired
    public ThingController(ThingService service) {
        this.service = service;
    }

    @GetMapping("/things")
    List<Thing> all() {
        return service.findAll();
    }

    @PostMapping("/things")
    Thing newThing(@RequestBody Thing thing) {
        service.save(thing);
        return thing;
    }
}
