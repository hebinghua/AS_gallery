package com.xiaomi.milab.videosdk.utils;

import android.graphics.Point;
import ch.qos.logback.core.CoreConstants;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class TextAnim {
    public ArrayList<Anim> animList;
    public long duration;
    public int height;
    public int width;

    /* loaded from: classes3.dex */
    public class Anim {
        public String alpha;
        public boolean bold;
        public String color;
        public List<Cue> cues;
        public String evaporate;
        public boolean isChangeSize;
        public float letterSpacing;
        public List<Point> pathList;
        public float posX;
        public float posY;
        public String rotate;
        public String scale;
        public float skewX;
        public String text;
        public float textSize;
        public boolean thrutext;
        public String transX;
        public String transY;
        public int type;
        public boolean underline;

        public Anim() {
        }
    }

    /* loaded from: classes3.dex */
    public class Cue {
        public long end;
        public String evaporate;
        public long index;
        public boolean isChangeSize;
        public long start;
        public String text;
        public int type;

        public Cue() {
        }

        public String toString() {
            return "Cue{index=" + this.index + ", start=" + this.start + ", end=" + this.end + ", text='" + this.text + CoreConstants.SINGLE_QUOTE_CHAR + ", type=" + this.type + ", evaporate='" + this.evaporate + CoreConstants.SINGLE_QUOTE_CHAR + ", isChangeSize=" + this.isChangeSize + '}';
        }
    }

    /* loaded from: classes3.dex */
    public enum Type {
        OTHER(-1),
        TEXT(0),
        LINE(1),
        PATH(2),
        PNG(3),
        CUE(4);
        
        private int value;

        Type(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }

        public static Type valueOf(int i) {
            if (i != 0) {
                if (i == 1) {
                    return LINE;
                }
                if (i == 2) {
                    return PATH;
                }
                if (i == 3) {
                    return PNG;
                }
                if (i == 4) {
                    return CUE;
                }
                return OTHER;
            }
            return TEXT;
        }
    }
}
