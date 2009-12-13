package org.urbanet.rtp.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.urbanet.rtp.protocol.beans.RtspPacket;
import org.urbanet.rtp.protocol.beans.RtspSession;

public class RtspSource {

    private final static Logger log = Logger.getLogger(RtspSource.class
            .getName());

    // the inputstream to receive response from the server
    private InputStream socketInputStream;

    // the outputstream to write to the server
    private OutputStream socketOutputStream;

    // the incrementing sequence number for each request
    // sent by the client
    private static int CSeq = 1;

    // flags to indicate the status of a session
    private boolean options, described, setup, playing;

    private RtspSession session;

    private boolean stopped = false;

    // constants
    private static final String SESSION = "session";
    private static final String CRLF = "\r\n";
    private static final String VERSION = "RTSP/1.0";
    private static final String TRACK_LINE = "trackID=";
    private static final String TRANSPORT_DATA = "TRANSPORT: RTP/AVP;unicast;client_port=";
    private static final String RTSP_OK = VERSION + " 200 OK";

    public RtspSource(String host, int port, String stream, int udpPort) {
        this.session = new RtspSession();

        this.session.setHost(host);
        this.session.setPort(port);
        this.session.setClientUdpPort(udpPort);
        this.session.setStream(stream);
    }

    public RtspSession getSession() {
        return session;
    }

    protected Socket connect() throws IOException {
        Socket rtspSocket = new Socket(this.session.getHost(), this.session
                .getPort());
        socketInputStream = rtspSocket.getInputStream();
        socketOutputStream = rtspSocket.getOutputStream();
        return rtspSocket;
    }

    public void start() throws IOException {
        Socket rtspSocket = connect();

        // send the basic signals to get it ready
        this.doOptions();
        this.doDescribe();
        this.doSetup();

        // send the PLAY command
        this.doPlay();

        rtspSocket.close();
    }

    public void doOptions() throws IOException {
        if (options)
            return;
        // create the base command
        String baseCommand = getBaseCommand("OPTIONS "
                + this.session.getSessionUrl());

        // execute it and read the response
        doCommand(baseCommand);

        // set flag
        options = true;
    }

    // creates, sends and parses a DESCRIBE client request
    public void doDescribe() throws IOException {

        // if already described, return
        if (described)
            return;

        // create the base command
        String baseCommand = getBaseCommand("DESCRIBE "
                + this.session.getSessionUrl());

        // execute it and read the response
        RtspPacket response = doCommand(baseCommand);

        String[] lines = response.getContent().split("\n");
        Pattern rtpmapPattern = Pattern.compile(
                "^a=rtpmap:(\\d+) (\\w+)/(\\d+)(.*)", Pattern.CASE_INSENSITIVE);

        Pattern controlPattern = Pattern.compile("^a=control:(.*)",
                Pattern.CASE_INSENSITIVE);

        boolean done = false;
        int i = 0;

        while (!done) {
            Matcher m = controlPattern.matcher(lines[i]);
            if (m.find())
                this.session.setSessionUrl(m.group(1));

            m = rtpmapPattern.matcher(lines[i]);
            if (m.find()) {
                int clockrate = Integer.parseInt(m.group(3));
                this.session.setClockrate(clockrate);

                int payloadType = Integer.parseInt(m.group(1));
                this.session.setPayloadType(payloadType);

                m = controlPattern.matcher(lines[i + 1]);
                if (!m.find())
                    throw new IOException(
                            "No control attribute after rtmap present. RTSP description content is: "
                                    + response.getContent());
                this.session.setControlUrl(m.group(1));
                done = true;
            }
            i++;
        }

        // set flag
        described = true;
    }

    // creates, sends and parses a SETUP client request
    public void doSetup() throws IOException {

        // if not described
        if (!described)
            throw new IOException("Not Described!");

        // create the base command for the first SETUP track
        String baseCommand = getBaseCommand("SETUP "
                + this.session.getControlUrl());

        // add the static transport data
        baseCommand += CRLF + TRANSPORT_DATA + this.session.getClientUdpPort()
                + "-" + this.session.getClientUdpPort();

        // read response
        RtspPacket response = doCommand(baseCommand);

        // parse it for session information
        this.session.setSessionId(response.get(SESSION));

        // if session information cannot be parsed, it is an error
        if (this.session.getSessionId() == null)
            throw new IOException("Could not find session info");

        // this is now setup
        setup = true;
    }

    // issues a PLAY command
    public void doPlay() throws IOException {

        // must be first setup
        if (!setup)
            throw new IOException("Not Setup!");

        // create base command
        String baseCommand = getBaseCommand("PLAY "
                + this.session.getControlUrl());

        // add session information
        baseCommand += CRLF + SESSION + ": " + this.session.getSessionId();

        // execute it
        doCommand(baseCommand);

        // set flags
        this.playing = true;
        this.stopped = false;
    }

    // issues a PAUSE command
    public void doPause() throws IOException {

        // if it is not playing, do nothing
        if (!playing)
            return;

        // create base command
        String baseCommand = getBaseCommand("PAUSE "
                + this.session.getSessionUrl());

        // add session information
        baseCommand += CRLF + SESSION + ": " + this.session.getSessionId();

        // execute it
        doCommand(baseCommand);

        // set flags
        this.stopped = true;
        this.playing = false;
    }

    // issues a TEARDOWN command
    public void doTeardown() throws IOException {
        Socket rtspSocket = this.connect();

        // if not setup, nothing to teardown
        if (!setup)
            return;

        // create base command
        String baseCommand = getBaseCommand("TEARDOWN "
                + this.session.getControlUrl());

        // add session information
        baseCommand += CRLF + SESSION + ": " + this.session.getSessionId();

        // execute it
        doCommand(baseCommand);

        // set flags
        described = setup = playing = false;
        stopped = true;

        rtspSocket.close();
    }

    // this method is a convenience method to put a RTSP command together
    protected String getBaseCommand(String command) {
        return (command + " " + VERSION + // version
                CRLF + "cseq: " + (CSeq++) // incrementing sequence
        );
    }

    // executes a command and receives response from server
    protected RtspPacket doCommand(String fullCommand) throws IOException {

        // to read the response from the server
        byte[] buffer = new byte[2048];

        // debug
        log.fine("Client request: \n" + fullCommand);

        // send a command
        socketOutputStream.write((fullCommand + CRLF + CRLF).getBytes());

        // read response
        int length = socketInputStream.read(buffer);
        String response = new String(buffer, 0, length);

        // empty the buffer
        buffer = null;

        RtspPacket packet = new RtspPacket();
        StringReader reader = new StringReader(response);
        BufferedReader bReader = new BufferedReader(reader);

        packet.setResponseCode(bReader.readLine().toUpperCase());

        // if the response doesn't start with an all clear
        if (!packet.getResponseCode().startsWith(RTSP_OK))
            throw new IOException("Server returned invalid code: " + response);

        boolean finished = false;
        while (!finished) {
            String line = bReader.readLine();

            if (line == null)
                finished = true;
            else
                this.parseHeaderLine(line, packet);
        }

        int contentLength = Integer.parseInt(packet.get("content-length"));

        if (contentLength > 0) {
            buffer = new byte[contentLength];
            length = socketInputStream.read(buffer);
            response = new String(buffer, 0, length);
            packet.setContent(response);
        }

        int cSeq = Integer.parseInt(packet.get("cseq"));
        packet.setcSeq(cSeq);

        // debug
        log.fine("Server response: \n" + packet.toString());

        return packet;
    }

    private void parseHeaderLine(String line, RtspPacket packet) {
        int startIndex = line.indexOf(":");

        if (startIndex < 0)
            return;

        String key = line.substring(0, startIndex).trim().toLowerCase();
        String value = line.substring(startIndex + 1, line.length()).trim();

        packet.put(key, value);
    }
}