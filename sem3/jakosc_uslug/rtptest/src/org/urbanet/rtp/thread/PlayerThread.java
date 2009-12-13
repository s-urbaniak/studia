package org.urbanet.rtp.thread;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import javazoom.jl.player.Player;

import org.urbanet.rtp.gui.ThreadMediator;
import org.urbanet.rtp.protocol.RtpMp3InputStream;
import org.urbanet.rtp.protocol.RtpPacketAsyncFilter;
import org.urbanet.rtp.protocol.RtpPacketInputStream;
import org.urbanet.rtp.protocol.RtpRawInputStream;
import org.urbanet.rtp.protocol.RtpSource;
import org.urbanet.rtp.protocol.RtspSource;
import org.urbanet.rtp.protocol.beans.RtpPacket;

public class PlayerThread implements Runnable {
    private String host;

    private int port;

    private String path;

    private Player mp3Player;

    private RtspSource rtspSource;

    private ThreadMediator mediator;

    private ArrayBlockingQueue<RtpPacket> queue = null;

    public void setMediator(ThreadMediator mediator) {
        this.mediator = mediator;
    }

    public PlayerThread(String host, int port, String path,
            ArrayBlockingQueue<RtpPacket> queue) {
        this.host = host;
        this.port = port;
        this.path = path;
        this.queue = queue;
    }

    public void stop() throws IOException {
        this.mp3Player.close();
    }

    @Override
    public void run() {
        try {
            RtpSource rtpSource = new RtpSource();
            this.rtspSource = new RtspSource(host, port, path, rtpSource
                    .getLocalUdpPort());

            RtpPacketInputStream packetStream = new RtpPacketInputStream(
                    rtpSource);
            RtpPacketAsyncFilter packetFilter = new RtpPacketAsyncFilter(
                    packetStream, queue);
            RtpRawInputStream rtpRawStream = new RtpRawInputStream(packetFilter);
            RtpMp3InputStream rtpMp3Stream = new RtpMp3InputStream(rtpRawStream);
            BufferedInputStream audioStream = new BufferedInputStream(
                    rtpMp3Stream);

            this.rtspSource.start();
            rtpSource.setRtspSession(this.rtspSource.getSession());

            this.mediator.startStatistics(queue);
            this.mp3Player = new Player(audioStream);
            this.mp3Player.play();

            this.rtspSource.doTeardown();
        } catch (Exception e) {
            mediator.exception(e);
        }
    }
}