package com.miui.gallery.vlog.caption.entity;

/* loaded from: classes2.dex */
public class HeaderTailPermanentEntity {
    private long inTime;
    private boolean isDynamic;
    private String mixerName;
    private String mixerParam;
    private String name;
    private long outTime;
    private String param;

    public HeaderTailPermanentEntity(HeaderTailPermanentEntity headerTailPermanentEntity) {
        if (headerTailPermanentEntity != null) {
            this.inTime = headerTailPermanentEntity.inTime;
            this.outTime = headerTailPermanentEntity.outTime;
            this.name = headerTailPermanentEntity.name;
            this.param = headerTailPermanentEntity.param;
            this.mixerName = headerTailPermanentEntity.mixerName;
            this.mixerParam = headerTailPermanentEntity.mixerParam;
            this.isDynamic = headerTailPermanentEntity.isDynamic;
        }
    }

    public void setEntity(HeaderTailPermanentEntity headerTailPermanentEntity) {
        if (headerTailPermanentEntity == null) {
            return;
        }
        this.inTime = headerTailPermanentEntity.inTime;
        this.outTime = headerTailPermanentEntity.outTime;
        this.name = headerTailPermanentEntity.name;
        this.param = headerTailPermanentEntity.param;
        this.mixerName = headerTailPermanentEntity.mixerName;
        this.mixerParam = headerTailPermanentEntity.mixerParam;
        this.isDynamic = headerTailPermanentEntity.isDynamic;
    }

    public long getInTime() {
        return this.inTime;
    }

    public void setInTime(long j) {
        this.inTime = j;
    }

    public long getOutTime() {
        return this.outTime;
    }

    public void setOutTime(long j) {
        this.outTime = j;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getParam() {
        return this.param;
    }

    public void setParam(String str) {
        this.param = str;
    }

    public String getMixerName() {
        return this.mixerName;
    }

    public void setMixerName(String str) {
        this.mixerName = str;
    }

    public String getMixerParam() {
        return this.mixerParam;
    }

    public void setMixerParam(String str) {
        this.mixerParam = str;
    }

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public void setDynamic(boolean z) {
        this.isDynamic = z;
    }
}
