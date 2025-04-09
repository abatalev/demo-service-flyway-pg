package com.abatalev.demo.things.service;

import com.abatalev.demo.things.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OwnerGetter {
    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public OwnerGetter(
            @Value("${app.owner.host}") String host,
            @Value("${app.owner.port}") String port,
            final RestTemplate restTemplate) {
        this.url = "http://" + host + ":" + port + "/owners/{name}";
        this.restTemplate = restTemplate;
    }

    public Owner get(String nickName) {
        Owner owner;
        try {
            owner = restTemplate.getForObject(url, Owner.class, nickName);
        } catch (Throwable exp) {
            throw new RuntimeException("Internal Error", exp);
        }

        if (owner == null) {
            throw new RuntimeException("Internal Error");
        }

        if (owner.errCode != 0) {
            throw new RuntimeException(owner.errMessage);
        }
        return owner;
    }
}
