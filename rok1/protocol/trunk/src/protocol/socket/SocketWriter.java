package protocol.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketWriter extends PrintWriter {
    private Socket socket;

    public SocketWriter(Socket socket) throws IOException {
        super(socket.getOutputStream(), true);
        this.socket = socket;
    }
}
