package org.urbanet.rtp.thread;

import org.eclipse.swt.widgets.Display;

public class ExceptionListenerThread implements Runnable {
    private Display ui;

    public ExceptionListenerThread(Display ui) {
        this.ui = ui;
    }

    @Override
    public void run() {
        System.out.println(ui.getActiveShell().getChildren());
    }
}