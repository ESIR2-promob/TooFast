package com.esir.toofast.utils;

import android.util.Log;

import java.net.Socket;

public class SocketHandler {

    private static Socket socket;
    public static Boolean isInitialized = false;

    public static void initializeSocket(Socket socket){
        Log.d("SocketHandler", "initializeSocket: Initialisation du socket");
        SocketHandler.socket = socket;
        isInitialized = true;
    }

    public static Socket getSocket(){
        return socket;
    }
}
