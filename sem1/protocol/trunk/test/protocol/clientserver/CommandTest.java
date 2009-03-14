/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package protocol.clientserver;

import protocol.commands.Command;
import junit.framework.TestCase;

/**
 *
 * @author sur
 */
public class CommandTest extends TestCase {
    private Command cmd;

    public CommandTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.cmd = new Command();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetType() {
        this.cmd.setType(Command.Type.ACK);
        assertEquals("ACK", this.cmd.getCommand());

        this.cmd.setType(Command.Type.unknown);
        assertEquals("", this.cmd.getCommand());

        this.cmd.setType(Command.Type.DIS);
        assertEquals("DIS", this.cmd.getCommand());

        this.cmd.setType(Command.Type.END);
        assertEquals("END", this.cmd.getCommand());

        this.cmd.setType(null);
        assertEquals("", this.cmd.getCommand());
    }

    public void testSetCommand() {
        this.cmd.setCommand("ACK");
        assertEquals(Command.Type.ACK, this.cmd.getType());

        this.cmd.setCommand("DIS");
        assertEquals(Command.Type.DIS, this.cmd.getType());

        this.cmd.setCommand("asd");
        assertEquals(Command.Type.unknown, this.cmd.getType());

        this.cmd.setCommand("end");
        assertEquals(Command.Type.END, this.cmd.getType());

        this.cmd.setCommand("crq");
        assertEquals(Command.Type.CRQ, this.cmd.getType());
    }
}
