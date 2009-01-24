package protocol.commands;

import protocol.clientserver.*;

/**
 *
 * @author sur
 */
public class Response extends Command {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResponse() {
        Checksum chksum = new Checksum();
        chksum.calcChecksum(getCommand() + getData());
        return getCommand() + getData() + chksum.getChecksum().toUpperCase();
    }
}
