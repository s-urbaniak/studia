package org.urbanet.rtp.protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import org.urbanet.rtp.protocol.beans.RtpPacket;

public class RtpPacketAsyncFilter extends ObjectInputStream {

    private ObjectInputStream rtpSource;

    private ArrayBlockingQueue<RtpPacket> queue = null;

    private final static Logger log = Logger
            .getLogger(RtpPacketAsyncFilter.class.getName());

    public RtpPacketAsyncFilter(ObjectInputStream rtpSource,
            ArrayBlockingQueue<RtpPacket> queue) throws IOException {
        this.rtpSource = rtpSource;
        this.queue = queue;
    }

    @Override
    public void close() throws IOException {
        this.rtpSource.close();
    }

    protected final Object readObjectOverride() throws IOException,
            ClassNotFoundException {
        RtpPacket rtpPacket = (RtpPacket) rtpSource.readObject();

        if (rtpPacket == null)
            return null;

        boolean result = queue.offer(rtpPacket);

        if (!result)
            log.warning("message queue (" + queue + ") is full ...");

        return rtpPacket;
    }
}