package org.urbanet.rtp.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.urbanet.rtp.gui.ThreadMediator;
import org.urbanet.rtp.protocol.beans.RtpPacket;
import org.urbanet.rtp.protocol.beans.RtpStatistics;

public class StatisticsThread implements Runnable {
    private ArrayBlockingQueue<RtpPacket> queue = null;

    private boolean stop;

    private ThreadMediator mediator;

    private final static Logger log = Logger.getLogger(StatisticsThread.class
            .getName());

    public void stop() {
        this.stop = true;
    }

    public void setQueue(ArrayBlockingQueue<RtpPacket> queue) {
        this.queue = queue;
    }

    public void setMediator(ThreadMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public void run() {
        this.stop = false;
        RtpStatistics stats = new RtpStatistics();
        stats.setFirstPacketArrived(false);
        stats.setPacketsLost(0);

        long startTime = System.currentTimeMillis();
        while (!stop) {
            try {
                RtpPacket packet = queue.poll(100, TimeUnit.MILLISECONDS);

                if (packet == null) {
                    if (stop)
                        return;
                } else {
                    long timestamp = packet.getTimeStamp();
                    long arrival = packet.getArrivalTimeStamp();
                    long transit = arrival - timestamp;

                    if (stats.isFirstPacketArrived()) {
                        double d = Math.abs(transit - stats.getPrevTransit());
                        stats.setJitter(stats.getJitter()
                                + (d - stats.getJitter()) / 16.0d);

                        if (packet.getSequenceNumber() > stats
                                .getLastSequenceNumber() + 1)
                            stats.setPacketsLost(stats.getPacketsLost() + 1);
                    } else {
                        stats.setFirstPacketArrived(true);
                        stats.setJitter(0);
                    }

                    stats.setLastSequenceNumber(packet.getSequenceNumber());
                    stats.setPrevTransit(transit);

                    long timeDiff = System.currentTimeMillis() - startTime;

                    int len = packet.getData().length;

                    // kiloBits per second
                    double lenBits = (double) len * 8 / 1000;
                    double timeDiffSecs = (double) timeDiff / 1000;
                    stats.setThroughput(lenBits / timeDiffSecs);

                    // communicate stats every 200msec
                    if (timeDiff > 200) {
                        startTime = System.currentTimeMillis();
                        mediator.updateStatistics(stats);
                        log.fine(stats.toString());
                    }
                }
            } catch (Exception e) {
                mediator.exception(e);
            }
        }
    }
}