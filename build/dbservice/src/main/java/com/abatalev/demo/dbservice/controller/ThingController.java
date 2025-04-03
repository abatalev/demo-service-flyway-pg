package com.abatalev.demo.dbservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.abatalev.demo.dbservice.model.Thing;
import com.abatalev.demo.dbservice.service.ThingService;

@RestController
public class ThingController {
    private static Logger log = LoggerFactory.getLogger(ThingController.class);
    private final ThingService service;

    @Autowired
    public ThingController(ThingService service) {
        this.service = service;
    }

    @GetMapping("/things")
    List<Thing> all() {
        log.info("run all");
        return service.findAll();
    }

    @PostMapping("/things/{owner}")
    Thing newThing(@PathVariable("owner") String owner, @RequestBody Thing thing) {
        service.save(owner, thing);
        return thing;
    }
}
