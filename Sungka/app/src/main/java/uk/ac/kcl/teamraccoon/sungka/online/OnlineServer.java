package uk.ac.kcl.teamraccoon.sungka.online;

import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import uk.ac.kcl.teamraccoon.sungka.Player;

public class OnlineServer {

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ServerSocket serverSocket;
    private Socket serverConnection;

    public OnlineServer() {

        Log.d("OnlineServer", "OnlineServer class constructed");

        try {

            //setup a new ServerSocket at port number 6273
            serverSocket = new ServerSocket(6273);

            Log.d("OnlineServer", "ServerSocket serverSocket initialised");

            //listen for client to connect to server and accept the socket
            serverConnection = serverSocket.accept();

            Log.d("OnlineServer", "Connection with client made");

            //initialise the streams for receiving data from and sending data to client
            initialiseStreams();

            Log.d("OnlineServer", "ObjectOutputStream outputStream and ObjectInputStream inputStream initialised");

        } catch (IOException e) {
            Log.e("OnlineServer", "IOException was caught with error message " + e.getMessage() + " in OnlineServer constructor");
        }

    }

    private void initialiseStreams() throws IOException {

        Log.d("OnlineServer", "initialiseStreams method called");
        outputStream = new ObjectOutputStream(serverConnection.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(serverConnection.getInputStream());
        Log.d("OnlineServer", "outputStream and inputStream initialised");

    }

    public static String getServerIP() {

        try {

            for (Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces(); networkInterfaceEnumeration.hasMoreElements(); ) {
                NetworkInterface tempNetworkInterface = networkInterfaceEnumeration.nextElement();
                for(Enumeration<InetAddress> inetAddressEnumeration = tempNetworkInterface.getInetAddresses(); inetAddressEnumeration.hasMoreElements();) {
                    InetAddress tempInetAddress = inetAddressEnumeration.nextElement();
                    if(!tempInetAddress.isLoopbackAddress() && tempInetAddress.isSiteLocalAddress()) {
                        return tempInetAddress.getHostAddress().toString();
                    }
                }
            }

        } catch(SocketException e) {
            Log.e("OnlineServer",e.getMessage());
        }

        return null;

    }

    public void sendPlayer(Player chosenPlayer) {
        try {
            outputStream.writeObject(chosenPlayer);
            outputStream.flush();
        } catch(IOException e) {
            Log.e("OnlineServer",e.getMessage());
        }
    }

    public void sendMove(int index) {
        try {
            outputStream.writeInt(index);
            outputStream.flush();
        } catch(IOException e) {
            Log.e("OnlineServer",e.getMessage());
        }
    }

    public int receiveMove() {
        try {
            int index = inputStream.readInt();
            return index;
        } catch (IOException e) {
            Log.e("OnlineServer", e.getMessage());
        }

        return -1;

    }

}

