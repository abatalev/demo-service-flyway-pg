package com.abatalev.demo.dbservice.controller;

import com.abatalev.demo.dbservice.model.Owner;
import com.abatalev.demo.dbservice.service.OwnersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OwnersController {
    private final OwnersService service;

    @Autowired
    public OwnersController(OwnersService service) {
        this.service = service;
    }

    @GetMapping("/owners/{nickname}")
    Owner getOwner(@PathVariable("nickname") String nickname) {
        return service.getByNickName(nickname);
    }

    @PostMapping("/owners/")
    Owner newOwner(@RequestBody Owner owner) {
        service.save(owner);
        return owner;
    }
}
