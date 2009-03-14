package protocol.clientserver;

import protocol.commands.Request;
import protocol.commands.Command;
import junit.framework.TestCase;

public class RequestTest extends TestCase {

    Request request;

    public RequestTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.request = new Request();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testChecksum() {
        assertTrue(request.decomposeRequest("CMD12345678"));
        assertFalse(request.check());

        assertTrue(request.decomposeRequest("CRQ197CB43F"));
        assertTrue(request.check());

        assertTrue(request.decomposeRequest("CMDDATA12345678"));
        assertFalse(request.check());

        assertTrue(request.decomposeRequest("CRQDATA16983ED6"));
        assertTrue(request.check());

        assertTrue(request.decomposeRequest("CRQDATA16983ed6"));
        assertTrue(request.check());

        assertTrue(request.decomposeRequest("CRQDATA16983ed7"));
        assertFalse(request.check());
    }

    public void testDecomposeRequest() {
        assertTrue(request.decomposeRequest("CMD12345678"));
        assertEquals("CMD", request.getCommand());
        assertEquals("12345678", request.getCrc());
        assertEquals("", request.getData());

        assertTrue(request.decomposeRequest("CRQ12345678"));
        assertEquals("CRQ", request.getCommand());
        assertEquals("12345678", request.getCrc());
        assertEquals("", request.getData());
        assertEquals(Command.Type.CRQ, request.getType());

        assertFalse(request.decomposeRequest(null));
        assertFalse(request.decomposeRequest("CMD123"));
        assertFalse(request.decomposeRequest("CM"));

        request.setRequest("CMDaaaaaaa12345678");
        assertTrue(request.decomposeRequest());
        assertEquals("CMD", request.getCommand());
        assertEquals("12345678", request.getCrc());
        assertEquals("aaaaaaa", request.getData());

        assertTrue(request.decomposeRequest("CMD"));
        assertEquals("CMD", request.getCommand());
        assertEquals("", request.getCrc());
        assertEquals("", request.getData());
    }
}
