package com.abatalev.demo.stub.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Owner {
    private String nickName;
    public String name;
    public int errCode;
    public String errMessage;

    public Owner(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public Owner(String nickName, String name) {
        this.nickName = nickName;
        this.name = name;
        this.errCode = 0;
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
