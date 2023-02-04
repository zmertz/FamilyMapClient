package com.example.familymapclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import Model.*;
import Request.*;
import Result.*;

public class ServerProxy {

    public LoginResult loginURL(URL url, LoginRequest loginRequest) {

        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.addRequestProperty("Accept", "application/json");

            conn.connect();
            Gson gson = new Gson();

            //posting data
            String reqData = gson.toJson(loginRequest);
            OutputStream reqBody = conn.getOutputStream();
            outputString(reqData, reqBody);
            reqBody.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream response = conn.getInputStream();
                Reader read = new InputStreamReader(response);

                LoginResult result = gson.fromJson(read, LoginResult.class);
                result.setErrorString("No Errors");
                result.setSuccess(true);
                conn.getInputStream().close();
                return result;   //should return here so returning null at end results in error
            } else {
                LoginResult result = new LoginResult(null, null,null,"Error");
                result.setSuccess(false);
                result.setMessage(conn.getResponseMessage());
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RegisterResult registerURL(URL url, RegisterRequest registerRequest) {
        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.addRequestProperty("Accept", "application/json");

            conn.connect();
            Gson gson = new Gson();

            //posting data
            String reqData = gson.toJson(registerRequest);
            OutputStream reqBody = conn.getOutputStream();
            outputString(reqData, reqBody);
            reqBody.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream response = conn.getInputStream();
                Reader read = new InputStreamReader(response);

                RegisterResult result = gson.fromJson(read, RegisterResult.class);
                result.setErrorString("No Errors");
                conn.getInputStream().close();
                return result;   //should return here so returning null at end results in error
            } else {
                RegisterResult result = new RegisterResult(null, null,null,"Error");
                result.setMessage(conn.getResponseMessage());
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PersonIDResult GetPersonURL(URL url, String authToken) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.addRequestProperty("Authorization", authToken);
            conn.addRequestProperty("Accept", "application/json");
            conn.connect();
            Gson gson = new Gson();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream response = conn.getInputStream();
                Reader read = new InputStreamReader(response);

                PersonIDResult result = gson.fromJson(read, PersonIDResult.class);
                result.setErrorString("No Errors");
                conn.getInputStream().close();
                return result;   //should return here so returning null at end results in error
            } else {
                PersonIDResult result = new PersonIDResult();
                result.setMessage(conn.getResponseMessage());
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FamilyResult GetFamilyURL(URL url, String authToken) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.addRequestProperty("Authorization", authToken);
            conn.addRequestProperty("Accept", "application/json");
            conn.connect();
            Gson gson = new Gson();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream response = conn.getInputStream();
                Reader read = new InputStreamReader(response);

                FamilyResult result = gson.fromJson(read, FamilyResult.class);
                result.setErrorString("No Errors");
                conn.getInputStream().close();
                return result;   //should return here so returning null at end results in error
            } else {
                FamilyResult result = new FamilyResult(null, null);
                result.setMessage(conn.getResponseMessage());
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public EventIDResult GetEventURL(URL url, String authToken) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.addRequestProperty("Authorization", authToken);
            conn.addRequestProperty("Accept", "application/json");
            conn.connect();
            Gson gson = new Gson();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream response = conn.getInputStream();
                Reader read = new InputStreamReader(response);

                EventIDResult result = gson.fromJson(read, EventIDResult.class);
                result.setErrorString("No Errors");
                conn.getInputStream().close();
                return result;   //should return here so returning null at end results in error
            } else {
                EventIDResult result = new EventIDResult(null, null);
                result.setMessage(conn.getResponseMessage());
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AllEventResult GetAllEventURL(URL url, String authToken) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.addRequestProperty("Authorization", authToken);
            conn.addRequestProperty("Accept", "application/json");
            conn.connect();
            Gson gson = new Gson();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream response = conn.getInputStream();
                Reader read = new InputStreamReader(response);

                AllEventResult result = gson.fromJson(read, AllEventResult.class);
                result.setErrorString("No Errors");
                conn.getInputStream().close();
                return result;   //should return here so returning null at end results in error
            } else {
                AllEventResult result = new AllEventResult(null, null);
                result.setMessage(conn.getResponseMessage());
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void outputString(String s, OutputStream o) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(o);
        writer.write(s);
        writer.flush();
    }
}

