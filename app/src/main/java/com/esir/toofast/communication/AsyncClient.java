package com.esir.toofast.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.esir.toofast.utils.SocketHandler;
import com.esir.toofast.activities.ActivityAttenteClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class AsyncClient extends AsyncTask<String, Void, String> {

    final String TAG = "AsyncClient";
    private ActivityAttenteClient callback;

    public AsyncClient(ActivityAttenteClient callback){
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... messages) {

        for (String message: messages) {
            Log.d(TAG, "Sending this to server : "+message);
            try {
                PrintWriter out = new PrintWriter(SocketHandler.getSocket().getOutputStream());
                //out.println("PREPARE\n");
                out.println(message);
                out.flush();
                Log.d(TAG, "doInBackground: Erreur d'envoie ? "+out.checkError());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Log.d(TAG, "Waiting for server response");

                BufferedReader input = new BufferedReader(new InputStreamReader(SocketHandler.getSocket().getInputStream()));
                String response = input.readLine();

                Log.d(TAG, "Serveur response: "+response);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        callback.onAsyncClientResponse(s);
    }
}
