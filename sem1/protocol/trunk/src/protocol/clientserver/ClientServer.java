package protocol.clientserver;

import protocol.socket.SocketReader;
import protocol.socket.SocketWriter;

public interface ClientServer {
    public void start();

    public void setSocketWriter(SocketWriter writer);

    public void setSocketReader(SocketReader reader);

    public void setConsoleReader(SystemReader reader);
}
