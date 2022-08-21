package com.miui.gallery.glide.load.data;

import android.text.TextUtils;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.boxes.MCoverBox;
import org.jcodec.containers.mp4.boxes.NodeBox;
import org.jcodec.containers.mp4.boxes.UdtaBox;

/* loaded from: classes2.dex */
public class VideoBoundCoverFetcher implements IThumbFetcher<String, BoundCover> {
    @Override // com.miui.gallery.glide.load.data.IThumbFetcher
    public BoundCover load(String str) throws IOException {
        byte[] parseCoverData = parseCoverData(str);
        if (parseCoverData != null) {
            return BoundCover.obtain(parseCoverData);
        }
        return null;
    }

    public static byte[] parseCoverData(String str) throws IOException {
        String mediaFilePath;
        Scheme ofUri = Scheme.ofUri(str);
        Scheme scheme = Scheme.FILE;
        if (scheme == ofUri) {
            mediaFilePath = scheme.crop(str);
        } else {
            mediaFilePath = Scheme.CONTENT == ofUri ? MediaStoreUtils.getMediaFilePath(str) : null;
        }
        if (TextUtils.isEmpty(mediaFilePath)) {
            throw new IllegalArgumentException("empty path");
        }
        long currentTimeMillis = System.currentTimeMillis();
        UdtaBox parseUdta = MP4Util.parseUdta(new File(mediaFilePath));
        if (parseUdta == null) {
            throw new IllegalArgumentException("UdtaBox is null");
        }
        MCoverBox mCoverBox = (MCoverBox) NodeBox.findFirstPath(parseUdta, MCoverBox.class, new String[]{MCoverBox.fourcc()});
        if (mCoverBox == null || mCoverBox.getData() == null) {
            throw new IllegalArgumentException("MCoverBox is null");
        }
        DefaultLogger.v("VideoBoundCoverFetcher", "parse [%s] cover cost [%d] ms.", mediaFilePath, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return mCoverBox.getData();
    }
}
