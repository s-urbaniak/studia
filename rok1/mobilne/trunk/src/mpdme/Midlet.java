package mpdme;

import mpdme.bluetooth.BtServer;
import java.io.IOException;
import javax.microedition.midlet.*;

import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import mpdme.forms.InquiryForm;
import mpdme.forms.MpdForm;

public class Midlet extends MIDlet implements ActionListener {

    public void startApp() {
        MpdException.midlet = this;
        MpdForm.setMidlet(this);

        Display.init(this);

        ThemeManager manager = new ThemeManager();
        try {
            manager.loadTheme();
        } catch (IOException ex) {
            throw new MpdException(ex);
        }

        InquiryForm mainForm = new InquiryForm();
        mainForm.show();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        BtServer.getInstance().disconnect();
    }

    public void actionPerformed(ActionEvent evt) {
    }
}
