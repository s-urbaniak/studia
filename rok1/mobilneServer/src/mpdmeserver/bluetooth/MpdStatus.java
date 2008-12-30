package mpdmeserver.bluetooth;

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
    protected void encodeImpl() throws IOException {
        this.dataStream.writeUTF(this.getAlbum());
        this.dataStream.writeUTF(this.getArtist());
        this.dataStream.writeUTF(this.getName());
        this.dataStream.writeUTF(this.getTitle());
        this.dataStream.writeUTF(this.getState());
        this.dataStream.writeInt(this.getLength());
        this.dataStream.writeInt(this.getTime());

        this.encodeFinished();
    }
}
