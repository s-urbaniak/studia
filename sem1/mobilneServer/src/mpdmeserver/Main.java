package mpdmeserver;

import mpdmeserver.mpd.MpdConnector;
import mpdmeserver.bluetooth.BtHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.dev.marge.communication.CommunicationListener;

/**
 *
 * @author sur
 */
public class Main implements CommunicationListener {

    private MpdConnector mpd = null;
    private BtHandler bt = null;

    public void runServer() {
        Logger.getLogger(this.getClass().getName()).log(
                Level.INFO,
                "Connecting to mpd");

        this.mpd = new MpdConnector();
        this.mpd.connect();

        Logger.getLogger(this.getClass().getName()).log(
                Level.INFO,
                "Starting bluetooth server");

        this.bt = new BtHandler();
        this.bt.startServer(this);
    }

    public static void main(String[] args) {
        Main instance = new Main();
        instance.runServer();
    }

    public void receiveMessage(byte[] message) {
        String receivedString = new String(message);

        Logger.getLogger(this.getClass().getName()).log(
                Level.INFO,
                "Command received: " + receivedString);

        try {
            if ("play".equals(receivedString)) {
                this.mpd.play();
            }

            if ("pause".equals(receivedString)) {
                this.mpd.pause();
            }

            if ("stop".equals(receivedString)) {
                this.mpd.stop();
            }

            if ("next".equals(receivedString)) {
                this.mpd.next();
            }

            if ("prev".equals(receivedString)) {
                this.mpd.prev();
            }

            if ("status".equals(receivedString)) {
                this.bt.send(this.mpd.status());
            }

            if ("quit".equals(receivedString)) {
                this.bt.quitServer();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void errorOnReceiving(IOException e) {
        throw new RuntimeException(e);
    }

    public void errorOnSending(IOException e) {
        throw new RuntimeException(e);
    }
}
