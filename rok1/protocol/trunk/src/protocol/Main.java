package protocol;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import protocol.argsvalidator.ArgValidator;
import protocol.argsvalidator.ClientServerArgValidator;

/**
 *
 * @author surbaniak
 */
public class Main {
    
    public static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);

        for(int i=0; i< buf.length; i++) {
            if(((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }
    
    public void fooPlayground() {
        
    }
    
    /** Creates a new instance of Main */
    public Main() {
        // TODO code application logic here
        try {
            System.out.println(Security.getAlgorithms("Cipher"));

            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(1024);
            KeyPair key = keygen.genKeyPair();

            byte[] publicKey = key.getPublic().getEncoded();
            byte[] privKey = key.getPrivate().getEncoded();

            System.out.println(publicKey.length);
            System.out.println(asHex(publicKey));

            String hello = "abcdefejewiorewiojoiijojfiotgjegwiojotrj";
            byte[] helloBytes = hello.getBytes("ASCII");

            Checksum chksum = new CRC32();
            chksum.update(helloBytes, 0, helloBytes.length);

            System.out.println("crc32: " + Long.toHexString(chksum.getValue()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // validate client / server validator
        ArgValidator validator = new ClientServerArgValidator();
        validator.validate(args[1]);
    }
}
