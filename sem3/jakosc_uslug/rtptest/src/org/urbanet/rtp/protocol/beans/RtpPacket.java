package org.urbanet.rtp.protocol.beans;

import java.util.Arrays;

public class RtpPacket {

    // used to identify separate streams that may contribute to this packet
    private long SSRC;

    // incrementing identifier for each packet that is sent
    private long sequenceNumber;

    // used to place this packet in the correct timing order
    // that is, where this packet fits in time based media
    private long timeStamp;

    private long arrivalTimeStamp;

    // the type of the media data, or the payload type
    private long payloadType;

    // the actual media data, also called the payload
    private byte data[];

    public long getArrivalTimeStamp() {
        return arrivalTimeStamp;
    }

    public void setArrivalTimeStamp(long arrivalTimeStamp) {
        this.arrivalTimeStamp = arrivalTimeStamp;
    }

    // the get and set methods
    public long getSSRC() {
        return this.SSRC;
    }

    public void setSSRC(long SSRC) {
        this.SSRC = SSRC;
    }

    public long getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getPayloadType() {
        return this.payloadType;
    }

    public void setPayloadType(long payloadType) {
        this.payloadType = payloadType;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RtpPacket [SSRC=" + SSRC + ", arrivalTimeStamp="
                + arrivalTimeStamp + ", data=" + Arrays.toString(data)
                + ", payloadType=" + payloadType + ", sequenceNumber="
                + sequenceNumber + ", timeStamp=" + timeStamp + "]";
    }
}