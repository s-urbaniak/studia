package protocol.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketReader extends BufferedReader {
    private Socket socket;
    
    public SocketReader(Socket socket) throws IOException {
        super(new InputStreamReader(socket.getInputStream()));
        this.socket = socket;
    }
}
