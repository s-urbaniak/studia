/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.clientserver;

import protocol.socket.SocketReader;
import protocol.socket.SocketWriter;
import java.io.IOException;
import java.net.Socket;
import protocol.properties.BiezProtProperties;

public class Client implements ClientServer {

    private BiezProtProperties props = null;
    private Socket socket = null;

    public Client(BiezProtProperties props) {
        this.props = props;
    }

    @Override
    public void start() {
        try {
            socket = new Socket(props.getServerHostname(), props.getPort());
            SocketReader reader = new SocketReader(socket);
            SocketWriter writer = new SocketWriter(socket);
            SystemReader sysReader = new SystemReader();

            String fromServer, fromUser;

            boolean quit = false;
            while (!quit) {
                fromUser = sysReader.readLine();
                writer.println(fromUser);
                fromServer = reader.readLine();

                if (fromServer == null) {
                    throw new IOException("No response from server.");
                }

                System.out.println(fromServer);

                if ("quit".equals(fromUser)) {
                    quit = true;
                }
            }
            
            writer.close();
            reader.close();
            sysReader.close();
            socket.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
