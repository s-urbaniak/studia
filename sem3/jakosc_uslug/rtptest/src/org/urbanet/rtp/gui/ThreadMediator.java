package org.urbanet.rtp.gui;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.urbanet.rtp.protocol.beans.RtpPacket;
import org.urbanet.rtp.protocol.beans.RtpStatistics;
import org.urbanet.rtp.thread.PlayerThread;
import org.urbanet.rtp.thread.StatisticsThread;

public class ThreadMediator {
    private final static Logger log = Logger.getLogger(ThreadMediator.class
            .getName());

    private ExecutorService executor;

    private PlayerThread player;

    private StatisticsThread statsThread;

    private MainWindow window;

    public ThreadMediator() {
        this.executor = Executors.newFixedThreadPool(2);
    }

    public void setWindow(MainWindow window) {
        this.window = window;
    }

    public void updateStatistics(final RtpStatistics stats) {
        window.getShell().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                window.updateStatistics(stats);
            }
        });
    }

    public void startStatistics(ArrayBlockingQueue<RtpPacket> queue) {
        statsThread = new StatisticsThread();
        statsThread.setQueue(queue);
        statsThread.setMediator(this);
        executor.execute(statsThread);
    }

    public synchronized void start(String host, int port, String path) {
        if (player != null)
            return;

        ArrayBlockingQueue<RtpPacket> queue = new ArrayBlockingQueue<RtpPacket>(
                8192);

        player = new PlayerThread(host, port, path, queue);
        player.setMediator(this);
        executor.execute(player);
    }

    public synchronized void stop() throws IOException {
        if (player == null)
            return;

        try {
            player.stop();
        } finally {
            player = null;

            try {
                statsThread.stop();
            } finally {
                statsThread = null;
            }
        }
    }

    public synchronized void exception(Exception e) {
        try {
            handleException(e);
            this.stop();
        } catch (IOException e1) {
            handleException(e1);
        }
    }

    private void handleException(Exception e) {
        log.log(Level.SEVERE, "An exception occured", e);

        final String message = e.getClass().getName() + ": "
                + e.getLocalizedMessage();

        window.getShell().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                MessageBox errorBox = new MessageBox(window.getShell(),
                        SWT.ICON_ERROR | SWT.OK);
                errorBox.setMessage(message);
                errorBox.open();
            }
        });
    }

    public synchronized void shutdown() throws IOException {
        if (player != null)
            this.stop();

        executor.shutdown();
    }
}