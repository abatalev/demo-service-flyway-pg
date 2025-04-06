package com.abatalev.demo.stub.controller;

import com.abatalev.demo.stub.model.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OwnerController {
    private static Logger log = LoggerFactory.getLogger(OwnerController.class);

    @GetMapping("/owners/{nickName}")
    Owner getOwner(@PathVariable("nickName") String nickname) {
        log.info("run getOwner " + nickname);
        if ("ivanov".equals(nickname)) {
            return new Owner("ivanov", "Ivanov");
        }
        return new Owner(2, "Owner not found");
    }
}
