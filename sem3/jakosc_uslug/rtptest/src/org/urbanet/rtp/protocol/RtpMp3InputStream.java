package org.urbanet.rtp.protocol;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RtpMp3InputStream extends FilterInputStream {

    private static final int MPA_HEADER_LENGTH = 4;

    public RtpMp3InputStream(InputStream in) {
        super(in);
    }

    public int read(byte data[], int off, int n) throws IOException {
        int length = this.in.read(data, off, n);

        if (length < MPA_HEADER_LENGTH)
            return 0;

        System.arraycopy(data, MPA_HEADER_LENGTH, data, 0, length
                - MPA_HEADER_LENGTH);

        // and return its length
        length = length - MPA_HEADER_LENGTH;

        if (length > n)
            return n;

        return length;
    }

    public int read() throws IOException {
        throw new UnsupportedOperationException();
    }
}