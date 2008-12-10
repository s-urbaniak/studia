package mpdme.forms;

import com.sun.lwuit.Button;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Image;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import javax.bluetooth.RemoteDevice;
import mpdme.BtServer;
import net.java.dev.marge.communication.ConnectionListener;
import net.java.dev.marge.entity.ServerDevice;
import net.java.dev.marge.entity.config.ClientConfiguration;
import net.java.dev.marge.factory.CommunicationFactory;
import net.java.dev.marge.factory.L2CAPCommunicationFactory;
import net.java.dev.marge.factory.RFCOMMCommunicationFactory;

public class ConnectForm extends Dialog implements ConnectionListener {

    private Button loader;
    private Image okImage;

    public ConnectForm() {
        super();
        this.initialize();
    }

    public void initialize() {
        Resources resources;

        try {
            resources = Resources.open("/resources.res");
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        this.okImage = resources.getImage("ok");
        this.loader = new Button(resources.getAnimation("loader"));
        this.loader.getStyle().setBorder(null);
        this.loader.setFocusable(false);

        this.setTitle("Connecting ...");
        this.setLayout(new BorderLayout());
        this.addComponent(BorderLayout.CENTER, this.loader);
    }

    public void connectionEstablished(ServerDevice serverDevice, RemoteDevice remoteDevice) {
    }

    public void errorOnConnection(IOException e) {
    }
}
