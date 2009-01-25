package protocol;

import java.util.HashMap;
import protocol.commands.Response;
import protocol.commands.Command.Type;
import protocol.commands.Request;

public class ProtocolLayer {

    private Integer packageSize = null;
    private int fragmentIndex = 0;
    private String buffer = null;
    private Response lastResponse = null;
    private State state = null;
    private HashMap<String, String> received = null;

    public enum State {

        DISCONNECTED,
        CRQ_SENT,
        CONNECTED,
        CONNECTION_ACCEPTED,
        DTA_SENT
    };

    public ProtocolLayer() {
        this.state = State.DISCONNECTED;
        this.received = new HashMap<String, String>();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getBuffer() {
        return buffer;
    }

    public void setBuffer(String data) {
        this.buffer = data;
        this.fragmentIndex = 0;
    }

    public Response answer(Request request) throws ProtocolException {
        if (!(request.decomposeRequest() && request.check())) {
            return repeat();
        }

        switch (request.getType()) {
            case REP:
                return this.lastResponse;
            case CRQ:
                return handleConnectionRequest();
            case CON:
                return handleConnectionConfirmation(request);
            case DTA:
                return handleData(request);
            case ACK:
                return handleAcknowledge();
            case END:
                return handleEnd();
            case DIS:
                return handleDisconnect();
        }

        return repeat();
    }

    public Response handleDisconnect() throws ProtocolException {
        if (wrongState(new State[]{State.CONNECTION_ACCEPTED})) {
            return repeat();
        }

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.ACK);
        this.lastResponse.setData("");

        return this.lastResponse;
    }

    public Response handleEnd() throws ProtocolException {
        if (wrongState(new State[]{State.CONNECTION_ACCEPTED})) {
            return repeat();
        }

        this.buffer = this.buffer.replaceAll("Z", "");
        this.dataTransmitted(this.buffer);
        this.buffer = new String();
        this.received.clear();

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.ACK);
        this.lastResponse.setData("");

        return this.lastResponse;
    }

    public void dataTransmitted(String data) {
    }

    public Response handleAcknowledge() throws ProtocolException {
        if (wrongState(new State[]{State.CONNECTED, State.DTA_SENT})) {
            return repeat();
        }

        if (this.fragmentIndex >= this.buffer.length()) {
            this.lastResponse = new Response();
            this.lastResponse.setType(Type.END);
            this.lastResponse.setData("");
            this.fragmentIndex = 0;

            return this.lastResponse;
        } else {
            return this.sendBuffer();
        }
    }

    private boolean wrongState(State[] states) {
        boolean check = false;

        for (int i = 0; i < states.length; i++) {
            check |= this.state == states[i];
        }

        return !check;
    }

    public Response handleData(Request request) throws ProtocolException {
        if (wrongState(new State[]{State.CONNECTION_ACCEPTED})) {
            return repeat();
        }

        if (received.get(request.getRequest()) != null) {
            return repeat();
        }

        received.put(request.getRequest(), "");
        this.buffer = this.buffer.concat(request.getData());

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.ACK);
        this.lastResponse.setData("");

        return this.lastResponse;
    }

    public Response disconnect() throws ProtocolException {
        if (wrongState(new State[]{State.CONNECTED, State.DTA_SENT})) {
            return repeat();
        }

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.DIS);
        this.lastResponse.setData("");

        return this.lastResponse;
    }

    public Response connect() throws ProtocolException {
        if (wrongState(new State[]{State.DISCONNECTED})) {
            return repeat();
        }

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.CRQ);
        this.lastResponse.setData("");

        this.state = State.CRQ_SENT;
        return this.lastResponse;
    }

    public Response sendBuffer() throws ProtocolException {
        if (wrongState(new State[]{State.CONNECTED, State.DTA_SENT})) {
            return repeat();
        }

        if (this.packageSize == null) {
            throw new ProtocolException("No package size defined.");
        }

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.DTA);

        String fragment = null;
        if (this.getBuffer().length() < packageSize.intValue()) {
            fragment = this.getBuffer();
            this.fragmentIndex = fragment.length();
        } else {
            int endindex = this.fragmentIndex + packageSize.intValue();

            if (endindex > this.getBuffer().length()) {
                endindex = this.getBuffer().length();
            }

            fragment = this.getBuffer().substring(
                    this.fragmentIndex,
                    endindex);

            this.fragmentIndex += packageSize.longValue();
        }

        int diff = packageSize.intValue() - fragment.length();
        for (int i = 0; i < diff; i++) {
            fragment = fragment.concat("Z");
        }

        this.lastResponse.setData(fragment);

        this.state = State.DTA_SENT;
        return this.lastResponse;
    }

    private Response handleConnectionConfirmation(Request request) throws ProtocolException {
        if (wrongState(new State[]{State.CRQ_SENT})) {
            return repeat();
        }

        this.packageSize = Integer.parseInt(request.getData());

        this.state = State.CONNECTED;
        return null;
    }

    private Response repeat() {
        this.lastResponse = new Response();
        this.lastResponse.setType(Type.REP);
        this.lastResponse.setData("");
        return this.lastResponse;
    }

    private Response handleConnectionRequest() throws ProtocolException {
        if (wrongState(new State[]{State.DISCONNECTED})) {
            return repeat();
        }

        long packetSize = Math.round(Math.random() * 7) + 3;
        this.lastResponse = new Response();
        this.lastResponse.setType(Type.CON);
        this.lastResponse.setData(Long.toString(packetSize));

        this.buffer = new String();

        this.state = State.CONNECTION_ACCEPTED;
        return this.lastResponse;
    }
}
