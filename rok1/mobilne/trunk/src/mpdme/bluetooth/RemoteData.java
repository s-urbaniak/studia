/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mpdme.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import mpdme.MpdException;

/**
 *
 * @author sur
 */
public abstract class RemoteData {

    private ByteArrayInputStream byteStream = null;
    protected DataInputStream dataStream = null;

    public void decode(byte[] message) {
        this.byteStream = new ByteArrayInputStream(message);
        this.dataStream = new DataInputStream(this.byteStream);
        this.decode();
    }

    public abstract void decode();

    public void decodeFinished() {
        try {
            this.dataStream.close();
        } catch (IOException ex) {
            throw new MpdException(ex);
        }
    }
}
