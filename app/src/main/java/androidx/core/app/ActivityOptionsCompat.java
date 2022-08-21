package androidx.core.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.core.util.Pair;

/* loaded from: classes.dex */
public class ActivityOptionsCompat {
    public Bundle toBundle() {
        return null;
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, Pair<View, String>... pairArr) {
        if (Build.VERSION.SDK_INT >= 21) {
            android.util.Pair[] pairArr2 = null;
            if (pairArr != null) {
                pairArr2 = new android.util.Pair[pairArr.length];
                for (int i = 0; i < pairArr.length; i++) {
                    pairArr2[i] = android.util.Pair.create(pairArr[i].first, pairArr[i].second);
                }
            }
            return new ActivityOptionsCompatImpl(ActivityOptions.makeSceneTransitionAnimation(activity, pairArr2));
        }
        return new ActivityOptionsCompat();
    }

    /* loaded from: classes.dex */
    public static class ActivityOptionsCompatImpl extends ActivityOptionsCompat {
        public final ActivityOptions mActivityOptions;

        public ActivityOptionsCompatImpl(ActivityOptions activityOptions) {
            this.mActivityOptions = activityOptions;
        }

        @Override // androidx.core.app.ActivityOptionsCompat
        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }
    }
}
