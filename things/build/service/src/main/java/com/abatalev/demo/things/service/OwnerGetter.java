package com.abatalev.demo.things.service;

import com.abatalev.demo.things.model.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OwnerGetter {
    private final RestClient restClient;
    private final String url;

    @Autowired
    public OwnerGetter(
            @Value("${app.owner.host}") String host,
            @Value("${app.owner.port}") String port,
            final RestClient restClient) {
        this.url = "http://" + host + ":" + port + "/owners/{name}";
        this.restClient = restClient;
    }

    public Owner get(String nickName) {
        Owner owner;
        try {
            owner = restClient
                    .get()
                    .uri(url, nickName)
                    .retrieve()
                    .toEntity(Owner.class)
                    .getBody();
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
