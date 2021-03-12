package com.example.rest;

import java.io.Serializable;

/**
 * Custom class to hold response status and message
 */
public class MyResponse implements Serializable {

    private boolean success;
    private String token;
    private static final long serialVersionUID = 115L;

    public MyResponse(){
        super();
    }

    public MyResponse(boolean success, String token){
        super();
        this.success = success;
        this.token = token;
    }

    public boolean getSuccess(){
        return this.success;
    }

    public String getToken(){
        return this.token;
    }

    public void setSuccess(boolean success){
        this.success = success;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String toString(){
        return "Success: " + this.success + ", Token: " + this.token;
    }
}
