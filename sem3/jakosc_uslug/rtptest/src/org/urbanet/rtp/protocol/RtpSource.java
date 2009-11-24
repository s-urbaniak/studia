package org.urbanet.rtp.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.urbanet.rtp.protocol.beans.RtpPacket;

public class RtpSource {

    private DatagramSocket socket;

    public RtpSource() throws IOException {
        socket = new DatagramSocket();
        socket.setSoTimeout(10000);
    }

    public RtpSource(int localUdpPort) throws IOException {
        socket = new DatagramSocket(localUdpPort);
        socket.setSoTimeout(10000);
    }

    public int getLocalUdpPort() {
        return this.socket.getLocalPort();
    }

    public void close() {
        this.socket.close();
        this.socket = null;
    }

    /**
     * Reads an RTP packet from the datagram socket. Note this is a blocking
     * operation !
     * 
     * @return The received RTP packet
     * @throws IOException
     */
    public RtpPacket read() throws IOException {
        // create a byte array which will be used to read the datagram
        byte[] fullPkt = new byte[2048];

        DatagramPacket packet = new DatagramPacket(fullPkt, fullPkt.length);

        socket.receive(packet);
        long arrival = System.currentTimeMillis();

        if (packet != null) {
            RtpPacket rtpPacket = getRTPPacket(packet, arrival);
            return rtpPacket;
        } else
            return null;
    }

    // extracts the RTP packet from each datagram packet received
    private RtpPacket getRTPPacket(DatagramPacket packet, long arrival) {
        // SSRC
        long SSRC = 0;

        // the payload type
        byte PT = 0;

        // the time stamp
        int timeStamp = 0;

        // the sequence number of this packet
        short seqNo = 0;

        byte[] buf = packet.getData();
        // see http://www.networksorcery.com/enp/protocol/rtp.htm
        // for detailed description of the packet and its data
        PT = (byte) ((buf[1] & 0xff) & 0x7f);

        seqNo = (short) ((buf[2] << 8) | (buf[3] & 0xff));

        timeStamp = (((buf[4] & 0xff) << 24) | ((buf[5] & 0xff) << 16)
                | ((buf[6] & 0xff) << 8) | (buf[7] & 0xff));

        SSRC = (((buf[8] & 0xff) << 24) | ((buf[9] & 0xff) << 16)
                | ((buf[10] & 0xff) << 8) | (buf[11] & 0xff));

        // create an RTPPacket based on these values
        RtpPacket rtpPkt = new RtpPacket();

        // the sequence number
        rtpPkt.setSequenceNumber(seqNo);

        // the timestamp
        rtpPkt.setTimeStamp(timeStamp);
        rtpPkt.setArrivalTimeStamp(arrival * 90);

        // the SSRC
        rtpPkt.setSSRC(SSRC);

        // the payload type
        rtpPkt.setPayloadType(PT);

        // the actual payload (the media data) is after the 12 byte header
        // which is constant
        byte payload[] = new byte[packet.getLength() - 12];

        // for (int i = 0; i < payload.length; i++)
        // payload[i] = buf[i + 12];
        System.arraycopy(buf, 12, payload, 0, payload.length);

        // set the payload on the RTP Packet
        rtpPkt.setData(payload);

        // and return the payload
        return rtpPkt;

    }
}