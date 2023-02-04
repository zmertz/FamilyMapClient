package com.example.familymapclient.Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapclient.DataCache;
import com.example.familymapclient.ServerProxy;

import java.net.MalformedURLException;
import java.net.URL;

import Model.Person;
import Request.LoginRequest;
import Request.PersonIDRequest;
import Result.AllEventResult;
import Result.FamilyResult;
import Result.LoginResult;
import Result.PersonIDResult;

public class GetPersonTask implements Runnable {

    private final Handler messageHandler;
    private PersonIDResult result;
    private FamilyResult familyResult;
    private AllEventResult eventsResult;
    private String authToken;
    private String personID;
    DataCache cache;

    public GetPersonTask(Handler messageHandler, String authToken, String personID) {
        this.messageHandler = messageHandler;
        this.authToken = authToken;
        this.personID = personID;
        this.cache = DataCache.getInstance();
    }

    @Override
    public void run() {
        try {
            ServerProxy proxy = new ServerProxy();
            URL url = new URL("http://10.0.2.2:8080/person/" + personID);
            result = proxy.GetPersonURL(url, authToken);

            URL url2 = new URL("http://10.0.2.2:8080/person/");
            familyResult = proxy.GetFamilyURL(url2, authToken);
            cache.addFamily(familyResult.getData());      // i think this adds the user to the map in addition to the whole family

            URL url3 = new URL("http://10.0.2.2:8080/event/");
            eventsResult = proxy.GetAllEventURL(url3, authToken);
            cache.addAllEvents(eventsResult.getData());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            //result.setSuccess(false);
        }
        sendMessage();
    }

    private void sendMessage() {
        //DO NOT CALL HANDLE MESSAGE HERE//

        Message message = Message.obtain();   //should equal login result
        Bundle messageBundle = new Bundle();
        String resultString = "rString";
        String firstName = "firstName";
        String lastName = "lastName";
        messageBundle.putBoolean(resultString, result.isSuccess());
        messageBundle.putString(firstName, result.getFirstName());
        messageBundle.putString(lastName, result.getLastName());
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
