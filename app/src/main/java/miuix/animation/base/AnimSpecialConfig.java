package miuix.animation.base;

import miuix.animation.property.FloatProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes3.dex */
public class AnimSpecialConfig extends AnimConfig {
    @Override // miuix.animation.base.AnimConfig
    @Deprecated
    public AnimSpecialConfig getSpecialConfig(String str) {
        return this;
    }

    @Override // miuix.animation.base.AnimConfig
    @Deprecated
    public AnimSpecialConfig queryAndCreateSpecial(String str) {
        return this;
    }

    public AnimSpecialConfig() {
        super(true);
    }

    @Override // miuix.animation.base.AnimConfig
    public AnimConfig setSpecial(String str, EaseManager.EaseStyle easeStyle, float... fArr) {
        super.setSpecial(this, easeStyle, -1L, fArr);
        return this;
    }

    @Override // miuix.animation.base.AnimConfig
    public AnimConfig setSpecial(String str, EaseManager.EaseStyle easeStyle, long j, float... fArr) {
        super.setSpecial(this, easeStyle, j, fArr);
        return this;
    }

    @Override // miuix.animation.base.AnimConfig
    public AnimConfig setSpecial(FloatProperty floatProperty, long j, float... fArr) {
        super.setSpecial(this, (EaseManager.EaseStyle) null, j, fArr);
        return this;
    }

    @Override // miuix.animation.base.AnimConfig
    public AnimConfig setSpecial(FloatProperty floatProperty, EaseManager.EaseStyle easeStyle, float... fArr) {
        super.setSpecial(this, easeStyle, -1L, fArr);
        return this;
    }

    @Override // miuix.animation.base.AnimConfig
    public AnimConfig setSpecial(FloatProperty floatProperty, EaseManager.EaseStyle easeStyle, long j, float... fArr) {
        super.setSpecial(this, easeStyle, j, fArr);
        return this;
    }
}
