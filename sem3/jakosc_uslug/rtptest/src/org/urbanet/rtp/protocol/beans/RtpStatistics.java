package org.urbanet.rtp.protocol.beans;

public class RtpStatistics {
    private long prevTransit;

    private double jitter;

    private boolean firstPacketArrived = false;

    private long lastSequenceNumber;

    private long packetsLost;

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    private double throughput;

    public long getPrevTransit() {
        return prevTransit;
    }

    public void setPrevTransit(long prevTransit) {
        this.prevTransit = prevTransit;
    }

    public double getJitter() {
        return jitter;
    }

    public void setJitter(double jitter) {
        this.jitter = jitter;
    }

    public boolean isFirstPacketArrived() {
        return firstPacketArrived;
    }

    public void setFirstPacketArrived(boolean firstPacketArrived) {
        this.firstPacketArrived = firstPacketArrived;
    }

    public long getLastSequenceNumber() {
        return lastSequenceNumber;
    }

    public void setLastSequenceNumber(long lastSequenceNumber) {
        this.lastSequenceNumber = lastSequenceNumber;
    }

    public long getPacketsLost() {
        return packetsLost;
    }

    public void setPacketsLost(long packetsLost) {
        this.packetsLost = packetsLost;
    }

    @Override
    public String toString() {
        return "RtpStatistics [firstPacketArrived=" + firstPacketArrived
                + ", jitter=" + jitter + ", lastSequenceNumber="
                + lastSequenceNumber + ", packetsLost=" + packetsLost
                + ", prevTransit=" + prevTransit + ", throughput=" + throughput
                + "]";
    }

}
