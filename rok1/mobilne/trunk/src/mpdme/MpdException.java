package mpdme;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;

public class MpdException extends RuntimeException {

    public static Midlet midlet = null;

    private void showAlert(String message) {
        Display display = Display.getDisplay(midlet);
        Alert alert = new Alert("Error", message, null, AlertType.ERROR);
        display.setCurrent(alert);

        //midlet.notifyDestroyed();
    }
    
    public MpdException(String message) {
        System.err.println(message);
        showAlert(message);
    }

    public MpdException(Exception e) {
        System.err.println(e.getMessage());
        showAlert(e.getMessage());
    }
}
