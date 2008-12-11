package mpdme.forms;

import com.sun.lwuit.Button;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import mpdme.BtServer;
import mpdme.MpdException;
import net.java.dev.marge.inquiry.DeviceDiscoverer;
import net.java.dev.marge.inquiry.InquiryListener;
import net.java.dev.marge.inquiry.ServiceDiscoverer;
import net.java.dev.marge.inquiry.ServiceSearchListener;
import net.java.dev.marge.util.UUIDGenerator;

public class InquiryForm extends MpdForm implements InquiryListener, ServiceSearchListener {

    private Label status;
    private List deviceList;
    private Vector btDevices;
    private boolean canConnect;
    private Image okImage;
    private Button loader;

    public InquiryForm() {
        super(null);
    }

    private class DevicelistSelectionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            if (!canConnect) {
                return;
            }

            RemoteDevice server = (RemoteDevice) btDevices.elementAt(deviceList.getSelectedIndex());
            BtServer.getInstance().setDevice(server);
            discoverService();
        }
    }

    private void discoverService() {
        try {
            DeviceDiscoverer.getInstance().cancelInquiry();
            this.loader.setIcon(this.okImage);

            UUID[] uuid = new UUID[]{UUIDGenerator.generate(BtServer.SERVER_NAME)};

            ServiceDiscoverer.getInstance().startSearch(uuid, BtServer.getInstance().getDevice(), this);
        } catch (IOException e) {
            throw new MpdException(e.getMessage());
        }
    }

    public void initialize() {
        Resources resources = this.getResources();

        this.okImage = resources.getImage("ok");

        this.setTitle("mpd Player");
        this.setLayout(new BorderLayout());

        Container northContainer = new Container(new BorderLayout());

        this.loader = new Button(resources.getAnimation("loader"));
        this.loader.getStyle().setBorder(null);
        this.loader.setFocusable(false);
        northContainer.addComponent(BorderLayout.WEST, loader);

        this.status = new Label("Inquiring ...");
        northContainer.addComponent(BorderLayout.CENTER, this.status);

        this.addComponent(BorderLayout.NORTH, northContainer);

        this.deviceList = new List(new DefaultListModel());
        DevicelistSelectionListener listener = new DevicelistSelectionListener();
        this.deviceList.addActionListener(listener);
        this.addComponent(BorderLayout.CENTER, this.deviceList);

        this.btDevices = new Vector();
        this.canConnect = false;

        try {
            DeviceDiscoverer.getInstance().startInquiryGIAC(this);
        } catch (BluetoothStateException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void deviceDiscovered(RemoteDevice device, DeviceClass deviceClass) {
        String name;

        try {
            name = device.getFriendlyName(false);
        } catch (Exception e) {
            e.printStackTrace();
            name = device.getBluetoothAddress();
        }

        this.deviceList.addItem(name);
        this.btDevices.addElement(device);
        this.canConnect = true;
    }

    public void inquiryCompleted(RemoteDevice[] devices) {
        this.status.setText("Inquiry complete");
        this.loader.setIcon(this.okImage);
    }

    public void inquiryError() {
        throw new MpdException("An error occured while inquiring");
    }

    public void serviceSearchCompleted(RemoteDevice remoteDevice, ServiceRecord[] services) {
        this.status.setText("services: " + services.length);

        if ((services == null) || (services.length < 1)) {
            throw new MpdException("No mpd services found.");
        }

        BtServer.getInstance().setService(services[0]);

        MainMenu menu = new MainMenu();
        menu.show();
    }

    public void deviceNotReachable() {
        throw new MpdException("The device is not reachable.");
    }

    public void serviceSearchError() {
        throw new MpdException("An service search error occured.");
    }
}
