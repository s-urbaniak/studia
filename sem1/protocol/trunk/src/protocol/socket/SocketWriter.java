package protocol.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import protocol.clientserver.SystemReader;

public class SocketWriter extends PrintWriter {

    private boolean interfere;
    private Socket socket;
    private SystemReader consoleReader;

    public void setInterfere(boolean interfere) {
        this.interfere = interfere;
    }

    public SocketWriter(Socket socket) throws IOException {
        super(socket.getOutputStream(), true);
        this.socket = socket;
        this.interfere = false;
    }

    public void setConsoleReader(SystemReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    @Override
    public void println(String what) {
        if (interfere) {
            System.out.println("Data  : " + what);
            System.out.print("Change: ");
            String changed = "";
            try {
                changed = consoleReader.readLine();
                if ("".equals(changed))
                    changed = what;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            super.println(changed);
        } else {
            super.println(what);
        }
    }
}
