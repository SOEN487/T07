package com.example.rest;

import java.security.SecureRandom;

public class User {
    private static final int TOKEN_LENGTH = 20;
    private String username;
    private String password;
    private String token;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.token = "";
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getToken(){
        return this.token;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String toString() {
        return String.format("Username: %s, Password: %s",
                this.getUsername(), this.getPassword());
    }

    public void generateToken(){
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;

        StringBuilder sb = new StringBuilder(TOKEN_LENGTH);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        this.token = sb.toString();
    }

    public void destroyToken(){
        this.token = "";
    }
}
