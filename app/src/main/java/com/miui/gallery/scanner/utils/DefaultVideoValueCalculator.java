package com.miui.gallery.scanner.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.scanner.core.ScanContracts$SQL;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.VideoAttrsReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class DefaultVideoValueCalculator extends AbsVideoValueCalculator {
    public VideoAttrsReader videoAttrsReader;

    public static /* synthetic */ Object $r8$lambda$75iqQsAfV2ZYsIFIjz1BcDczoVw(Map map, Cursor cursor) {
        return lambda$ensureLocation$0(map, cursor);
    }

    public DefaultVideoValueCalculator(String str) {
        super(str);
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public String calcSha1() {
        return FileUtils.getSha1(this.path);
    }

    @Override // com.miui.gallery.scanner.utils.AbsVideoValueCalculator
    public long calcDuration() throws IOException {
        return getVideoAttrsReader().getDuration() / 1000;
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifImageWidth() throws IOException {
        return getVideoAttrsReader().getVideoWidth();
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifImageHeight() throws IOException {
        return getVideoAttrsReader().getVideoHeight();
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public int calcExifOrientation() throws IOException {
        return getVideoAttrsReader().getOrientation();
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public long calcDateTaken(long j, long j2, boolean z) throws IOException {
        return j2 > 0 ? j2 : getVideoAttrsReader().getDateTaken();
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public long calcSpecialTypeFlags() {
        return SpecialTypeMediaUtils.parseFlagsForVideo(this.path);
    }

    @Override // com.miui.gallery.scanner.utils.AbsValueCalculator
    public Map<String, String> calcExifGpsLocation() throws IOException {
        boolean z;
        HashMap hashMap = new HashMap();
        double latitude = getVideoAttrsReader().getLatitude();
        double longitude = getVideoAttrsReader().getLongitude();
        boolean z2 = true;
        if (!BaseMiscUtil.doubleEquals(latitude, SearchStatUtils.POW)) {
            hashMap.put("GPSLatitude", LocationUtil.convertDoubleToLaLon(latitude));
            z = true;
        } else {
            z = false;
        }
        if (!BaseMiscUtil.doubleEquals(longitude, SearchStatUtils.POW)) {
            hashMap.put("GPSLongitude", LocationUtil.convertDoubleToLaLon(longitude));
        } else {
            z2 = false;
        }
        if (!z || !z2) {
            hashMap.putAll(ensureLocation(GalleryApp.sGetAndroidContext()));
        }
        return hashMap;
    }

    public final VideoAttrsReader getVideoAttrsReader() throws IOException {
        if (this.videoAttrsReader == null) {
            this.videoAttrsReader = VideoAttrsReader.read(this.path);
        }
        return this.videoAttrsReader;
    }

    public final Map<String, String> ensureLocation(Context context) {
        final HashMap hashMap = new HashMap();
        SafeDBUtil.safeQuery(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, ScanContracts$SQL.ENSURE_INFO_PROJECTION_VIDEO, "_data=?", new String[]{this.path}, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.scanner.utils.DefaultVideoValueCalculator$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public final Object mo1808handle(Cursor cursor) {
                return DefaultVideoValueCalculator.$r8$lambda$75iqQsAfV2ZYsIFIjz1BcDczoVw(hashMap, cursor);
            }
        });
        return hashMap;
    }

    public static /* synthetic */ Object lambda$ensureLocation$0(Map map, Cursor cursor) {
        if (cursor != null && cursor.moveToNext()) {
            double d = cursor.getDouble(0);
            double d2 = cursor.getDouble(1);
            if (!BaseMiscUtil.doubleEquals(d, SearchStatUtils.POW)) {
                map.put("GPSLatitude", LocationUtil.convertDoubleToLaLon(d));
            }
            if (!BaseMiscUtil.doubleEquals(d2, SearchStatUtils.POW)) {
                map.put("GPSLongitude", LocationUtil.convertDoubleToLaLon(d2));
            }
        }
        return null;
    }
}
