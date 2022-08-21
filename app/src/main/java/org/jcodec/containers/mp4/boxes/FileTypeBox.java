package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;
import org.jcodec.common.JCodecUtil2;
import org.jcodec.common.io.NIOUtils;

@Keep
/* loaded from: classes3.dex */
public class FileTypeBox extends Box {
    private Collection<String> compBrands;
    private String majorBrand;
    private int minorVersion;

    public static String fourcc() {
        return "ftyp";
    }

    public FileTypeBox(Header header) {
        super(header);
        this.compBrands = new LinkedList();
    }

    public static FileTypeBox createFileTypeBox(String str, int i, Collection<String> collection) {
        FileTypeBox fileTypeBox = new FileTypeBox(new Header(fourcc()));
        fileTypeBox.majorBrand = str;
        fileTypeBox.minorVersion = i;
        fileTypeBox.compBrands = collection;
        return fileTypeBox;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void parse(ByteBuffer byteBuffer) {
        String readString;
        this.majorBrand = NIOUtils.readString(byteBuffer, 4);
        this.minorVersion = byteBuffer.getInt();
        while (byteBuffer.hasRemaining() && (readString = NIOUtils.readString(byteBuffer, 4)) != null) {
            this.compBrands.add(readString);
        }
    }

    public String getMajorBrand() {
        return this.majorBrand;
    }

    public Collection<String> getCompBrands() {
        return this.compBrands;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void doWrite(ByteBuffer byteBuffer) {
        byteBuffer.put(JCodecUtil2.asciiString(this.majorBrand));
        byteBuffer.putInt(this.minorVersion);
        for (String str : this.compBrands) {
            byteBuffer.put(JCodecUtil2.asciiString(str));
        }
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public int estimateSize() {
        int i = 13;
        for (String str : this.compBrands) {
            i += JCodecUtil2.asciiString(str).length;
        }
        return i;
    }
}
