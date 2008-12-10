package mpdmeserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.bluetooth.RemoteDevice;
import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.communication.ConnectionListener;
import net.java.dev.marge.entity.ServerDevice;
import net.java.dev.marge.entity.config.ServerConfiguration;
import net.java.dev.marge.factory.CommunicationFactory;
import net.java.dev.marge.factory.RFCOMMCommunicationFactory;
import net.java.dev.marge.util.UUIDGenerator;

/**
 *
 * @author sur
 */
public class Main implements ConnectionListener, CommunicationListener {

    private ServerDevice serverDevice;
    private RemoteDevice remoteDevice;
    private final static String SERVER_NAME = "mpdme Server";

    public void runServer() {
        System.out.println("Waiting for clients ...");
        CommunicationFactory factory = new RFCOMMCommunicationFactory();
        ServerConfiguration serverConfig = new ServerConfiguration(this);
        serverConfig.setMaxNumberOfConnections(8);
        // If the server name and UUID are not specified it uses a standard one.
        factory.waitClients(serverConfig, this);
    }

    public static void main(String[] args) {
        Main instance = new Main();
        instance.runServer();
    }

    public void receiveMessage(byte[] message) {
        String receivedString = new String(message);
        System.out.println(receivedString);
        try {
            Socket mpdSocket = new Socket("localhost", 6600);
            PrintWriter toMpd = new PrintWriter(mpdSocket.getOutputStream(), true);
            BufferedReader fromMpd = new BufferedReader(new InputStreamReader(mpdSocket.getInputStream()));

            if ("play".equals(receivedString)) {
                toMpd.println("play");
            }

            if ("stop".equals(receivedString)) {
                toMpd.println("stop");
            }

            String answer = fromMpd.readLine();
            System.out.println("mpd responded: " + answer);

            toMpd.close();
            fromMpd.close();
            mpdSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void errorOnReceiving(IOException e) {
        e.printStackTrace();
    }

    public void errorOnSending(IOException e) {
        e.printStackTrace();
    }

    public void connectionEstablished(ServerDevice serverDevice, RemoteDevice remoteDevice) {
        System.out.println("Connection has been established.");
        this.serverDevice = serverDevice;
        this.remoteDevice = remoteDevice;

        this.serverDevice.startListening();
    }

    public void errorOnConnection(IOException e) {
        e.printStackTrace();
    }
}
