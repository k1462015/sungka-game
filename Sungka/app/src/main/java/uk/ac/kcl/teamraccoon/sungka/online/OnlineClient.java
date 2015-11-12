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

    public OnlineClient(String serverIP) throws IOException {

        try {

            //attempt to connect to ServerSocket with Socket clientConnection
            //connect at IP serverIP, host 6273 and with a timeout of 3000 milliseconds
            clientConnection = new Socket();
            clientConnection.connect(new InetSocketAddress(serverIP, 6273), 3000);

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
     *initialises the data streams for sending and receiving data from server
     */
    private void initialiseStreams() throws IOException {

        outputStream = new ObjectOutputStream(clientConnection.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(clientConnection.getInputStream());

    }

    /**
     *receive a Player enum from the server
     */
    public Player receivePlayer() {
        try {
            Player chosenPlayer = (Player) inputStream.readObject();
            return chosenPlayer;
        } catch (ClassNotFoundException e) {
            Log.e("OnlineClient", "" + Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e("OnlineClient", "IOException when receiving player " + Log.getStackTraceString(e));
        }
        return null;
    }

    /**
     * send a move in the form of a tray index integer to server
     * @param index of the move to be sent to client
     */
    public void sendMove(int index) {
        try {
            outputStream.writeInt(index);
            Log.i("OnlineClient", "Client successfully sent move " + index);
            outputStream.flush();
        } catch (IOException e) {
            Log.e("OnlineClient", "IOException when sending move " + Log.getStackTraceString(e));
        }
    }

    /**
     * receive a move in the from of a tray index integer from server
     * @return index of the tray of the move received from server
     */
    public int receiveMove() {
        try {
            int index = inputStream.readInt();
            return index;
        } catch (IOException e) {
            Log.e("OnlineClient", "IOException when receiving move " + Log.getStackTraceString(e));
        }

        return -1;

    }

    /**
     * close all connections with server in order to stop conflicts later
     */
    public void closeConnection() {
        try {
            outputStream.close();
            inputStream.close();
            clientConnection.close();
        } catch (IOException e) {
            Log.e("OnlineClient", "IOException when closing client connection " + Log.getStackTraceString(e));
        }
    }

}
