package mpdme;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public final class BtServer {
    public final static String SERVER_NAME = "mpdme Server";

    private static BtServer instance = null;

    private RemoteDevice device = null;

    private ServiceRecord service = null;

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
        if (BtServer.instance == null)
            BtServer.instance = new BtServer();

        return BtServer.instance;
    }
}
