package protocol.clientserver;

import java.util.zip.CRC32;

public class Checksum {
    public static String getChecksum(String what) {
        CRC32 crc = new CRC32();
        what.getBytes();
        return "";
    }
}
