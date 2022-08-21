package com.airbnb.lottie;

import androidx.collection.ArraySet;
import androidx.core.util.Pair;
import com.airbnb.lottie.utils.MeanCalculator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public class PerformanceTracker {
    public boolean enabled = false;
    public final Set<FrameListener> frameListeners = new ArraySet();
    public final Map<String, MeanCalculator> layerRenderTimes = new HashMap();
    public final Comparator<Pair<String, Float>> floatComparator = new Comparator<Pair<String, Float>>() { // from class: com.airbnb.lottie.PerformanceTracker.1
        @Override // java.util.Comparator
        public int compare(Pair<String, Float> pair, Pair<String, Float> pair2) {
            float floatValue = pair.second.floatValue();
            float floatValue2 = pair2.second.floatValue();
            if (floatValue2 > floatValue) {
                return 1;
            }
            return floatValue > floatValue2 ? -1 : 0;
        }
    };

    /* loaded from: classes.dex */
    public interface FrameListener {
        void onFrameRendered(float f);
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }

    public void recordRenderTime(String str, float f) {
        if (!this.enabled) {
            return;
        }
        MeanCalculator meanCalculator = this.layerRenderTimes.get(str);
        if (meanCalculator == null) {
            meanCalculator = new MeanCalculator();
            this.layerRenderTimes.put(str, meanCalculator);
        }
        meanCalculator.add(f);
        if (!str.equals("__container")) {
            return;
        }
        for (FrameListener frameListener : this.frameListeners) {
            frameListener.onFrameRendered(f);
        }
    }
}
