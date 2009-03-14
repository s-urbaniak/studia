package protocol.clientserver;

import protocol.ProtocolLayer;
import protocol.ProtocolLayer.State;
import protocol.ProtocolException;
import protocol.commands.Request;
import protocol.commands.Command;
import protocol.commands.Command.Type;
import protocol.commands.Response;
import junit.framework.TestCase;

/**
 *
 * @author sur
 */
public class ProtocolTest extends TestCase {
    private String transmittedData = null;

    private ProtocolLayer protocol;

    public ProtocolTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        protocol = new ProtocolLayer();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRep() throws ProtocolException {
        Request req = new Request();
        req.setType(Command.Type.REP);
        req.setData("");
        Checksum crc = new Checksum();
        req.setCrc(crc.calcChecksum(req.getCommand() + req.getData()));
        req.setRequest(req.getCommand() + req.getCrc());

        protocol.setState(ProtocolLayer.State.DISCONNECTED);
        protocol.connect();

        Response res = protocol.answer(req);
        assertEquals(Command.Type.CRQ, res.getType());
    }

    public void testGenerateData() {
        protocol.setBuffer("ä");
        System.out.println(protocol.getBuffer());
    }

    public void testProtocolShort() throws ProtocolException {
        // client
        ProtocolLayer client = new ProtocolLayer();
        Response res = client.connect();

        assertEquals(State.CRQ_SENT, client.getState());
        assertEquals(Type.CRQ, res.getType());

        // server
        Request req = new Request();
        req.setRequest(res.getResponse());
        req.decomposeRequest();

        assertTrue(req.check());
        assertEquals(res.getData(), req.getData());
        assertEquals(res.getCommand(), req.getCommand());

        ProtocolLayer server = new ProtocolLayer() {
            @Override
            public void dataTransmitted(String data) {
                transmittedData = data;
            }
        };

        res = server.answer(req);
        res.setData("7");

        assertEquals(State.CONNECTED, server.getState());
        assertEquals(Type.CON, res.getType());

        // client
        req.setRequest(res.getResponse());
        req.decomposeRequest();

        assertEquals(res.getData(), req.getData());
        assertEquals(res.getCommand(), req.getCommand());
        assertTrue(req.check());

        res = client.answer(req);

        assertNull(res);
        assertEquals(State.CONNECTED, client.getState());

        // client sending data
        client.setBuffer("abcd");
        res = client.sendBuffer();

        System.out.println("client data = " + res.getResponse());
        assertEquals("abcdZZZ", res.getData());
        assertEquals(Type.DTA, res.getType());

        // server
        req.decomposeRequest(res.getResponse());
        res = server.answer(req);

        assertEquals(Type.ACK, res.getType());
        assertEquals("abcdZZZ", server.getBuffer());

        // client
        req.decomposeRequest(res.getResponse());
        res = client.answer(req);

        assertEquals(Type.END, res.getType());

        // server
        req.decomposeRequest(res.getResponse());
        res = server.answer(req);

        assertEquals("abcd", transmittedData);
        assertEquals(Type.ACK, res.getType());
    }

    public void testProtocolLong() throws ProtocolException {
        // client
        ProtocolLayer client = new ProtocolLayer();
        Response res = client.connect();

        assertEquals(State.CRQ_SENT, client.getState());
        assertEquals(Type.CRQ, res.getType());

        // server
        Request req = new Request();
        req.setRequest(res.getResponse());
        req.decomposeRequest();

        assertTrue(req.check());
        assertEquals(res.getData(), req.getData());
        assertEquals(res.getCommand(), req.getCommand());

        ProtocolLayer server = new ProtocolLayer() {
            @Override
            public void dataTransmitted(String data) {
                transmittedData = data;
            }
        };

        res = server.answer(req);
        res.setData("7");

        assertEquals(State.CONNECTED, server.getState());
        assertEquals(Type.CON, res.getType());

        // client
        req.setRequest(res.getResponse());
        req.decomposeRequest();

        assertEquals(res.getData(), req.getData());
        assertEquals(res.getCommand(), req.getCommand());
        assertTrue(req.check());

        res = client.answer(req);

        assertNull(res);
        assertEquals(State.CONNECTED, client.getState());

        // client sending data
        client.setBuffer("abcdefhijklm");
        res = client.sendBuffer();

        System.out.println("client data = " + res.getResponse());
        assertEquals("abcdefh", res.getData());
        assertEquals(Type.DTA, res.getType());

        // server
        req.decomposeRequest(res.getResponse());
        res = server.answer(req);
        assertEquals(Type.ACK, res.getType());

        // client
        req.decomposeRequest(res.getResponse());
        res = client.answer(req);

        System.out.println("client data = " + res.getResponse());
        assertEquals("ijklmZZ", res.getData());
        assertEquals(Type.DTA, res.getType());

        // introduce error
        String error = "DTAijklmZZ1DE4630G";

        // server
        req.decomposeRequest(error);
        res = server.answer(req);
        assertEquals(Type.REP, res.getType());

        // client
        req.decomposeRequest(res.getResponse());
        res = client.answer(req);

        System.out.println("client data = " + res.getResponse());
        assertEquals("ijklmZZ", res.getData());
        assertEquals(Type.DTA, res.getType());

        // server
        req.setRequest(res.getResponse());
        res = server.answer(req);
        assertEquals(Type.ACK, res.getType());

        // client
        req.decomposeRequest(res.getResponse());
        res = client.answer(req);

        assertEquals(Type.END, res.getType());

        // server
        req.decomposeRequest(res.getResponse());
        res = server.answer(req);

        assertEquals("abcdefhijklm", transmittedData);
        assertEquals(Type.ACK, res.getType());
    }

    public void testFalsePacket() throws ProtocolException {
        Request req = new Request();
        req.setType(Command.Type.DTA);
        req.setData("sadasds");
        req.setCrc("12345678");
        Response res = protocol.answer(req);
        assertEquals(Command.Type.REP, res.getType());
    }

    public void testConnectionRequest() throws ProtocolException {
        protocol.setState(ProtocolLayer.State.DISCONNECTED);
        Response res = protocol.connect();
        assertEquals(Command.Type.CRQ, res.getType());
        assertEquals("", res.getData());

        protocol.setState(ProtocolLayer.State.CRQ_SENT);
        ProtocolException ex = null;
        try {
            res = protocol.connect();
        } catch (ProtocolException e) {
            ex = e;
        }
        assertNotNull(ex);
    }
}
