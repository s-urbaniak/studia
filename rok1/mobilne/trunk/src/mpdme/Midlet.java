package mpdme;

import javax.microedition.midlet.*;

import com.sun.lwuit.*;
import com.sun.lwuit.events.*;
import mpdme.forms.MainMenu;

public class Midlet extends MIDlet implements ActionListener {

    public void startApp() {
        Display.init(this);

        try {
            ThemeManager manager = new ThemeManager();
            manager.loadTheme();

            MainMenu mainForm = new MainMenu();
            mainForm.show();

            Command exitCommand = new Command("Exit");
            mainForm.addCommand(exitCommand);
            mainForm.setCommandListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            Dialog.show("Error", "Sorry, some error occured: " + ex.getMessage(), "OK", null);
            notifyDestroyed();
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
