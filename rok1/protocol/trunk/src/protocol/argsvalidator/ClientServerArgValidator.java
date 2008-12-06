/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.argsvalidator;

/**
 *
 * @author sur
 */
public class ClientServerArgValidator implements ArgValidator {

    public static final String CLIENT_ARG = "client";
    public static final String SERVER_ARG = "server";

    @Override
    public void validate(String arg) {
        if (SERVER_ARG.equals(arg)) {
            return;
        }

        if (CLIENT_ARG.equals(arg)) {
            return;
        }

        throw new UnsupportedOperationException(
                "Unknown option. Please specify either " +
                SERVER_ARG + " or " + CLIENT_ARG + ".");
    }
}
