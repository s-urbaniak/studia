package protocol.clientserver;

import java.util.zip.CRC32;

public class Checksum {

    private String actualChecksum;
    private String what;

    public String getChecksum() {
        return actualChecksum;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhat() {
        return what;
    }

    public String calcChecksum(String what) {
        this.what = what;
        return this.calcChecksum();
    }

    public String calcChecksum() {
        CRC32 crc = new CRC32();
        crc.update(this.what.getBytes());
        this.actualChecksum = Long.toHexString(crc.getValue());

        int diff = 8 - this.actualChecksum.length();

        for (int i=0; i<diff; i++)
            this.actualChecksum = "0".concat(this.actualChecksum);

        return this.actualChecksum;
    }

    public boolean check(String checksum) {
        return this.check(this.what, checksum);
    }

    public boolean check(String what, String checksum) {
        if (checksum == null) {
            return false;
        }

        this.what = what;
        calcChecksum();
        return this.actualChecksum.toUpperCase().equals(checksum.toUpperCase());
    }
}
