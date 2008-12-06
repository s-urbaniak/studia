package protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketReader {

    private BufferedReader reader;
    private String input;
    private Socket socket = null;
    private boolean closed = false;

    public boolean isClosed() {
        return closed;
    }

    public SocketReader(Socket socket) throws IOException {
        this.socket = socket;
        reader =
                new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
    }

    public String read() throws IOException {
        return reader.readLine();
    }
}
