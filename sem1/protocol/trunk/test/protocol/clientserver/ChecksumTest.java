/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.clientserver;

import junit.framework.TestCase;

public class ChecksumTest extends TestCase {

    private Checksum chksum;

    public ChecksumTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.chksum = new Checksum();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCheck() {
        assertTrue(chksum.check("test", "d87f7e0c"));
        assertTrue(chksum.check("test", "D87F7e0c"));
        assertFalse(chksum.check("test", "a87F7e0c"));
        assertFalse(chksum.check("test", ""));
        assertFalse(chksum.check("test", null));
    }

    public void testChecksum() {
        chksum.calcChecksum("test");
        assertNotNull(chksum.getChecksum());
        assertEquals("d87f7e0c".toUpperCase(), chksum.getChecksum().toUpperCase());
        System.out.println("Checksum of test = " + chksum.getChecksum());

        chksum.calcChecksum("CRQ");
        assertNotNull(chksum.getChecksum());
        assertEquals("197CB43F".toUpperCase(), chksum.getChecksum().toUpperCase());
        System.out.println("Checksum of CRQ = " + chksum.getChecksum());

        chksum.calcChecksum("CMDDATA");
        assertNotNull(chksum.getChecksum());
        System.out.println("Checksum of CMDDATA = " + chksum.getChecksum());
    }
}
