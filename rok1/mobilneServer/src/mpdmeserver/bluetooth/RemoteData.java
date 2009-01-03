/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpdmeserver.bluetooth;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author sur
 */
public abstract class RemoteData {

    private ByteArrayOutputStream byteStream = null;
    private DataOutputStream dataStream = null;

    public byte[] encode() {
        this.byteStream = new ByteArrayOutputStream();
        this.dataStream = new DataOutputStream(byteStream);

        try {
            this.encodeImpl(this.dataStream);
            this.encodeFinished();
            return this.byteStream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected abstract void encodeImpl(DataOutputStream dataStream) throws IOException;

    private void encodeFinished() {
        try {
            this.byteStream.flush();
            this.byteStream.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
