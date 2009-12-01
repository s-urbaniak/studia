package org.urbanet.rtp;

import java.io.InputStream;
import java.util.logging.LogManager;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.urbanet.rtp.gui.MainWindow;

public class Main {
    public static void main(String[] args) {
        try {
            InputStream is = Main.class
                    .getResourceAsStream("/logging.properties");

            LogManager.getLogManager().readConfiguration(is);

            Shell shell = new Shell();
            MainWindow awin = new MainWindow(shell);
            awin.setBlockOnOpen(true);
            awin.open();
            Display.getCurrent().dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}