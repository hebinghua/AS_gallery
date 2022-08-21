package com.miui.gallery.cloudcontrol.strategies;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.uil.ShortCutHelper;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class SlimEntrancesStrategy extends BaseStrategy {
    @SerializedName(CallMethod.RESULT_ENABLE_BOOLEAN)
    private boolean mEnable;
    @SerializedName("entrances")
    private List<Entrance> mEntranceList = new ArrayList();
    public HashMap<String, Entrance> mEntrancesMap;

    public boolean isEnable() {
        return this.mEnable;
    }

    public Entrance getEntrance(String str) {
        return this.mEntrancesMap.get(str);
    }

    /* loaded from: classes.dex */
    public static class Entrance {
        @SerializedName(CallMethod.RESULT_ENABLE_BOOLEAN)
        private boolean mEnable;
        @SerializedName("entrance")
        private String mEntrance;

        public String getEntrance() {
            return this.mEntrance;
        }

        public boolean isEnable() {
            return this.mEnable;
        }
    }

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        HashMap<String, Entrance> hashMap = this.mEntrancesMap;
        if (hashMap == null) {
            this.mEntrancesMap = new HashMap<>();
        } else {
            hashMap.clear();
        }
        if (BaseMiscUtil.isValid(this.mEntranceList)) {
            for (Entrance entrance : this.mEntranceList) {
                if (!TextUtils.isEmpty(entrance.getEntrance())) {
                    this.mEntrancesMap.put(entrance.getEntrance(), entrance);
                }
            }
        }
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (isEnable() && getEntrance("shortcut").isEnable()) {
            Intent intent = new Intent("android.intent.action.VIEW", GalleryContract.Common.URI_CLEANER_PAGE);
            intent.setPackage(sGetAndroidContext.getPackageName());
            intent.putExtra("extra_from_type", 1018);
            ShortCutHelper.add(sGetAndroidContext, new ShortcutInfoCompat.Builder(sGetAndroidContext, "shortcut_slim").setShortLabel(sGetAndroidContext.getResources().getString(R.string.cleaner_title)).setIcon(IconCompat.createFromIcon(Icon.createWithResource(sGetAndroidContext, (int) R.drawable.action_shortcut_slim))).setIntent(intent).build());
            return;
        }
        ShortCutHelper.remove(sGetAndroidContext, "shortcut_slim");
    }
}
