package com.abatalev.demo.things.controller;

import com.abatalev.demo.things.model.Thing;
import com.abatalev.demo.things.service.ThingService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
