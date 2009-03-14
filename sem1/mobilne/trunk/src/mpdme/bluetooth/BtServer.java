package mpdme.bluetooth;

import mpdme.*;
import java.io.IOException;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.entity.ClientDevice;
import net.java.dev.marge.entity.config.ClientConfiguration;
import net.java.dev.marge.factory.CommunicationFactory;
import net.java.dev.marge.factory.L2CAPCommunicationFactory;
import net.java.dev.marge.factory.RFCOMMCommunicationFactory;

public final class BtServer implements CommunicationListener {
    public final static String SERVER_NAME = "mpdme";

    private static BtServer instance = null;
    private RemoteDevice device = null;
    private ServiceRecord service = null;
    private ClientDevice server = null;

    public ClientDevice getServer() {
        return server;
    }

    public void setServer(ClientDevice server) {
        this.server = server;
    }

    public ServiceRecord getService() {
        return service;
    }

    public void setService(ServiceRecord service) {
        this.service = service;
    }

    public RemoteDevice getDevice() {
        return device;
    }

    public void setDevice(RemoteDevice device) {
        this.device = device;
    }

    public final static BtServer getInstance() {
        if (BtServer.instance == null) {
            BtServer.instance = new BtServer();
        }

        return BtServer.instance;
    }

    public void disconnect() {
        if (this.server == null)
            return;

        this.server.send("quit".getBytes());

        // wait a little
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }

        this.server.stopListenning();
        this.server.close();
    }

    public void connect() {
        ClientConfiguration clientConfig = new ClientConfiguration(BtServer.getInstance().getService(), this);
        CommunicationFactory factory = new L2CAPCommunicationFactory();

        try {
            this.server = factory.connectToServer(clientConfig);
        } catch (IOException ex) {
            throw new MpdException(ex);
        }
    }

    public void receiveMessage(byte[] message) {
        System.out.println("wrong place :-( " + new String(message));
    }

    public void errorOnReceiving(IOException e) {
        throw new MpdException(e);
    }

    public void errorOnSending(IOException e) {
        throw new MpdException(e);
    }
}
