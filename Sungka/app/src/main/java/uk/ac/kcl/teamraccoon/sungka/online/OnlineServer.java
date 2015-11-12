package uk.ac.kcl.teamraccoon.sungka.online;

import android.util.Log;

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

    public OnlineServer() throws IOException {

        //setup a new ServerSocket at port number 6273
        serverSocket = new ServerSocket(6273);

    }

    /**
     * first, connect to opponent via a socket
     * then, initalise input and output streams
     * @throws IOException
     */
    public void findConnection() throws IOException {

        //listen for client to connect to server and accept the socket
        serverConnection = serverSocket.accept();

        //initialise the streams for receiving data from and sending data to client
        initialiseStreams();

    }

    /**
     * initialises the data streams for receiving data from and sending data to client
     * @throws IOException
     */
    private void initialiseStreams() throws IOException {

        outputStream = new ObjectOutputStream(serverConnection.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(serverConnection.getInputStream());

    }

    /**
     * finds the local IPv4 address of the device
     * @return the IP address of the device as a String
     */
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
            Log.e("OnlineServer","SocketException when finding local IP address of device " + Log.getStackTraceString(e));
        }

        return null;

    }

    /**
     * send a Player enum to the client
     * @param chosenPlayer Player enum of the randomly chosen starting player
     */
    public void sendPlayer(Player chosenPlayer) {
        try {
            outputStream.writeObject(chosenPlayer);
            outputStream.flush();
        } catch(IOException e) {
            Log.e("OnlineServer","IOException when sending Player object to client " + Log.getStackTraceString(e));
        }
    }

    /**
     * send a move in the form of a tray index integer to client
     * @param index
     */
    public void sendMove(int index) {
        try {
            outputStream.writeInt(index);
            Log.i("OnlineServer", "Server successfully sent move " + index);
            outputStream.flush();
        } catch(IOException e) {
            Log.e("OnlineServer","IOException when sending move to client " + Log.getStackTraceString(e));
        }
    }

    /**
     * receive a move in the from of a tray index integer from server
     * @return the index of the move received from client
     */
    public int receiveMove() {
        try {
            int index = inputStream.readInt();
            return index;
        } catch (IOException e) {
            Log.e("OnlineServer","IOException when receiving move from client " + Log.getStackTraceString(e));
        }

        return -1;

    }

    /**
     * closes streams and sockets so that no conflicts occur later if reuse of them is needed
     */
    public void closeConnection() {
        try {
            Log.i("OnlineServer","onlineServer closeConnection() called");
            outputStream.close();
            inputStream.close();
            serverConnection.close();
            serverSocket.close();
            Log.i("OnlineServer","onlineServer connection successfully closed");
        } catch (IOException e) {
            Log.e("OnlineServer","IOException when closing server connection " + Log.getStackTraceString(e));
        }
    }

    /**
     * just closes ServerSocket for situations when sockets and streams have not been initialised
     */
    public void closeServerSocket() {
        try {
            serverSocket.close();
        } catch(IOException e) {
            Log.e("OnlineServer","IOException when closing serverSocket");
        }
    }

}

