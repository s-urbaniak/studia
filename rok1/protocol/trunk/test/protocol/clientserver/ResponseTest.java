/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.clientserver;

import protocol.commands.Command;
import protocol.commands.Response;
import junit.framework.TestCase;

/**
 *
 * @author sur
 */
public class ResponseTest extends TestCase {

    private Response response;

    public ResponseTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.response = new Response();
    }

    public void testResponse() {
        response.setData("DDDDDDD");
        response.setType(Command.Type.DTA);
        String result = response.getResponse();

        System.out.println("response = " + result);
        assertNotNull(result);
        assertEquals("DTADDDDDDDB1ED1FC2", result);

        response.setData("");
        response.setType(Command.Type.CRQ);
        result = response.getResponse();

        System.out.println("response = " + result);
        assertNotNull(result);
        assertEquals("CRQ197CB43F", result);
    }
}
