package com.example.familymapclient.Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.ServerProxy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import Request.RegisterRequest;
import Result.RegisterResult;


public class RegisterTask implements Runnable {

    private final Handler messageHandler;
    private RegisterResult result;
    private RegisterRequest request;

    public RegisterTask(Handler messageHandler, RegisterRequest request) {
        this.messageHandler = messageHandler;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            ServerProxy proxy = new ServerProxy();
            URL url = new URL("http://10.0.2.2:8080/user/register");
            result = proxy.registerURL(url, request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //result.success = false;
        }
        sendMessage();
    }

    private void sendMessage() {
        //DO NOT CALL HANDLE MESSAGE HERE//

        Message message = Message.obtain();
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
