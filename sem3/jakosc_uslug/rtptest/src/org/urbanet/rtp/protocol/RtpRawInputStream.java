package org.urbanet.rtp.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.urbanet.rtp.protocol.beans.RtpPacket;

public class RtpRawInputStream extends InputStream {

    private ObjectInputStream rtpSource;

    public RtpRawInputStream(ObjectInputStream rtpSource) {
        this.rtpSource = rtpSource;
    }

    @Override
    public void close() throws IOException {
        this.rtpSource.close();
    }

    public int read(byte data[], int off, int n) throws IOException {
        RtpPacket rtpPacket;
        try {
            rtpPacket = (RtpPacket) rtpSource.readObject();

            if (rtpPacket == null)
                return 0;
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }

        if (rtpPacket.getData().length > n)
            throw new ArrayIndexOutOfBoundsException(rtpPacket.getData().length
                    + " bytes read / " + n + " bytes available in buffer");
        else
            n = rtpPacket.getData().length;

        System.arraycopy(rtpPacket.getData(), 0, data, 0, n);

        // and return its length
        return n;
    }

    public int read() throws IOException {
        throw new UnsupportedOperationException();
    }
}