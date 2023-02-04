package com.example.familymapclient.Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.ServerProxy;

import java.net.MalformedURLException;
import java.net.URL;

import Request.LoginRequest;
import Result.LoginResult;


public class LoginTask implements Runnable {

    private final Handler messageHandler;
    private LoginResult result;
    private LoginRequest request;

    public LoginTask(Handler messageHandler, LoginRequest request) {
        this.messageHandler = messageHandler;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            ServerProxy proxy = new ServerProxy();
            URL url = new URL("http://10.0.2.2:8080/user/login");
            result = proxy.loginURL(url, request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result.setSuccess(false);
        }
        sendMessage();
    }

    private void sendMessage() {
        //DO NOT CALL HANDLE MESSAGE HERE//

        Message message = Message.obtain();   //should equal login result
        Bundle messageBundle = new Bundle();
        String resultString = "rString";
        String authToken = "auth";
        String personID = "pID";
        messageBundle.putBoolean(resultString, result.isSuccess());
        messageBundle.putString(authToken, result.getAuthtoken());
        messageBundle.putString(personID, result.getPersonID());
        message.setData(messageBundle);//stores if result was successful in logging in
        //if successful, store authtoken
        messageHandler.sendMessage(message);
    }
}
