/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package protocol.argsvalidator;

import protocol.properties.BiezProtProperties;
import protocol.properties.BiezProtProperties.Type;

/**
 *
 * @author sur
 */
public class CmdLineArgsValidator implements ArgValidator<BiezProtProperties> {

    @Override
    public BiezProtProperties validate(String[] args) {
        if (args.length < 2) {
            throw new UnsupportedOperationException("Not enough parameters given.");
        }

        BiezProtProperties props = new BiezProtProperties();

        // validate client / server validator
        ClientServerArgValidator validator = new ClientServerArgValidator();
        props.setType(validator.validate(new String[]{args[0]}));

        PortArgValidator portValidator = new PortArgValidator();
        props.setPort(portValidator.validate(new String[]{args[1]}));

        // if this is a client, then we still need the server hostname
        if (Type.CLIENT.equals(props.getType())) {
            if (args.length < 3) {
                throw new UnsupportedOperationException("Not enough parameters given for a client.");
            }

            ServerHostArgValidator srvValidator = new ServerHostArgValidator();
            props.setServerHostname(srvValidator.validate(new String[]{args[2]}));
        }

        return props;
    }
}
