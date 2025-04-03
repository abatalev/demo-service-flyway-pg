package com.abatalev.demo.dbservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.abatalev.demo.dbservice.model.Owner;

@Component
public class OwnerGetter {
    private final WebClient client;

    @Autowired
    public OwnerGetter(@Value("app.owner.host") String host, @Value("app.owner.port") String port) {
        this.client = WebClient.builder()
            .baseUrl("http://"+host+":"+port)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
            .build();
    }

    public Owner get(String nickName) {
        Owner owner;
        try {
            owner = client.get().uri("/owners/{name}", nickName)
                .retrieve().bodyToMono(Owner.class).block();
        } catch(Throwable exp){
            throw new RuntimeException("Internal Error",exp);
        }

        if (owner == null){
            throw new RuntimeException("Internal Error");
        }

        if (owner.errCode != 0) {
            throw new RuntimeException(owner.errMessage);
        }
        return owner;
    }
}
