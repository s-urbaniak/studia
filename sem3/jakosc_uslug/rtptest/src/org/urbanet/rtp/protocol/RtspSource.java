package org.urbanet.rtp.protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.urbanet.rtp.protocol.beans.RtspPacket;

public class RtspSource {

    private final static Logger log = Logger.getLogger(RtspSource.class
            .getName());

    // the address of the media file as an rtsp://... String
    private String host;

    private int port;

    private String address;

    private String stream;

    // the inputstream to receive response from the server
    private InputStream socketInputStream;

    // the outputstream to write to the server
    private OutputStream socketOutputStream;

    // the incrementing sequence number for each request
    // sent by the client
    private static int CSeq = 1;

    // the session id sent by the server after an initial setup
    private String sessionId;

    // the number of tracks in a media file
    private Vector tracks = new Vector(2);

    // flags to indicate the status of a session
    private boolean options, described, setup, playing;

    private int udpPort;

    private boolean stopped = false;

    // constants
    private static final String SESSION = "session";
    private static final String CRLF = "\r\n";
    private static final String VERSION = "RTSP/1.0";
    private static final String TRACK_LINE = "trackID=";
    private static final String TRANSPORT_DATA = "TRANSPORT: RTP/AVP;unicast;client_port=";
    private static final String RTSP_OK = VERSION + " 200 OK";

    // base constructor, takes the media address, input and output streams
    public RtspSource(String host, int port, String stream, int udpPort) {
        this.host = host;
        this.port = port;
        this.udpPort = udpPort;
        this.stream = stream;
        this.address = "rtsp://" + host + ":" + port + stream;
    }

    protected Socket connect() throws IOException {
        Socket rtspSocket = new Socket(host, port);
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
        String baseCommand = getBaseCommand("OPTIONS " + address);

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
        String baseCommand = getBaseCommand("DESCRIBE " + address);

        // execute it and read the response
        RtspPacket response = doCommand(baseCommand);

        // the response will contain track information, amongst other things
        parseTrackInformation(response.getContent());

        // set flag
        described = true;
    }

    // creates, sends and parses a SETUP client request
    public void doSetup() throws IOException {

        // if not described
        if (!described)
            throw new IOException("Not Described!");

        // create the base command for the first SETUP track
        String baseCommand = getBaseCommand("SETUP " + address + "/trackID="
                + tracks.elementAt(0));

        // add the static transport data
        baseCommand += CRLF + TRANSPORT_DATA + this.udpPort + "-"
                + this.udpPort;

        // read response
        RtspPacket response = doCommand(baseCommand);

        // parse it for session information
        this.sessionId = response.get(SESSION);

        // if session information cannot be parsed, it is an error
        if (sessionId == null)
            throw new IOException("Could not find session info");

        // now, send SETUP commands for each of the tracks
        int cntOfTracks = tracks.size();
        for (int i = 1; i < cntOfTracks; i++) {
            baseCommand = getBaseCommand("SETUP " + address + "/trackID="
                    + tracks.elementAt(i));
            baseCommand += CRLF + SESSION + sessionId + CRLF + TRANSPORT_DATA;
            doCommand(baseCommand);
        }

        // this is now setup
        setup = true;
    }

    // issues a PLAY command
    public void doPlay() throws IOException {

        // must be first setup
        if (!setup)
            throw new IOException("Not Setup!");

        // create base command
        String baseCommand = getBaseCommand("PLAY " + address);

        // add session information
        baseCommand += CRLF + SESSION + ": " + sessionId;

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
        String baseCommand = getBaseCommand("PAUSE " + address);

        // add session information
        baseCommand += CRLF + SESSION + ": " + sessionId;

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
        String baseCommand = getBaseCommand("TEARDOWN " + address);

        // add session information
        baseCommand += CRLF + SESSION + ": " + sessionId;

        // execute it
        doCommand(baseCommand);

        // set flags
        described = setup = playing = false;
        stopped = true;

        rtspSocket.close();
    }

    // this method is a convenience method to put a RTSP command together
    private String getBaseCommand(String command) {
        return (command + " " + VERSION + // version
                CRLF + "cseq: " + (CSeq++) // incrementing sequence
        );
    }

    // executes a command and receives response from server
    private RtspPacket doCommand(String fullCommand) throws IOException {

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

    // convenience method to parse a server response to DESCRIBE command
    // for track information
    private void parseTrackInformation(String response) throws IOException {

        String localRef = response;
        String trackId = "";

        // iterate through the response to find all instances of the
        // TRACK_LINE, which indicates all the tracks. Add all the
        // track id's to the tracks vector
        int index = localRef.indexOf(TRACK_LINE);
        while (index != -1) {
            int baseIdx = index + TRACK_LINE.length();
            trackId = localRef.substring(baseIdx, baseIdx + 1);
            localRef = localRef.substring(baseIdx + 1, localRef.length());
            index = localRef.indexOf(TRACK_LINE);
            tracks.addElement(trackId);
        }

    }
}