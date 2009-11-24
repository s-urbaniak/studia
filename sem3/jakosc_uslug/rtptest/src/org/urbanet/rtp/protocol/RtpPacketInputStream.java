package org.urbanet.rtp.protocol;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.urbanet.rtp.protocol.beans.RtpPacket;

public class RtpPacketInputStream extends ObjectInputStream {

    private RtpSource rtpSource;

    private long previousSeqNo;

    private boolean firstPacketArrived = false;

    public RtpPacketInputStream(RtpSource rtpSource) throws IOException {
        this.rtpSource = rtpSource;
    }

    @Override
    public void close() throws IOException {
        this.rtpSource.close();
    }

    protected final Object readObjectOverride() throws IOException,
            ClassNotFoundException {
        RtpPacket rtpPacket = rtpSource.read();

        if (rtpPacket == null)
            return null;

        if (firstPacketArrived)
            if (previousSeqNo > rtpPacket.getSequenceNumber()) {
                throw new IOException("Sequence number mismatch (previous="
                        + previousSeqNo + ", current="
                        + rtpPacket.getSequenceNumber() + ")");
            } else if (previousSeqNo == rtpPacket.getSequenceNumber()) {
                // Double packets, ignoring current one
                return readObject();
            } else {
                firstPacketArrived = true;
            }

        previousSeqNo = rtpPacket.getSequenceNumber();

        return rtpPacket;
    }
}