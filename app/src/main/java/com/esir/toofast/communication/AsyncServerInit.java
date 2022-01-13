package com.esir.toofast.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.esir.toofast.activities.ActivityMulti;
import com.esir.toofast.utils.SocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//https://developer.android.com/training/connect-devices-wirelessly/wifi-direct#create-group
public class AsyncServerInit extends AsyncTask<Void, Void, Socket> {

    private ActivityMulti callback;
    private final String TAG = "AsyncServerInit";

    public AsyncServerInit(ActivityMulti callback){
        this.callback = callback;
    }

    @Override
    protected Socket doInBackground(Void... inutile) {

        try{

            Log.d("SERVEUR", "doInBackground: Serveur lancé");

            ServerSocket serverSocket = new ServerSocket(8888);
            Socket socket = serverSocket.accept();

            Log.d("SERVEUR", "doInBackground: Socket connecté lancé");

            return socket;
        }
        catch (IOException e){
            Log.d("SERVEUR", "doInBackground: Erreur lors de la création du serveur : "+e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Socket socket) {
        if(socket==null||!socket.isConnected()){//En cas de problème
            callback.onAsyncServerInitResult(false);
        }
        else{
            SocketHandler.initializeSocket(socket);
            callback.onAsyncServerInitResult(true);
        }
    }
}
