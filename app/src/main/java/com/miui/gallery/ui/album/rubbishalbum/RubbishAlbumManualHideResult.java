package com.miui.gallery.ui.album.rubbishalbum;

import android.text.TextUtils;
import android.util.Pair;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class RubbishAlbumManualHideResult {
    public List<Pair<String, Boolean>> results = new LinkedList();

    public void add(String str, Boolean bool) {
        this.results.add(new Pair<>(str, bool));
    }

    public List<String> getSuccessfulPaths() {
        return getResult(true);
    }

    public List<String> getFailedPaths() {
        return getResult(false);
    }

    public final List<String> getResult(boolean z) {
        ArrayList arrayList = new ArrayList(this.results.size());
        for (Pair<String, Boolean> pair : this.results) {
            if (((Boolean) pair.second).booleanValue() == z) {
                arrayList.add((String) pair.first);
            }
        }
        return arrayList;
    }

    public String toString() {
        return "successfulPaths: " + TextUtils.join(",", getSuccessfulPaths()) + ",failedPaths: " + TextUtils.join(",", getFailedPaths());
    }
}
