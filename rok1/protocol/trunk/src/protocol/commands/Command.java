package protocol.commands;

public class Command {

    public enum Type {

        CRQ,
        CON,
        DTA,
        ACK,
        REP,
        END,
        DIS,
        unknown
    };
    private String command;
    private Type type;

    public String getCommand() {
        return command;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;

        if (type == null) {
            this.command = "";
            return;
        }

        if (type == Type.unknown) {
            this.command = "";
            return;
        }

        this.command = type.name().toUpperCase();
    }

    public void setCommand(String command) {
        if (command == null) {
            this.command = "";
        }

        command = command.toUpperCase();
        this.command = command;

        try {
            this.type = Enum.valueOf(Type.class, command);
        } catch (IllegalArgumentException e) {
            this.type = Type.unknown;
        }
    }
}
