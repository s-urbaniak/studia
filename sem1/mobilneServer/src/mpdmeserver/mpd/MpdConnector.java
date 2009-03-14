package mpdmeserver.mpd;

import mpdmeserver.bluetooth.MpdStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sur
 */
public class MpdConnector {

    public static final String DEFAULT_MPD_HOST = "localhost";
    public static final int DEFAULT_MPD_PORT = 6600;
    private Socket mpdSocket = null;
    private PrintWriter toMpd = null;
    private BufferedReader fromMpd = null;

    public void connect() {
        try {
            this.mpdSocket = new Socket(DEFAULT_MPD_HOST, DEFAULT_MPD_PORT);
            this.toMpd = new PrintWriter(mpdSocket.getOutputStream(), true);
            this.fromMpd = new BufferedReader(new InputStreamReader(mpdSocket.getInputStream()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void disconnect() {
        try {
            this.toMpd.close();
            this.fromMpd.close();
            this.mpdSocket.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public HashMap<String, String> getAnswer() {
        StringBuilder answer = new StringBuilder();
        HashMap<String, String> map = new HashMap();

        try {
            String line = "";
            while (!"OK".equals(line)) {
                line = this.fromMpd.readLine();

                Logger.getLogger(this.getClass().getName()).log(
                        Level.INFO,
                        "mpd: " + line);

                answer.append(line);
                String[] kv = getKeyValue(line);
                map.put(kv[0].toLowerCase(), kv[1]);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return map;
    }

    protected String[] getKeyValue(String from) {
        if (from == null) {
            return new String[]{"", null};
        }

        if (from.contains(": ")) {
            return from.split(": ");
        } else {
            return new String[]{from, null};
        }
    }

    public void play() {
        this.toMpd.println("play");
        getAnswer();
    }

    public void pause() {
        this.toMpd.println("pause");
        getAnswer();
    }

    public MpdStatus status() {
        MpdStatus status = new MpdStatus();

        this.toMpd.println("status");
        HashMap<String, String> answer = getAnswer();

        status.setState(getValue("state", answer));

        String val = (String) answer.get("time");
        if (val != null) {
            String times[] = val.split(":");
            status.setTime(Integer.parseInt(times[0]));
            status.setLength(Integer.parseInt(times[1]));
        }

        this.toMpd.println("currentsong");
        answer = getAnswer();

        status.setArtist(getValue("artist", answer));
        status.setTitle(getValue("title", answer));
        status.setAlbum(getValue("album", answer));
        status.setName(getValue("name", answer));

        return status;
    }

    public String getValue(String key, HashMap<String, String> map) {
        String val = map.get(key);

        if (val == null) {
            val = "";
        }

        return val;
    }

    public void stop() {
        this.toMpd.println("stop");
        getAnswer();
    }

    public void next() {
        this.toMpd.println("next");
        getAnswer();
    }

    public void prev() {
        this.toMpd.println("previous");
        getAnswer();
    }

    private boolean checkAnswer(String answer) {
        return answer.contains("OK MPD");
    }
}
