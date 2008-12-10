/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpdme.forms;

import com.sun.lwuit.Container;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import mpdme.BtServer;
import mpdme.MpdException;
import mpdme.components.MenuButton;
import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.entity.ClientDevice;
import net.java.dev.marge.entity.config.ClientConfiguration;
import net.java.dev.marge.factory.CommunicationFactory;
import net.java.dev.marge.factory.L2CAPCommunicationFactory;
import net.java.dev.marge.factory.RFCOMMCommunicationFactory;

public class PlayerForm extends MpdForm implements CommunicationListener {
    private ClientDevice device;

    private class PlayButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            device.send(new String("play").getBytes());
        }
    }

    private class StopButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            device.send(new String("stop").getBytes());
        }
    }

    public PlayerForm(MpdForm parent) {
        super(parent);
    }

    public void initialize() {
        Resources resources = this.getResources();

        this.setTitle("mpd Player Control");
        this.setLayout(new BorderLayout());

        Container buttonContainer = new Container(new GridLayout(1, 2));
        MenuButton button;

        button = new MenuButton("Play", resources.getImage("play_btn"), resources.getImage("play_btn_sel"));
        button.addActionListener(new PlayButtonListener());
        buttonContainer.addComponent(button);

        button = new MenuButton("Stop", resources.getImage("stop"), resources.getImage("stop_sel"));
        button.addActionListener(new StopButtonListener());
        buttonContainer.addComponent(button);

        this.addComponent(BorderLayout.SOUTH, buttonContainer);

        ClientConfiguration clientConfig = new ClientConfiguration(BtServer.getInstance().getService(), this);
        CommunicationFactory factory = new RFCOMMCommunicationFactory();

        try {
            this.device = factory.connectToServer(clientConfig);
        } catch (IOException ex) {
            throw new MpdException(ex);
        }
    }

    public void receiveMessage(byte[] message) {
        System.out.println(new String(message));
    }

    public void errorOnReceiving(IOException e) {
        throw new MpdException(e);
    }

    public void errorOnSending(IOException e) {
        throw new MpdException(e);
    }
}
