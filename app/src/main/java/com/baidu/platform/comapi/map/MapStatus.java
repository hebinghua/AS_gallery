package com.baidu.platform.comapi.map;

import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.io.Serializable;

/* loaded from: classes.dex */
public class MapStatus implements Serializable {
    public int animationTime;
    public int animationType;
    public boolean bOverlookSpringback;
    public boolean bfpp;
    public double centerPtX;
    public double centerPtY;
    public double centerPtZ;
    public GeoBound geoRound;
    public int hasAnimation;
    public boolean isBirdEye;
    public float level;
    public int minOverlooking;
    public int overlooking;
    public String panoId;
    public float roadOffsetX;
    public float roadOffsetY;
    public int rotation;
    public int streetExt;
    public float streetIndicateAngle;
    public WinRound winRound;
    public float xOffset;
    public float yOffset;

    /* loaded from: classes.dex */
    public static class GeoBound implements Serializable {
        public long left = 0;
        public long right = 0;
        public long top = 0;
        public long bottom = 0;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof GeoBound)) {
                return false;
            }
            GeoBound geoBound = (GeoBound) obj;
            return this.bottom == geoBound.bottom && this.left == geoBound.left && this.right == geoBound.right && this.top == geoBound.top;
        }

        public int hashCode() {
            long j = this.bottom;
            long j2 = this.left;
            long j3 = this.right;
            long j4 = this.top;
            return ((((((((int) (j ^ (j >>> 32))) + 31) * 31) + ((int) (j2 ^ (j2 >>> 32)))) * 31) + ((int) (j3 ^ (j3 >>> 32)))) * 31) + ((int) ((j4 >>> 32) ^ j4));
        }
    }

    /* loaded from: classes.dex */
    public static class WinRound implements Serializable {
        public int left = 0;
        public int right = 0;
        public int top = 0;
        public int bottom = 0;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof WinRound)) {
                return false;
            }
            WinRound winRound = (WinRound) obj;
            return this.bottom == winRound.bottom && this.left == winRound.left && this.right == winRound.right && this.top == winRound.top;
        }

        public int hashCode() {
            return ((((((this.bottom + 31) * 31) + this.left) * 31) + this.right) * 31) + this.top;
        }
    }

    public MapStatus() {
        this.animationType = 0;
        this.level = -1.0f;
        this.rotation = -1;
        this.overlooking = -1;
        this.centerPtX = -1.0d;
        this.centerPtY = -1.0d;
        this.centerPtZ = SearchStatUtils.POW;
        this.xOffset = 0.0f;
        this.yOffset = 0.0f;
        this.winRound = new WinRound();
        this.geoRound = new GeoBound();
        this.bfpp = false;
        this.panoId = "";
        this.streetIndicateAngle = 0.0f;
        this.isBirdEye = false;
        this.streetExt = 0;
        this.roadOffsetX = 0.0f;
        this.roadOffsetY = 0.0f;
        this.bOverlookSpringback = false;
        this.minOverlooking = -1;
    }

    public MapStatus(MapStatus mapStatus) {
        this.animationType = 0;
        this.level = mapStatus.level;
        this.rotation = mapStatus.rotation;
        this.overlooking = mapStatus.overlooking;
        this.centerPtX = mapStatus.centerPtX;
        this.centerPtY = mapStatus.centerPtY;
        this.centerPtZ = mapStatus.centerPtZ;
        this.xOffset = mapStatus.xOffset;
        this.yOffset = mapStatus.yOffset;
        this.winRound = mapStatus.winRound;
        this.geoRound = mapStatus.geoRound;
        this.bfpp = mapStatus.bfpp;
        this.panoId = mapStatus.panoId;
        this.streetIndicateAngle = mapStatus.streetIndicateAngle;
        this.isBirdEye = mapStatus.isBirdEye;
        this.streetExt = mapStatus.streetExt;
        this.roadOffsetX = mapStatus.roadOffsetX;
        this.roadOffsetY = mapStatus.roadOffsetY;
        this.bOverlookSpringback = mapStatus.bOverlookSpringback;
        this.minOverlooking = mapStatus.minOverlooking;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MapStatus)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        MapStatus mapStatus = (MapStatus) obj;
        if (this.centerPtX != mapStatus.centerPtX || this.centerPtY != mapStatus.centerPtY || this.centerPtZ != mapStatus.centerPtZ || this.bfpp != mapStatus.bfpp) {
            return false;
        }
        GeoBound geoBound = this.geoRound;
        if (geoBound == null) {
            if (mapStatus.geoRound != null) {
                return false;
            }
        } else if (!geoBound.equals(mapStatus.geoRound)) {
            return false;
        }
        if (Float.floatToIntBits(this.level) != Float.floatToIntBits(mapStatus.level) || this.overlooking != mapStatus.overlooking || this.rotation != mapStatus.rotation || this.yOffset != mapStatus.yOffset || this.xOffset != mapStatus.xOffset) {
            return false;
        }
        WinRound winRound = this.winRound;
        WinRound winRound2 = mapStatus.winRound;
        if (winRound == null) {
            if (winRound2 != null) {
                return false;
            }
        } else if (!winRound.equals(winRound2)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        double d = (((((((this.centerPtX + 31.0d) * 31.0d) + this.centerPtY) * 31.0d) + this.centerPtZ) * 31.0d) + (this.bfpp ? 1.0d : 0.0d)) * 31.0d;
        GeoBound geoBound = this.geoRound;
        int i = 0;
        double hashCode = (((((((d + (geoBound == null ? 0 : geoBound.hashCode())) * 31.0d) + Float.floatToIntBits(this.level)) * 31.0d) + this.overlooking) * 31.0d) + this.rotation) * 31.0d;
        WinRound winRound = this.winRound;
        if (winRound != null) {
            i = winRound.hashCode();
        }
        return (int) (hashCode + i);
    }

    public String toString() {
        return "MapStatus{level=" + this.level + ", rotation=" + this.rotation + ", overlooking=" + this.overlooking + ", centerPtX=" + this.centerPtX + ", centerPtY=" + this.centerPtY + ", centerPtZ=" + this.centerPtZ + ", winRound=" + this.winRound + ", geoRound=" + this.geoRound + ", xOffset=" + this.xOffset + ", yOffset=" + this.yOffset + ", bfpp=" + this.bfpp + ", panoId='" + this.panoId + CoreConstants.SINGLE_QUOTE_CHAR + ", streetIndicateAngle=" + this.streetIndicateAngle + ", isBirdEye=" + this.isBirdEye + ", streetExt=" + this.streetExt + ", roadOffsetX=" + this.roadOffsetX + ", roadOffsetY=" + this.roadOffsetY + '}';
    }
}
