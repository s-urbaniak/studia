/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
            BufferedReader socketIn = new BufferedReader(
                    new InputStreamReader(
                    clientSocket.getInputStream()));
            PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);

            while ((inputLine = socketIn.readLine()) != null) {
                System.out.println(inputLine);
                socketOut.println("OK");
            }

            socketOut.close();
            socketIn.close();
            clientSocket.close();
            socket.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
