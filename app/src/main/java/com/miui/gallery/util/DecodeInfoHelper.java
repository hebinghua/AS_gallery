package com.miui.gallery.util;

import android.content.ContentUris;
import android.net.Uri;
import com.miui.gallery.util.DecodeInfoHelper;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/* loaded from: classes2.dex */
public class DecodeInfoHelper {
    public final List<DecodeInfo> mInfoList;

    /* renamed from: $r8$lambda$Liflo_TVQO-wxGumBekIES4rwx4 */
    public static /* synthetic */ boolean m1676$r8$lambda$Liflo_TVQOwxGumBekIES4rwx4(long j, DecodeInfo decodeInfo, DecodeInfo decodeInfo2) {
        return lambda$put$0(j, decodeInfo, decodeInfo2);
    }

    /* renamed from: $r8$lambda$dHZNR6eEr6xNo-guOrRLT0rfDvs */
    public static /* synthetic */ boolean m1677$r8$lambda$dHZNR6eEr6xNoguOrRLT0rfDvs(long j, Uri uri, DecodeInfo decodeInfo) {
        return lambda$get$1(j, uri, decodeInfo);
    }

    /* loaded from: classes2.dex */
    public static final class Singleton {
        public static final DecodeInfoHelper sInstance = new DecodeInfoHelper();
    }

    public static DecodeInfoHelper getInstance() {
        return Singleton.sInstance;
    }

    public DecodeInfoHelper() {
        this.mInfoList = new LinkedList();
    }

    public void put(final DecodeInfo decodeInfo) {
        synchronized (this) {
            final long currentTimeMillis = System.currentTimeMillis() / 1000;
            this.mInfoList.removeIf(new Predicate() { // from class: com.miui.gallery.util.DecodeInfoHelper$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DecodeInfoHelper.m1676$r8$lambda$Liflo_TVQOwxGumBekIES4rwx4(currentTimeMillis, decodeInfo, (DecodeInfoHelper.DecodeInfo) obj);
                }
            });
            this.mInfoList.add(decodeInfo);
        }
    }

    public static /* synthetic */ boolean lambda$put$0(long j, DecodeInfo decodeInfo, DecodeInfo decodeInfo2) {
        if (!decodeInfo2.isValid(j)) {
            return true;
        }
        return (decodeInfo2.match(decodeInfo.fileUri) || decodeInfo2.match(decodeInfo.mediaUri)) && decodeInfo.lastModified >= decodeInfo2.lastModified;
    }

    public DecodeInfo get(final Uri uri) {
        DecodeInfo orElse;
        synchronized (this) {
            final long currentTimeMillis = System.currentTimeMillis() / 1000;
            orElse = this.mInfoList.stream().filter(new Predicate() { // from class: com.miui.gallery.util.DecodeInfoHelper$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return DecodeInfoHelper.m1677$r8$lambda$dHZNR6eEr6xNoguOrRLT0rfDvs(currentTimeMillis, uri, (DecodeInfoHelper.DecodeInfo) obj);
                }
            }).findFirst().orElse(null);
        }
        return orElse;
    }

    public static /* synthetic */ boolean lambda$get$1(long j, Uri uri, DecodeInfo decodeInfo) {
        return decodeInfo.isValid(j) && decodeInfo.match(uri);
    }

    /* loaded from: classes2.dex */
    public static class DecodeInfo {
        public Uri fileUri;
        public int height;
        public long lastModified;
        public Uri mediaUri;
        public int width;

        public boolean isValid(long j) {
            return j - this.lastModified < 10;
        }

        public boolean match(Uri uri) {
            if (Objects.equals(uri, this.fileUri)) {
                return true;
            }
            return isMediaUri(uri) && isMediaUri(this.mediaUri) && ContentUris.parseId(uri) == ContentUris.parseId(this.mediaUri);
        }

        public static boolean isMediaUri(Uri uri) {
            return uri != null && Scheme.ofUri(uri.toString()) == Scheme.CONTENT && "media".equals(uri.getAuthority());
        }
    }
}
