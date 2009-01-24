package protocol;

import protocol.commands.Response;
import protocol.commands.Command.Type;
import protocol.commands.Request;

public class ProtocolLayer {

    private Integer packageSize = null;
    private int fragmentIndex;
    private String buffer = null;
    private Response lastResponse = null;
    private State state = null;

    public enum State {

        DISCONNECTED,
        CRQ_SENT,
        CONNECTED
    };

    public ProtocolLayer() {
        this.state = State.DISCONNECTED;
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

    public void setData(String data) {
        byte[] bytes = data.getBytes();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            builder.append(Long.toHexString(bytes[i]));
        }

        this.buffer = data;
    }

    public Response answer(Request request) throws ProtocolException {
        if (!request.check()) {
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
        }

        return repeat();
    }

    public Response handleEnd() throws ProtocolException {
        this.buffer = this.buffer.replaceAll("Z", "");
        this.dataTransmitted(this.buffer);
        this.buffer = new String();

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.ACK);
        this.lastResponse.setData("");

        return this.lastResponse;
    }

    public void dataTransmitted(String data) {
    }

    public Response handleAcknowledge() throws ProtocolException {
        if (this.state != State.CONNECTED) {
            throw new ProtocolException("Wrong state: " + this.state.name());
        }

        if (this.fragmentIndex >= this.buffer.length()) {
            this.lastResponse = new Response();
            this.lastResponse.setType(Type.END);
            this.lastResponse.setData("");

            return this.lastResponse;
        } else {
            return this.sendData();
        }
    }

    public Response handleData(Request request) throws ProtocolException {
        if (this.state != State.CONNECTED) {
            throw new ProtocolException("Wrong state: " + this.state.name());
        }

        this.buffer = this.buffer.concat(request.getData());

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.ACK);
        this.lastResponse.setData("");

        return this.lastResponse;
    }

    public Response connectionRequest() throws ProtocolException {
        if (this.state != State.DISCONNECTED) {
            throw new ProtocolException("Wrong state: " + this.state.name());
        }

        this.lastResponse = new Response();
        this.lastResponse.setType(Type.CRQ);
        this.lastResponse.setData("");

        this.state = State.CRQ_SENT;
        return this.lastResponse;
    }

    public Response sendData() throws ProtocolException {
        if (this.state != State.CONNECTED) {
            throw new ProtocolException("Wrong state: " + this.state.name());
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

        return this.lastResponse;
    }

    private Response handleConnectionConfirmation(Request request) throws ProtocolException {
        if (this.state != State.CRQ_SENT) {
            throw new ProtocolException("Wrong state: " + this.state.name());
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
        if (this.state != State.DISCONNECTED) {
            throw new ProtocolException("Wrong state: " + this.state.name());
        }

        long packetSize = Math.round(Math.random() * 7) + 3;
        this.lastResponse = new Response();
        this.lastResponse.setType(Type.CON);
        this.lastResponse.setData(Long.toString(packetSize));

        this.buffer = new String();

        this.state = State.CONNECTED;
        return this.lastResponse;
    }
}
