/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.clientserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocol.properties.BiezProtProperties;
import protocol.properties.BiezProtProperties.Type;
import protocol.socket.SocketReader;
import protocol.socket.SocketWriter;

/**
 *
 * @author sur
 */
public abstract class ClientServerFactory {

    public final static ClientServer createClientServer(BiezProtProperties props) {
        ClientServer instance = null;

        if ((props == null) || (props.getType() == null)) {
            throw new UnsupportedOperationException("BiezProtProperties must not be null.");
        }

        if (Type.CLIENT.equals(props.getType())) {
            Socket socket = null;
            try {
                socket = new Socket(props.getServerHostname(), props.getPort());

                Client client = new Client();
                instance = client;

                client.setSocketReader(new SocketReader(socket));

                SystemReader consoleReader = new SystemReader();
                client.setConsoleReader(consoleReader);

                SocketWriter clientWriter = new SocketWriter(socket);
                clientWriter.setInterfere(true);
                clientWriter.setConsoleReader(consoleReader);

                client.setSocketWriter(clientWriter);
            } catch (UnknownHostException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (Type.SERVER.equals(props.getType())) {
            try {
                ServerSocket socket = null;
                socket = new ServerSocket(props.getPort());
                Socket clientSocket = socket.accept();

                Server server = new Server();
                server.setConsoleReader(new SystemReader());
                server.setSocketWriter(new SocketWriter(clientSocket));
                server.setSocketReader(new SocketReader(clientSocket));
                instance = server;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        return instance;
    }
}
