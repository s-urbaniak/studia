package mpdmeserver.bluetooth;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.communication.ConnectionListener;
import net.java.dev.marge.entity.ServerDevice;
import net.java.dev.marge.entity.config.ServerConfiguration;
import net.java.dev.marge.factory.CommunicationFactory;
import net.java.dev.marge.factory.L2CAPCommunicationFactory;
import net.java.dev.marge.util.UUIDGenerator;

public class BtHandler implements ConnectionListener {

    private final static String SERVER_NAME = "mpdme";
    private ServerConfiguration serverConfig = null;
    private CommunicationFactory factory = null;
    private ServerDevice device = null;
    private RemoteDevice remoteDevice = null;

    public void startServer(CommunicationListener listener) {
        this.factory = new L2CAPCommunicationFactory();

        this.serverConfig = new ServerConfiguration(listener);
        this.serverConfig.setUuid(UUIDGenerator.generate(SERVER_NAME));
        this.serverConfig.setMaxNumberOfConnections(1);
//        this.serverConfig.addParameter(ServerConfiguration.PARAMETER_L2CAP_RECEIVEMTU, "2048");
        this.serverConfig.addParameter(ServerConfiguration.PARAMETER_L2CAP_TRANSMITMTU, "2048");

        this.factory.waitClients(this.serverConfig, this);
    }

    public void quitServer() {
        this.device.stopListenning();
        this.device.close();
        this.device = null;
        this.factory.waitClients(this.serverConfig, this);
    }

    public void send(RemoteData obj) {
        this.send(obj.encode());
    }

    public void send(byte[] message) {
        if (this.device == null) {
            throw new RuntimeException("Not connected to a bluetooth client.");
        }

        this.device.send(message);
    }

    public void connectionEstablished(ServerDevice serverDevice, RemoteDevice remoteDevice) {
        Logger.getLogger(this.getClass().getName()).log(
                Level.INFO,
                "connection established with " + remoteDevice.getBluetoothAddress()
                + ", ServerDevice = " + serverDevice);

        this.device = serverDevice;
        this.remoteDevice = remoteDevice;

        this.device.startListening();
        this.device.setEnableBroadcast(false);
    }

    public void errorOnConnection(IOException ex) {
        throw new RuntimeException(ex);
    }
}
