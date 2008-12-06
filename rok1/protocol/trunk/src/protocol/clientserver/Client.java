/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);

            String fromServer, fromUser;

            boolean quit = false;
            while (!quit) {
                fromUser = stdIn.readLine();
                socketOut.println(fromUser);
                fromServer = socketIn.readLine();

                if (fromServer == null) {
                    throw new IOException("No response from server.");
                }

                System.out.println(fromServer);

                if ("quit".equals(fromUser)) {
                    quit = true;
                }
            }
            
            socketOut.close();
            socketIn.close();
            stdIn.close();
            socket.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
