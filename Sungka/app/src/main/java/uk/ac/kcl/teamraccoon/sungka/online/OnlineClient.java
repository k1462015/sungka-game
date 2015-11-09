package uk.ac.kcl.teamraccoon.sungka.online;


import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import uk.ac.kcl.teamraccoon.sungka.Player;

public class OnlineClient {

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket clientConnection;
    private String serverIP;

    public OnlineClient(String serverIP) throws IOException {

        Log.i("OnlineClient", "OnlineClient class constructed");

        this.serverIP = serverIP;

        try{

            //attempt to connect to ServerSocket with Socket clientConnection
            clientConnection = new Socket(InetAddress.getByName(serverIP), 6273);

        } catch (UnknownHostException e) {
            Log.e("OnlineClient", "Exception was caught with error message " + Log.getStackTraceString(e));
        }
        try {

            //initialise the streams for receiving data from and sending data to server
            initialiseStreams();

        } catch (IOException e) {
            Log.e("OnlineClient", "Exception was caught with error message " + Log.getStackTraceString(e));
        }
    }

    /**
     * initialise the ObjectOutputStream and ObjectInputStream
     * @throws IOException
     */

    private void initialiseStreams() throws IOException {

        Log.d("OnlineClient", "initialiseStreams method called");
        outputStream = new ObjectOutputStream(clientConnection.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(clientConnection.getInputStream());
        Log.d("OnlineClient", "outputStream and inputStream initialised");

    }

    public Player receivePlayer() {
        try {
            Player chosenPlayer = (Player) inputStream.readObject();
            return  chosenPlayer;
        } catch(ClassNotFoundException e) {
            Log.e("OnlineClient","" + Log.getStackTraceString(e));
        } catch(IOException e) {
            Log.e("OnlineClient","" + Log.getStackTraceString(e));
        }
        return null;
    }

    public void sendMove(int index) {
        try {
            outputStream.writeInt(index);
            outputStream.flush();
        } catch(IOException e) {
            Log.e("OnlineClient","" + Log.getStackTraceString(e));
        }
    }

    public int receiveMove() {
        try {
            int index = inputStream.readInt();
            return index;
        } catch (IOException e) {
            Log.e("OnlineClient","" + Log.getStackTraceString(e));
        }

        return -1;

    }

}
