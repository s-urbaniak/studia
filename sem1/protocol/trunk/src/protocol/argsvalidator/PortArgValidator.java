/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package protocol.argsvalidator;

/**
 *
 * @author sur
 */
public class PortArgValidator implements ArgValidator<Integer> {

    @Override
    public Integer validate(String[] args) {
        if ((args == null) || (args.length != 1))
            throw new UnsupportedOperationException(
                    "This validator expects exactly one string.");
        String arg = args[0];

        if (arg == null) {
            throw new UnsupportedOperationException(
                    "Please specify a port");
        }

        return Integer.parseInt(arg);
    }
}
