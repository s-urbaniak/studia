/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.argsvalidator;

import protocol.properties.BiezProtProperties.Type;

/**
 *
 * @author sur
 */
public class ClientServerArgValidator implements ArgValidator<Type> {

    public static final String CLIENT_ARG = "client";
    public static final String SERVER_ARG = "server";

    @Override
    public Type validate(String[] args) {
        if ((args == null) || (args.length != 1))
            throw new UnsupportedOperationException(
                    "This validator expects exactly one string.");
        String arg = args[0];
        
        if (arg == null) {
            throw new UnsupportedOperationException(
                    "Please specify either " +
                    SERVER_ARG + " or " + CLIENT_ARG + ".");
        }

        if (SERVER_ARG.equals(arg.toLowerCase())) {
            return Type.SERVER;
        }

        if (CLIENT_ARG.equals(arg.toLowerCase())) {
            return Type.CLIENT;
        }

        throw new UnsupportedOperationException(
                "Unknown option " + arg + ". Please specify either " +
                SERVER_ARG + " or " + CLIENT_ARG + ".");
    }
}
