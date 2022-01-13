package com.esir.toofast.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.esir.toofast.activities.ActivityMulti;
import com.esir.toofast.utils.SocketHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class AsyncClientInit extends AsyncTask<String, Void, Socket> {

    private ActivityMulti callback;
    private final String TAG = "AsyncClientInit";

    public AsyncClientInit(ActivityMulti callback){
        this.callback = callback;
    }

    @Override
    protected Socket doInBackground(String... address) {
        Socket socket = new Socket();

        Log.d(TAG, "doInBackground: Tentative de connexion");

        try{
            socket.connect(new InetSocketAddress(address[0],8888),5000);
        } catch (IOException e){
            Log.d(TAG, "doInBackground: Impossible de se connecter : " + e);
        }

        return socket;
    }

    @Override
    protected void onPostExecute(Socket socket) {
        if(socket.isConnected()){
            Log.d(TAG, "onPostExecute: Socket connecté, le prorgramme peut continuer");
            SocketHandler.initializeSocket(socket);
            callback.onAsyncClientInitResult(true);
        }
        else{
            Log.d(TAG, "onPostExecute: Socket non connecté");
            callback.onAsyncClientInitResult(false);
        }
    }
}
