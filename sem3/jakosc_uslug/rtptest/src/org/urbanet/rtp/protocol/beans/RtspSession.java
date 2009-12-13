package org.urbanet.rtp.protocol.beans;

public class RtspSession {
    private String host;

    private int port;

    private String stream;

    private int clientUdpPort;

    private int payloadType;

    private int clockrate;

    private String sessionId;

    private String sessionUrl;

    private String controlUrl;

    public int getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(int payloadType) {
        this.payloadType = payloadType;
    }

    public int getClockrate() {
        return clockrate;
    }

    public void setClockrate(int clockrate) {
        this.clockrate = clockrate;
    }

    public String getControlUrl() {
        return controlUrl;
    }

    public void setControlUrl(String controlUrl) {
        this.controlUrl = controlUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getClientUdpPort() {
        return clientUdpPort;
    }

    public void setClientUdpPort(int clientUdpPort) {
        this.clientUdpPort = clientUdpPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public void setSessionUrl(String sessionUrl) {
        this.sessionUrl = sessionUrl;
    }

    public String getSessionUrl() {
        if (sessionUrl != null)
            return sessionUrl;
        else
            return "rtsp://" + host + ":" + port + stream;
    }

    @Override
    public String toString() {
        return "RtspSession [clientUdpPort=" + clientUdpPort + ", clockrate="
                + clockrate + ", controlUrl=" + controlUrl + ", host=" + host
                + ", payloadType=" + payloadType + ", port=" + port
                + ", sessionId=" + sessionId + ", sessionUrl=" + sessionUrl
                + ", stream=" + stream + "]";
    }
}