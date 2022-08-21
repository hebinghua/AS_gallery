package miuix.animation.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import miuix.animation.internal.AnimConfigUtils;
import miuix.animation.utils.EaseManager;

/* loaded from: classes3.dex */
public class AnimConfigLink {
    public static final AtomicInteger sIdGenerator = new AtomicInteger();
    public final int id = sIdGenerator.getAndIncrement();
    public final List<AnimConfig> configList = new ArrayList();
    public final AnimConfig mHeadConfig = new AnimConfig();

    public static AnimConfigLink linkConfig(AnimConfig... animConfigArr) {
        AnimConfigLink animConfigLink = new AnimConfigLink();
        for (AnimConfig animConfig : animConfigArr) {
            animConfigLink.add(animConfig, new boolean[0]);
        }
        return animConfigLink;
    }

    public void add(AnimConfig animConfig, boolean... zArr) {
        if (animConfig == null || this.configList.contains(animConfig)) {
            return;
        }
        if (zArr.length > 0 && zArr[0]) {
            this.configList.add(new AnimConfig(animConfig));
            return;
        }
        this.configList.add(animConfig);
    }

    public void add(AnimConfigLink animConfigLink, boolean... zArr) {
        if (animConfigLink == null) {
            return;
        }
        for (AnimConfig animConfig : animConfigLink.configList) {
            add(animConfig, zArr);
        }
    }

    public void addTo(AnimConfig animConfig) {
        for (int size = this.configList.size() - 1; size >= 0; size--) {
            AnimConfig animConfig2 = this.configList.get(size);
            animConfig.delay = Math.max(animConfig.delay, animConfig2.delay);
            EaseManager.EaseStyle easeStyle = animConfig.ease;
            EaseManager.EaseStyle easeStyle2 = animConfig2.ease;
            if (easeStyle2 != null && easeStyle2 != AnimConfig.sDefEase) {
                easeStyle = easeStyle2;
            }
            animConfig.setEase(easeStyle);
            animConfig.listeners.addAll(animConfig2.listeners);
            animConfig.flags |= animConfig2.flags;
            animConfig.fromSpeed = AnimConfigUtils.chooseSpeed(animConfig.fromSpeed, animConfig2.fromSpeed);
            animConfig.minDuration = Math.max(animConfig.minDuration, animConfig2.minDuration);
            animConfig.tintMode = Math.max(animConfig.tintMode, animConfig2.tintMode);
            animConfig.addSpecialConfigs(animConfig2);
        }
    }

    public void clear() {
        doClear();
        this.configList.add(this.mHeadConfig);
    }

    public final void doClear() {
        this.configList.clear();
        this.mHeadConfig.clear();
    }

    public AnimConfig getHead() {
        if (this.configList.isEmpty()) {
            this.configList.add(this.mHeadConfig);
        }
        return this.configList.get(0);
    }

    public String toString() {
        return "AnimConfigLink{id = " + this.id + ", configList=" + Arrays.toString(this.configList.toArray()) + '}';
    }
}
