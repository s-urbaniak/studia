package org.urbanet.rtp.protocol.beans;

import java.util.HashMap;

public class RtspPacket {
    private int cSeq;

    private String content;

    private String responseCode;

    private HashMap header;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public RtspPacket() {
        this.header = new HashMap();
    }

    public int getcSeq() {
        return cSeq;
    }

    public void setcSeq(int cSeq) {
        this.cSeq = cSeq;
    }

    public void put(String headerField, String content) {
        this.header.put(headerField, content);
    }

    public String get(String headerField) {
        return (String) this.header.get(headerField);
    }

    @Override
    public String toString() {
        return "RtspPacket [cSeq=" + cSeq + ", content=\n" + content
                + "\nheader=" + header + ", responseCode=" + responseCode + "]";
    }
}