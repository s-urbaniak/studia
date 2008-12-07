package protocol.clientserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SystemReader extends BufferedReader {
    public SystemReader() {
        super(new InputStreamReader(System.in));
    }
}
