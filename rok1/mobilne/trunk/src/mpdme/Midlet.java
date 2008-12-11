package mpdme;

import java.io.IOException;
import javax.microedition.midlet.*;

import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import mpdme.forms.InquiryForm;

public class Midlet extends MIDlet implements ActionListener {

    public void startApp() {
        MpdException.midlet = this;

        Display.init(this);

        ThemeManager manager = new ThemeManager();
        try {
            manager.loadTheme();
        } catch (IOException ex) {
            throw new MpdException(ex);
        }

        InquiryForm mainForm = new InquiryForm();
        mainForm.show();

        Command exitCommand = new Command("Exit");
        mainForm.addCommand(exitCommand);
        mainForm.setCommandListener(this);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void actionPerformed(ActionEvent ae) {
        notifyDestroyed();
    }
}
