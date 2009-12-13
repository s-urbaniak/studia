package org.urbanet.rtp.protocol;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.urbanet.rtp.protocol.beans.RtspPacket;
import org.urbanet.rtp.protocol.beans.RtspSession;

/**
 * A poor man's unit test for rtsp session logic ;-)
 * 
 * @author sur
 */
public class RtspSourceTest {
    private RtspSource rtspSource;

    public static class DummyRtspSource extends RtspSource {
        @Override
        protected RtspPacket doCommand(String fullCommand) throws IOException {
            RtspPacket packet = new RtspPacket();

            File contentFile = new File("describe_content.txt");
            String content = FileUtils.readFileToString(contentFile);

            packet.setContent(content);
            return packet;
        }

        public DummyRtspSource() {
            super("", 1, "", 2);
        }

    }

    public RtspSource getRtspSource() {
        return rtspSource;
    }

    public void setRtspSource(RtspSource rtspSource) {
        this.rtspSource = rtspSource;
    }

    public void testDoDescribe() {
        try {
            rtspSource.doDescribe();
            RtspSession session = rtspSource.getSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DummyRtspSource source = new DummyRtspSource();
        RtspSourceTest test = new RtspSourceTest();
        test.setRtspSource(source);
        test.testDoDescribe();
    }
}