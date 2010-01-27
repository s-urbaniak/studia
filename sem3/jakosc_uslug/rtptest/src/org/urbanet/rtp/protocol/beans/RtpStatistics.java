package org.urbanet.rtp.protocol.beans;

public class RtpStatistics {
    private long previousTransit;

    private double jitter;

    private boolean firstPacketArrived = false;

    private long previousSequenceNumber;

    private long previousArrivalTimeStamp;

    private long previousUpdateTimeStamp;

    private long packetsLost;

    private RtpPacket currentPacket;

    private double throughput;

    public long getPreviousUpdateTimeStamp() {
        return previousUpdateTimeStamp;
    }

    public void setPreviousUpdateTimeStamp(long previousUpdateTimeStamp) {
        this.previousUpdateTimeStamp = previousUpdateTimeStamp;
    }

    public long getPreviousArrivalTimeStamp() {
        return previousArrivalTimeStamp;
    }

    public void setPreviousArrivalTimeStamp(long previousArrivalTimeStamp) {
        this.previousArrivalTimeStamp = previousArrivalTimeStamp;
    }

    public RtpPacket getCurrentPacket() {
        return currentPacket;
    }

    public void setCurrentPacket(RtpPacket currentPacket) {
        this.currentPacket = currentPacket;
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public long getPreviousTransit() {
        return previousTransit;
    }

    public void setPreviousTransit(long prevTransit) {
        this.previousTransit = prevTransit;
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

    public long getPreviousSequenceNumber() {
        return previousSequenceNumber;
    }

    public void setPreviousSequenceNumber(long lastSequenceNumber) {
        this.previousSequenceNumber = lastSequenceNumber;
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
                + previousSequenceNumber + ", packetsLost=" + packetsLost
                + ", prevTransit=" + previousTransit + ", throughput="
                + throughput + "]";
    }
}
