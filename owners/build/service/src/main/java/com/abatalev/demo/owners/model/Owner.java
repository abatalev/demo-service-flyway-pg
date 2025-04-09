package com.abatalev.demo.owners.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Owner {
    private String nickName;
    public String name;
    public int errCode;
    public String errMessage;

    @JsonCreator
    public Owner(String nickName, String name) {
        this.nickName = nickName;
        this.name = name;
    }

    public Owner(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public String getNickName() {
        return nickName;
    }

    public String getName() {
        return name;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
