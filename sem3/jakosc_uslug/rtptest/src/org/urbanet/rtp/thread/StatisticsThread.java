package org.urbanet.rtp.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.urbanet.rtp.gui.ThreadMediator;
import org.urbanet.rtp.protocol.beans.RtpPacket;
import org.urbanet.rtp.protocol.beans.RtpStatistics;

/**
 * The statistics thread is the number crunching work horse of the application.
 * It updates data in a statistics bean (see {@link RtpStatistics}) after every
 * packet arrival and communicates statistics data to the mediator.
 * 
 * Packet data is received using a blocking queue. Note that receiving a packet
 * for this thread is a blocking operation (see
 * {@link ArrayBlockingQueue#poll(long, TimeUnit)}). The receive timeout is
 * specified using the constant {@link StatisticsThread#RECEIVE_TIMEOUT_MSEC}.
 * 
 * Statistics data is being propagated to the mediator after the update interval
 * {@link StatisticsThread#UPDATE_INTERVAL_MSEC}.
 * 
 * @author sur
 */
public class StatisticsThread implements Runnable {
    /**
     * The update interval after which statistics data is being propagated to
     * the mediator.
     */
    public static final int UPDATE_INTERVAL_MSEC = 200;

    public static final int RECEIVE_TIMEOUT_MSEC = 100;

    private ArrayBlockingQueue<RtpPacket> queue = null;

    private boolean stop;

    private ThreadMediator mediator;

    private final static Logger log = Logger.getLogger(StatisticsThread.class
            .getName());

    /**
     * Call this method when the statistics thread is supposed to stop after the
     * next packet arrival.
     */
    public void stop() {
        this.stop = true;
    }

    /**
     * Sets the current packet queue.
     * 
     * @param queue
     *            The queue being listened on
     */
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
        initStatistics(stats);

        while (!stop) {
            try {
                RtpPacket packet = queue.poll(RECEIVE_TIMEOUT_MSEC,
                        TimeUnit.MILLISECONDS);

                stats.setCurrentPacket(packet);

                if (packet == null) {
                    if (stop)
                        return;
                } else {
                    // only calculate jitter if at least one packet arrived
                    if (stats.isFirstPacketArrived()) {
                        calculateJitter(stats, packet);
                        calculateLostPackets(stats, packet);
                    }

                    stats.setFirstPacketArrived(true);

                    calculateThroughput(stats);
                    updateStats(stats);

                    stats.setPreviousSequenceNumber(packet.getSequenceNumber());
                    stats.setPreviousArrivalTimeStamp(packet
                            .getArrivalTimeStamp());
                }
            } catch (Exception e) {
                mediator.exception(e);
            }
        }
    }

    /**
     * Update statistics approximately every 200msecs by calling
     * {@link ThreadMediator#updateStatistics(RtpStatistics)}.
     * 
     * @param stats
     *            The statistics bean.
     * @return
     */
    private void updateStats(RtpStatistics stats) {
        long timeDiff = stats.getCurrentPacket().getArrivalTimeStamp()
                - stats.getPreviousArrivalTimeStamp();

        // communicate stats every 200msec
        if (timeDiff > UPDATE_INTERVAL_MSEC) {
            mediator.updateStatistics(stats);
            log.fine(stats.toString());
        }
    }

    /**
     * Updates the throughput in kbits/second in the {@link RtpStatistics} bean.
     * 
     * @param stats
     *            The statistics bean being updated (see
     *            {@link RtpStatistics#setThroughput(double)})
     * @param timeDiff
     *            Time period in msec after the last update
     * @param len
     *            Length in bits of data which were received
     */
    private void calculateThroughput(RtpStatistics stats) {
        int len = stats.getCurrentPacket().getData().length;
        long timeDiff = stats.getCurrentPacket().getArrivalTimeStamp()
                - stats.getPreviousArrivalTimeStamp();

        double lenBits = (double) len * 8 / 1000;
        double timeDiffSecs = (double) timeDiff / 1000;

        stats.setThroughput(lenBits / timeDiffSecs);
    }

    /**
     * Initializes the statistics bean when the first data packet arrives
     * 
     * @param stats
     *            Initializes
     *            {@link RtpStatistics#setFirstPacketArrived(boolean)} and
     *            {@link RtpStatistics#setJitter(double)}
     */
    private void initStatistics(RtpStatistics stats) {
        stats.setFirstPacketArrived(false);
        stats.setJitter(0);
        stats.setPacketsLost(0);
    }

    /**
     * Calculates the amount of lost data packets.
     * 
     * TODO I think there is a bug here. Currently only the amount of loss
     * coincidences is being count but not the actual number of lost packets.
     * 
     * @param stats
     *            The statistics bean, updates
     *            {@link RtpStatistics#setPacketsLost(long)}
     * @param packet
     *            The current data packet
     */
    private void calculateLostPackets(RtpStatistics stats, RtpPacket packet) {
        if (packet.getSequenceNumber() > stats.getPreviousSequenceNumber() + 1)
            stats.setPacketsLost(stats.getPacketsLost() + 1);
    }

    /**
     * Calculates the jitter and updates statistics using
     * {@link RtpStatistics#setJitter(double)}. See Appendix A.8 of rfc3550 for
     * details.
     * 
     * @param stats
     *            The statistics bean, updates
     *            {@link RtpStatistics#setJitter(double)} and
     *            {@link RtpStatistics#setPreviousTransit(long)}.
     * @param packet
     *            The current data packet
     */
    private void calculateJitter(RtpStatistics stats, RtpPacket packet) {
        long timestamp = packet.getTimeStamp();
        long arrival = packet.getArrivalTimeStamp();
        long transit = arrival - timestamp;

        double d = Math.abs(transit - stats.getPreviousTransit());
        stats.setJitter(stats.getJitter() + (d - stats.getJitter()) / 16.0d);

        stats.setPreviousTransit(transit);
    }
}