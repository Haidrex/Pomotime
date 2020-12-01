package com.example.pomotime;

import android.content.ClipData;
import android.graphics.ColorSpace;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RequestOperator extends Thread {
    public interface RequestOperatorListener{
        void success (int count);
        void failed (int responseCode);
    }

//    public interface CountRequestOperatorListener{
//        void successCount (int count);
//        void failedCount (int responseCode);
//    }

    private RequestOperatorListener listener;
    //private CountRequestOperatorListener countListener;
    private int responseCode;

    public void setListener (RequestOperatorListener listener) {this.listener = listener;}
    //public void setCountListener (CountRequestOperatorListener listener){this.countListener = listener;}

    @Override
    public void run(){
        super.run();
        try{

            int count = request();
            if(count != -1){
                success(count);
            }
            else{
                failed(responseCode);
            }
        }catch (IOException e){
            failed(-1);
        }catch (JSONException e){
            failed(-2);
        }
    }

//    @Override
//    public void run(){
//        super.run();
//        try{
//            //ArrayList<ModelPost> models = requestAllItems();
//            int count = requestAllItems();
//            if(count != 0){
//                successCount(count);
//            }
//            else{
//                failedCount(responseCode);
//            }
//        }catch (IOException e){
//            failedCount(-1);
//        }catch (JSONException e){
//            failedCount(-2);
//        }
//    }

    private int request() throws IOException, JSONException{
        URL object = new URL("https://jsonplaceholder.typicode.com/posts");
        HttpsURLConnection connection = (HttpsURLConnection) object.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        responseCode = connection.getResponseCode();
        Log.i("Response Code", String.valueOf((responseCode)));
        InputStreamReader inputStreamReader;

        if(responseCode == 200){
            inputStreamReader = new InputStreamReader(connection.getInputStream());
        }else{
            inputStreamReader = new InputStreamReader((connection.getErrorStream()));
        }

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String inputLine;
        StringBuffer responseStringBuffer = new StringBuffer();

        while((inputLine = bufferedReader.readLine()) != null){
            responseStringBuffer.append(inputLine);
        }
        bufferedReader.close();
        Log.i("Response Result", responseStringBuffer.toString());
        JSONArray array = new JSONArray(responseStringBuffer.toString());
        int count = array.length();
        if(array != null){
            return count;
        }else{
            return -1;
        }
    }

//    private int requestAllItems() throws IOException, JSONException{
//        URL object = new URL("https://jsonplaceholder.typicode.com/posts");
//        HttpsURLConnection connection = (HttpsURLConnection) object.openConnection();
//
//        connection.setRequestMethod("GET");
//        connection.setRequestProperty("Content-Type", "application/json");
//
//        responseCode = connection.getResponseCode();
//        Log.i("Response Code", String.valueOf((responseCode)));
//        InputStreamReader inputStreamReader;
//
//        if(responseCode == 200){
//            inputStreamReader = new InputStreamReader(connection.getInputStream());
//        }else{
//            inputStreamReader = new InputStreamReader((connection.getErrorStream()));
//        }
//
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        String inputLine;
//        StringBuffer responseStringBuffer = new StringBuffer();
//        //int cycle = 0;
//        //ArrayList<ModelPost> models = new ArrayList<ModelPost>();
//        while((inputLine = bufferedReader.readLine()) != null){
//            responseStringBuffer.append(inputLine);
////            cycle++;
////            if(cycle == 3){
////                ModelPost post = new ModelPost();
////                post = parsingJsonObject(responseStringBuffer.toString());
////                models.add(post);
////                responseStringBuffer.delete(0, responseStringBuffer.length());
////                cycle = 0;
////            }
//
//        }
//        bufferedReader.close();
//        Log.i("Response Result", responseStringBuffer.toString());
//
//        JSONArray array = new JSONArray(responseStringBuffer.toString());
//        int count = array.length();
//        if(count != 0){
//            return count;
//        }else{
//            return -1;
//        }
//    }

    public int getAllCount(String response) throws JSONException{
        JSONArray array = new JSONArray(response);
        int count = array.length();
        return count;
    }

    private void failed(int code){
        if(listener != null){
            listener.failed(code);
        }
    }

    private void success(int count){
        if(listener != null){
            listener.success(count);
        }
    }

//    private void successCount(int count){
//        if(countListener != null){
//            countListener.successCount(count);
//        }
//    }
//
//    private void failedCount(int code){
//        if(countListener != null){
//            countListener.failedCount(code);
//        }
//    }
}
