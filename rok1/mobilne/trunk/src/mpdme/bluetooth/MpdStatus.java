package mpdme.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import mpdme.MpdException;

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

    public void decode(DataInputStream dataStream) {
        try {
            this.setAlbum(dataStream.readUTF());
            this.setArtist(dataStream.readUTF());
            this.setName(dataStream.readUTF());
            this.setTitle(dataStream.readUTF());
            this.setState(dataStream.readUTF());
            this.setLength(dataStream.readInt());
            this.setTime(dataStream.readInt());
        } catch (IOException ex) {
            throw new MpdException(ex);
        }
    }
}
