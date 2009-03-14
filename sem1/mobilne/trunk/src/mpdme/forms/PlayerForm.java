/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpdme.forms;

import com.sun.lwuit.Container;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.util.Resources;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import mpdme.bluetooth.BtServer;
import mpdme.MpdException;
import mpdme.bluetooth.MpdStatus;
import mpdme.components.MenuButton;
import net.java.dev.marge.communication.CommunicationListener;
import net.java.dev.marge.entity.Device;

public class PlayerForm extends MpdForm implements CommunicationListener, ActionListener {

    private Label labelArtist;
    private Label labelAlbum;
    private Label labelTitle;
    private Label labelTime;
    private Image pause, pause_sel;
    private Image play, play_sel;
    private Container buttonContainer;
    private static final int PLAY_BUTTON = 1;
    private static final int STOP_BUTTON = 2;
    private static final int PAUSE_BUTTON = 3;
    private static final int PREV_BUTTON = 4;
    private static final int NEXT_BUTTON = 5;
    private int lastAction;
    private UpdateTimerTask updateTimer;

    public void quit() {
        this.updateTimer.cancel();
    }

    private class UpdateTimerTask extends TimerTask {

        public void run() {
            BtServer.getInstance().getServer().send(new String("status").getBytes());
        }
    }

    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);

        try {
            MenuButton button = (MenuButton) evt.getSource();
            Device device = BtServer.getInstance().getServer();

            this.lastAction = button.getId();

            switch (button.getId()) {
                case PLAY_BUTTON:
                    device.send(new String("play").getBytes());
                    break;
                case STOP_BUTTON:
                    device.send(new String("stop").getBytes());
                    break;
                case PAUSE_BUTTON:
                    device.send(new String("pause").getBytes());
                    break;
                case NEXT_BUTTON:
                    device.send(new String("next").getBytes());
                    break;
                case PREV_BUTTON:
                    device.send(new String("prev").getBytes());
                    break;
            }
        } catch (ClassCastException e) {
            return;
        }
    }

    public PlayerForm(MpdForm parent) {
        super(parent);
    }

    public void initialize() {
        Resources resources = this.getResources();

        this.setTitle("mpd Player Control");
        this.setLayout(new BorderLayout());

        BtServer.getInstance().getServer().setCommunicationListener(this);
        BtServer.getInstance().getServer().startListening();

        Container container = new Container(new GridLayout(1, 4));
        this.addComponent(BorderLayout.SOUTH, container);

        MenuButton button;

        button = new MenuButton("Previous", resources.getImage("prev"), resources.getImage("prev_sel"));
        button.setId(PREV_BUTTON);
        button.addActionListener(this);
        container.addComponent(button);

        this.play = resources.getImage("play_btn");
        this.play_sel = resources.getImage("play_btn_sel");
        this.pause = resources.getImage("pause");
        this.pause_sel = resources.getImage("pause_sel");

        button = new MenuButton("Play", this.play, this.play_sel);
        button.setId(PLAY_BUTTON);
        button.addActionListener(this);
        container.addComponent(button);

        button = new MenuButton("Stop", resources.getImage("stop"), resources.getImage("stop_sel"));
        button.setId(STOP_BUTTON);
        button.addActionListener(this);
        container.addComponent(button);

        button = new MenuButton("Next", resources.getImage("next"), resources.getImage("next_sel"));
        button.setId(NEXT_BUTTON);
        button.addActionListener(this);
        container.addComponent(button);
        buttonContainer = container;

        container = new Container(new BoxLayout(BoxLayout.Y_AXIS));

        this.labelArtist = new Label();
        this.labelArtist.setText(" ");
        this.labelArtist.setAlignment(Label.CENTER);
        container.addComponent(this.labelArtist);

        this.labelTitle = new Label();
        this.labelTitle.setText(" ");
        this.labelTitle.setAlignment(Label.CENTER);
        container.addComponent(this.labelTitle);

        this.labelAlbum = new Label();
        this.labelAlbum.setText(" ");
        this.labelAlbum.setAlignment(Label.CENTER);
        container.addComponent(this.labelAlbum);

        this.labelTime = new Label();
        this.labelTime.setText(" ");
        this.labelTime.setAlignment(Label.CENTER);
        container.addComponent(this.labelTime);

        this.addComponent(BorderLayout.CENTER, container);

        Timer timer = new Timer();
        this.updateTimer = new UpdateTimerTask();
        timer.schedule(this.updateTimer, 1, 1000);
    }

    public void receiveMessage(byte[] message) {
        MpdStatus status = new MpdStatus();
        status.decode(message);
        if ("".equals(status.getTitle())) {
            this.labelTitle.setText(status.getName());
        } else {
            this.labelTitle.setText(status.getTitle());
        }

        MenuButton currentButton = (MenuButton) this.buttonContainer.getComponentAt(1);
        if ("stop".equals(status.getState())) {
            if (currentButton.getId() != PLAY_BUTTON)
                currentButton.replaceWith("Play", play, play_sel, PLAY_BUTTON);
        } else if ("pause".equals(status.getState())) {
            if (currentButton.getId() != PLAY_BUTTON)
                currentButton.replaceWith("Play", play, play_sel, PLAY_BUTTON);
        } else if ("play".equals(status.getState())) {
            if (currentButton.getId() != PAUSE_BUTTON)
                currentButton.replaceWith("Pause", pause, pause_sel, PAUSE_BUTTON);
        }

        this.labelAlbum.setText(status.getAlbum());
        this.labelArtist.setText(status.getArtist());

        int mm_time = (int)(status.getTime()) / 60;
        int ss_time = (int)(status.getTime()) % 60;
        int mm_length = (int)(status.getLength()) / 60;
        int ss_length = (int)(status.getLength()) % 60;

        this.labelTime.setText(
                mm_time + ":" + ss_time
                + " / "
                + mm_length + ":" + ss_length);
    }

    public void errorOnReceiving(IOException e) {
        System.out.println("An error occured!" + e.getMessage());
        throw new MpdException(e);
    }

    public void errorOnSending(IOException e) {
        System.out.println("An error occured!" + e.getMessage());
        throw new MpdException(e);
    }
}
