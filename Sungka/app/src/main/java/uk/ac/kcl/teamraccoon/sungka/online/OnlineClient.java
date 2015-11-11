package uk.ac.kcl.teamraccoon.sungka.online;


import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
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
            clientConnection = new Socket();
            clientConnection.connect(new InetSocketAddress(serverIP,6273),3000);

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

    //receive a Player object from the server
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

    //send a move in the form of a tray index integer to server
    public void sendMove(int index) {
        try {
            outputStream.writeInt(index);
            Log.i("OnlineClient", "Client successfully sent move " + index);
            outputStream.flush();
        } catch(IOException e) {
            Log.e("OnlineClient","" + Log.getStackTraceString(e));
        }
    }

    //receive a move in the from of a tray index integer from server
    public int receiveMove() {
        Log.i("OnlineClient","Client attempting to receive move");
        try {
            int index = inputStream.readInt();
            Log.d("OnlineClient", "Client successfully received move " + index);
            return index;
        } catch (IOException e) {
            Log.i("OnlineClient","Client failed to receive move");
            Log.e("OnlineClient","" + Log.getStackTraceString(e));
        }

        return -1;

    }

    public void closeConnection() {
        try {
            outputStream.close();
            inputStream.close();
            clientConnection.close();
        } catch (IOException e) {
            Log.e("OnlineClient","IOException when closing client connection " + Log.getStackTraceString(e));
        }
    }

}
