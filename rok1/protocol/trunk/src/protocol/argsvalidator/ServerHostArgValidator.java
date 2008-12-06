package protocol.argsvalidator;

/**
 *
 * @author sur
 */
public class ServerHostArgValidator implements ArgValidator<String> {

    @Override
    public String validate(String[] args) {
        if ((args == null) || (args.length != 1))
            throw new UnsupportedOperationException(
                    "This validator expects exactly one string.");
        String arg = args[0];

        if (arg == null)
            throw new UnsupportedOperationException(
                    "Please specify a hostname.");

        return arg;
    }
}
