package protocol.clientserver;

import java.io.IOException;
import protocol.ProtocolException;
import protocol.socket.SocketReader;
import protocol.socket.SocketWriter;
import protocol.ProtocolLayer;
import protocol.commands.Command;
import protocol.commands.Request;
import protocol.commands.Response;

public class Client extends ProtocolLayer implements ClientServer {

    private SystemReader consoleReader = null;
    private SocketReader reader = null;
    private SocketWriter writer = null;

    @Override
    public void start() {
        try {
            boolean quit = false;

            // connect to server
            Response res = this.connect();
            repeatUntilOk(res);

            while (!quit) {
                System.out.println("Please enter data to be transmitted or enter 'q' to quit:");
                String data = consoleReader.readLine();

                char[] chars = data.toCharArray();

                StringBuffer output = new StringBuffer();
                for (int i = 0; i < chars.length; i++) {
                    output.append(Integer.toHexString((int) chars[i]));
                }

                this.setBuffer(output.toString().toUpperCase());

                if ("q".equals(data)) {
                    quit = true;
                    res = this.disconnect();
                    repeatUntilOk(res);
                } else {
                    // send data
                    res = this.sendBuffer();
                    while (res.getType() != Command.Type.END) {
                        res = repeatUntilOk(res);
                    }

                    // send END packet
                    repeatUntilOk(res);
                }
            }

            writer.close();
            reader.close();
            consoleReader.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Response repeatUntilOk(Response res) throws IOException, ProtocolException {
        boolean ok = false;

        while (!ok) {
            writer.println(res.getResponse());
            String fromServerString = reader.readLine();
            System.out.println("\nServer: " + fromServerString);
            Request fromServer = new Request(fromServerString);
            res = this.answer(fromServer);
            if ((res == null) || (fromServer.getType() != Command.Type.REP)) {
                ok = true;
            }
        }

        return res;
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
