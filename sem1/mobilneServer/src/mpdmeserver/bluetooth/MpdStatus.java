package mpdmeserver.bluetooth;

import java.io.DataOutputStream;
import java.io.IOException;

public class MpdStatus extends RemoteData {

    private String title;
    private String name;
    private String artist;
    private String album;
    private String state;
    private int time;
    private int length;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void encodeImpl(DataOutputStream dataStream) throws IOException {
        dataStream.writeUTF(this.getAlbum());
        dataStream.writeUTF(this.getArtist());
        dataStream.writeUTF(this.getName());
        dataStream.writeUTF(this.getTitle());
        dataStream.writeUTF(this.getState());
        dataStream.writeInt(this.getLength());
        dataStream.writeInt(this.getTime());
    }
}
