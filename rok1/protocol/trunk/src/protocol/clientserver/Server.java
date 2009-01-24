/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.clientserver;

import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.ProtocolException;
import protocol.socket.SocketReader;
import protocol.socket.SocketWriter;
import java.io.IOException;
import protocol.ProtocolLayer;
import protocol.commands.Request;
import protocol.commands.Response;

public class Server extends ProtocolLayer implements ClientServer {

    private SystemReader consoleReader = null;
    private SocketReader reader = null;
    private SocketWriter writer = null;

    @Override
    public void dataTransmitted(String data) {
        System.out.println("The client sent us the following string:");

        char[] txtInByte = new char[data.length() / 2];
        int j = 0;
        for (int i = 0; i < data.length(); i += 2) {
            txtInByte[j++] = (char)Integer.parseInt(data.substring(i, i + 2), 16);
        }

        String txt = new String(txtInByte);

        System.out.println(txt);
    }

    @Override
    public void start() {
        String inputLine;

        try {
            while ((inputLine = reader.readLine()) != null) {
                Request req = new Request(inputLine);
                try {
                    Response res = this.answer(req);
                    writer.println(res.getResponse());
                } catch (ProtocolException ex) {
                    throw new RuntimeException(ex);
                }
            }

            writer.close();
            reader.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setConsoleReader(SystemReader reader) {
        this.consoleReader = reader;
    }

    @Override
    public void setSocketWriter(SocketWriter writer) {
        this.writer = writer;
    }

    @Override
    public void setSocketReader(SocketReader reader) {
        this.reader = reader;
    }
}
