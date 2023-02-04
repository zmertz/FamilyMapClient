package com.example.familymapclient.Tasks;

import android.os.Handler;


import com.example.familymapclient.DataCache;
import com.example.familymapclient.ServerProxy;

import java.net.MalformedURLException;
import java.net.URL;

import Result.AllEventResult;
import Result.EventIDResult;


public class GetEventTask implements Runnable {

    private final Handler messageHandler;
    private EventIDResult result;
    private AllEventResult eventsResult;
    private String authToken;
    private String eventID;
    DataCache cache;

    public GetEventTask(Handler messageHandler, String authToken) {//, String eventID) {
        this.messageHandler = messageHandler;
        this.authToken = authToken;
        //this.eventID = eventID;
        this.cache = DataCache.getInstance();
    }

    @Override
    public void run() {
        try {
            ServerProxy proxy = new ServerProxy();
            //URL url = new URL("http://10.0.2.2:8080/event/" + eventID);
            //result = proxy.GetEventURL(url, authToken);

            URL url2 = new URL("http://10.0.2.2:8080/event/");
            eventsResult = proxy.GetAllEventURL(url2, authToken);
            cache.addAllEvents(eventsResult.getData());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            //result.setSuccess(false);
        }
        //sendMessage();
    }

}
