/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.clientserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import protocol.properties.BiezProtProperties;

public class Server implements ClientServer {

    private BiezProtProperties props = null;
    private ServerSocket socket = null;
    private Socket clientSocket = null;

    public Server(BiezProtProperties props) {
        this.props = props;
    }

    @Override
    public void start() {
        try {
            socket = new ServerSocket(props.getPort());
            clientSocket = socket.accept();

            String inputLine, outputLine;
            SocketReader reader = new SocketReader(clientSocket);
            SocketWriter writer = new SocketWriter(clientSocket);

            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
                writer.println("OK");
            }

            writer.close();
            reader.close();

            clientSocket.close();
            socket.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
