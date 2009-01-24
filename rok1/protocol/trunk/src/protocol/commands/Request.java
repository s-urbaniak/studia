package protocol.commands;

import protocol.clientserver.*;

public class Request extends Command {

    private String request;
    private String crc;
    private String data;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public Request() {
    }

    public Request(String request) {
        this.request = request;
    }

    public boolean decomposeRequest() {
        return decomposeRequest(this.request);
    }

    public boolean check() {
        Checksum checksum = new Checksum();
        checksum.setWhat(this.getCommand() + this.getData());
        boolean crcCheck = checksum.check(this.crc);
        boolean typeCheck = !(this.getType() == Command.Type.unknown);
        return (crcCheck & typeCheck);
    }

    public boolean decomposeRequest(String request) {
        this.request = request;

        if (request == null)
            return false;

        if (request.length() < 3)
            return false;

        this.setCommand(request.substring(0, 3));

        if (request.length() > 3) {
            if (request.length() < 11)
                return false;

            this.crc = request.substring(request.length() - 8, request.length());
            this.data = request.substring(3, request.length() - 8);
        } else {
            this.crc = "";
            this.data = "";
        }

        return true;
    }
}
