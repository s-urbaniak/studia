package mpdme;

import javax.microedition.midlet.*;

import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import mpdme.forms.InquiryForm;

public class Midlet extends MIDlet implements ActionListener {

    public void startApp() {
        MpdException.midlet = this;

        Display.init(this);

        try {
            ThemeManager manager = new ThemeManager();
            manager.loadTheme();

            InquiryForm mainForm = new InquiryForm();
            mainForm.show();

            Command exitCommand = new Command("Exit");
            mainForm.addCommand(exitCommand);
            mainForm.setCommandListener(this);
        } catch (Exception ex) {
            throw new MpdException(ex);
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void actionPerformed(ActionEvent ae) {
        notifyDestroyed();
    }
}
