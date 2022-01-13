package com.esir.toofast.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.esir.toofast.utils.SocketHandler;
import com.esir.toofast.activities.ActivityAttenteServeur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AsyncServer extends AsyncTask<String, Void, String> {

    final String TAG = "AsyncServer";
    private ActivityAttenteServeur callback;

    public AsyncServer(ActivityAttenteServeur callback){
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... seed) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(SocketHandler.getSocket().getInputStream()));
            OutputStreamWriter out = new OutputStreamWriter(SocketHandler.getSocket().getOutputStream());

            Log.d(TAG, "doInBackground: ECOUTE EN COURS");

            while(true) {
                Log.d(TAG, "doInBackground: LECTURE D'UNE LIGNE : " + input.ready());
                String response = input.readLine();

                Log.d(TAG, "valeur de la réponse du client: " + response);
                if(response!= null) {
                    switch (response) {
                            case "PREPARE":
                            out.write("PREPARE;"+seed[0] + "\r\n");
                            out.flush();
                            break;
                        case "READY":
                            out.write("OKREADY\r\n");
                            out.flush();
                            return "0";
                        default:
                            String[] parts =response.split(";");
                            if(parts[0].equals("SCORE")){

                                out.write("SCORE;"+seed[1]+"\r\n");
                                out.flush();
                                return parts[1];
                            }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.onAsyncServerResponse(Integer.parseInt(result));
    }
}
