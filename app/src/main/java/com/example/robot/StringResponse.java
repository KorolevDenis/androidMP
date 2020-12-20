package com.example.robot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StringResponse {
    @SerializedName("Message")
    @Expose
    private String message;

    public StringResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}