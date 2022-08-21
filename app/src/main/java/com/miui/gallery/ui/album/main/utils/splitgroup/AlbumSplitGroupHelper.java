package com.miui.gallery.ui.album.main.utils.splitgroup;

import android.text.TextUtils;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.ExtraSourceProvider;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import java.math.BigDecimal;
import java.util.Locale;

/* loaded from: classes2.dex */
public class AlbumSplitGroupHelper {
    public volatile ISplitGroupMode mSplitGroupMode;

    public AlbumSplitGroupHelper() {
    }

    public static AlbumSplitGroupHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final AlbumSplitGroupHelper INSTANCE = new AlbumSplitGroupHelper();
    }

    public static ISplitGroupMode getSplitGroupMode() {
        AlbumSplitGroupHelper albumSplitGroupHelper = getInstance();
        if (albumSplitGroupHelper.mSplitGroupMode == null) {
            synchronized (albumSplitGroupHelper) {
                if (albumSplitGroupHelper.mSplitGroupMode == null) {
                    albumSplitGroupHelper.mSplitGroupMode = AlbumPageConfig.getInstance().getComponent(null).getSplitGroupMode();
                }
            }
        }
        return albumSplitGroupHelper.mSplitGroupMode;
    }

    public static double getSortPosition(String str) {
        if (TextUtils.isEmpty(str)) {
            return SearchStatUtils.POW;
        }
        if (str.contains("|")) {
            return Double.parseDouble(str.substring(0, str.indexOf("|")));
        }
        return Double.parseDouble(str);
    }

    public static double getSortPosition(BaseViewBean baseViewBean) {
        Object source = baseViewBean.getSource();
        return getSortPosition(source instanceof Album ? ((Album) source).getAlbumSortInfo() : GalleryPreferences.Album.getFixedAlbumSortInfo(baseViewBean.getId()));
    }

    public static String packSortInfo(double d, String str) {
        return String.format(Locale.US, "%s|%s", new BigDecimal(d).toPlainString(), str);
    }

    public static boolean isInValidGroup(String str) {
        return TextUtils.equals(str, "unknow");
    }

    public static Album getAlbumSource(BaseViewBean baseViewBean) {
        Object source;
        if (baseViewBean instanceof ExtraSourceProvider) {
            source = ((ExtraSourceProvider) baseViewBean).mo1601provider();
        } else if (!(baseViewBean.getSource() instanceof Album)) {
            return null;
        } else {
            source = baseViewBean.getSource();
        }
        return (Album) source;
    }
}
