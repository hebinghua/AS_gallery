package com.miui.gallery.cloudcontrol.strategies;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy;
import com.miui.gallery.scanner.utils.StatHelper;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/* loaded from: classes.dex */
public class HiddenAlbumsStrategy extends BaseStrategy {
    public LazyValue<Void, Pattern[]> mHideAlbumPatterns = new LazyValue<Void, Pattern[]>() { // from class: com.miui.gallery.cloudcontrol.strategies.HiddenAlbumsStrategy.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Pattern[] mo1272onInit(Void r6) {
            Pattern[] patternArr = new Pattern[HiddenAlbumsStrategy.this.mPatterns.size()];
            int i = 0;
            for (String str : HiddenAlbumsStrategy.this.mPatterns) {
                int i2 = i + 1;
                try {
                    patternArr[i] = Pattern.compile(str, 2);
                    i = i2;
                } catch (PatternSyntaxException e) {
                    DefaultLogger.e("HiddenAlbumsStrategy", "Invalid hide album regex pattern: %s", e.getPattern());
                    i = i2 + 1;
                    patternArr[i2] = null;
                }
            }
            return patternArr;
        }
    };
    @SerializedName("non_hidden_path_prefix")
    private List<String> mNonHiddenPathPrefix;
    @SerializedName("patterns")
    private List<String> mPatterns;

    public List<String> getNonHiddenPathPrefix() {
        return this.mNonHiddenPathPrefix;
    }

    public String toString() {
        return "HiddenAlbumsStrategy{mPatterns=" + this.mPatterns + ", mNonHiddenPathPrefix=" + this.mNonHiddenPathPrefix + '}';
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        if (BaseMiscUtil.isValid(this.mPatterns)) {
            ArrayList arrayList = new ArrayList();
            for (String str : this.mPatterns) {
                if (!TextUtils.isEmpty(str)) {
                    arrayList.add(str);
                }
            }
            if (arrayList.size() < this.mPatterns.size()) {
                this.mPatterns.clear();
                this.mPatterns.addAll(arrayList);
            }
        }
        if (BaseMiscUtil.isValid(this.mNonHiddenPathPrefix)) {
            ArrayList arrayList2 = new ArrayList();
            for (String str2 : this.mNonHiddenPathPrefix) {
                if (!TextUtils.isEmpty(str2)) {
                    arrayList2.add(str2);
                }
            }
            if (arrayList2.size() >= this.mNonHiddenPathPrefix.size()) {
                return;
            }
            this.mNonHiddenPathPrefix.clear();
            this.mNonHiddenPathPrefix.addAll(arrayList2);
        }
    }

    public Pattern[] getHideAlbumPatterns() {
        return this.mHideAlbumPatterns.get(null);
    }

    public boolean isInHideList(String str) {
        if (!TextUtils.isEmpty(str) && !GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH.equals(str)) {
            int length = str.length();
            String str2 = str;
            do {
                str2 = str2.substring(0, length);
                AlbumsStrategy.Album albumByPath = CloudControlStrategyHelper.getAlbumByPath(str2);
                if (albumByPath != null && albumByPath.getAttributes() != null && albumByPath.getAttributes().isHide()) {
                    return true;
                }
                length = str2.lastIndexOf(File.separatorChar);
            } while (length != -1);
            if (!str.contains(File.separator)) {
                return false;
            }
            List<String> nonHiddenPathPrefix = getNonHiddenPathPrefix();
            if (BaseMiscUtil.isValid(nonHiddenPathPrefix)) {
                for (String str3 : nonHiddenPathPrefix) {
                    if (str.toLowerCase().startsWith(str3.toLowerCase())) {
                        return false;
                    }
                }
            }
            Pattern[] hideAlbumPatterns = getHideAlbumPatterns();
            if (hideAlbumPatterns != null && hideAlbumPatterns.length > 0) {
                for (Pattern pattern : hideAlbumPatterns) {
                    if (pattern != null && pattern.matcher(str).find()) {
                        StatHelper.recordHiddenAlbum(str);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
