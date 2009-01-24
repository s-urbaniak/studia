package protocol.clientserver;

public class Protocol {
    private enum State {
        DISCONNECTED, 
        CRQ_SENT,
        CON_SENT,
        DTA_SENT,
        ACK_SENT,
        REP_SENT,
        END_SENT,
        DIS_SENT
    };

    private State state;

    public String connect() {
        if (state != State.DISCONNECTED)
            throw new RuntimeException("You are not disconnected !");

        return "CRQ";
    }

    public String answer(String request) {
        return "";
    }
}
