package org.jcodec.containers.mp4;

import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.Header;
import org.jcodec.containers.mp4.boxes.MetaBox;
import org.jcodec.containers.mp4.boxes.UdtaBox;

/* loaded from: classes3.dex */
public class MP4Util {

    /* loaded from: classes3.dex */
    public static class Atom {
        public Header header;
        public long offset;

        public Atom(Header header, long j) {
            this.header = header;
            this.offset = j;
        }

        public Header getHeader() {
            return this.header;
        }

        public Box parseBox(SeekableByteChannel seekableByteChannel) throws IOException {
            seekableByteChannel.setPosition(this.offset + this.header.headerSize());
            return BoxUtil.parseBox(NIOUtils.fetchFromChannel(seekableByteChannel, (int) this.header.getBodySize()), this.header, BoxFactory.getDefault());
        }
    }

    public static MetaBox parseMeta(File file) throws IOException {
        FileChannelWrapper fileChannelWrapper;
        try {
            fileChannelWrapper = NIOUtils.readableChannel(file);
            try {
                MetaBox metaBox = (MetaBox) parseMoovChildBox(fileChannelWrapper, MetaBox.fourcc());
                if (fileChannelWrapper != null) {
                    fileChannelWrapper.close();
                }
                return metaBox;
            } catch (Throwable th) {
                th = th;
                if (fileChannelWrapper != null) {
                    fileChannelWrapper.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileChannelWrapper = null;
        }
    }

    public static UdtaBox parseUdta(File file) throws IOException {
        FileChannelWrapper fileChannelWrapper;
        try {
            fileChannelWrapper = NIOUtils.readableChannel(file);
            try {
                UdtaBox udtaBox = (UdtaBox) parseMoovChildBox(fileChannelWrapper, UdtaBox.fourcc());
                if (fileChannelWrapper != null) {
                    fileChannelWrapper.close();
                }
                return udtaBox;
            } catch (Throwable th) {
                th = th;
                if (fileChannelWrapper != null) {
                    fileChannelWrapper.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileChannelWrapper = null;
        }
    }

    public static <T extends Box> T parseMoovChildBox(SeekableByteChannel seekableByteChannel, String str) throws IOException {
        Atom findFirstAtom = findFirstAtom("moov", seekableByteChannel, 0L, seekableByteChannel.size());
        if (findFirstAtom == null) {
            return null;
        }
        long headerSize = findFirstAtom.getHeader().headerSize();
        Atom findFirstAtom2 = findFirstAtom(str, seekableByteChannel, findFirstAtom.offset + headerSize, findFirstAtom.getHeader().getSize() - headerSize);
        if (findFirstAtom2 == null) {
            return null;
        }
        if (findFirstAtom2.getHeader().getBodySize() >= 3145728) {
            DefaultLogger.w("jcodec", "box[%s] body size %d is too large.", str, Long.valueOf(findFirstAtom2.getHeader().getBodySize()));
            return null;
        }
        return (T) findFirstAtom2.parseBox(seekableByteChannel);
    }

    public static Atom findFirstAtom(String str, SeekableByteChannel seekableByteChannel, long j, long j2) throws IOException {
        seekableByteChannel.setPosition(j);
        long j3 = j;
        while (j3 < seekableByteChannel.size() && j3 < j + j2) {
            seekableByteChannel.setPosition(j3);
            Header read = Header.read(NIOUtils.fetchFromChannel(seekableByteChannel, 16));
            if (read == null) {
                return null;
            }
            if (str.equals(read.getFourcc())) {
                return new Atom(read, j3);
            }
            j3 += read.getSize();
        }
        return null;
    }
}
