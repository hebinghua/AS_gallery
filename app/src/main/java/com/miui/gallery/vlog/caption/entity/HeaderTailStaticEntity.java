package com.miui.gallery.vlog.caption.entity;

import java.util.List;

/* loaded from: classes2.dex */
public class HeaderTailStaticEntity {
    private List<AnimListBean> animList;
    private int height;
    private int width;

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int i) {
        this.height = i;
    }

    public List<AnimListBean> getAnimList() {
        return this.animList;
    }

    public void setAnimList(List<AnimListBean> list) {
        this.animList = list;
    }

    /* loaded from: classes2.dex */
    public static class AnimListBean {
        private String alpha;
        private boolean bold;
        private String color;
        private String evaporate;
        private float letterSpacing;
        private float posX;
        private float posY;
        private String rotate;
        private String scale;
        private String skewX;
        private String text;
        private float textSize;
        private boolean thrutext;
        private String transX;
        private String transY;
        private int type;
        private boolean underline;

        public String getEvaporate() {
            return this.evaporate;
        }

        public void setEvaporate(String str) {
            this.evaporate = str;
        }

        public float getLetterSpacing() {
            return this.letterSpacing;
        }

        public void setLetterSpacing(float f) {
            this.letterSpacing = f;
        }

        public String getText() {
            return this.text;
        }

        public void setText(String str) {
            this.text = str;
        }

        public float getPosX() {
            return this.posX;
        }

        public void setPosX(float f) {
            this.posX = f;
        }

        public float getPosY() {
            return this.posY;
        }

        public void setPosY(float f) {
            this.posY = f;
        }

        public boolean isBold() {
            return this.bold;
        }

        public void setBold(boolean z) {
            this.bold = z;
        }

        public boolean isThrutext() {
            return this.thrutext;
        }

        public void setThrutext(boolean z) {
            this.thrutext = z;
        }

        public boolean isUnderline() {
            return this.underline;
        }

        public void setUnderline(boolean z) {
            this.underline = z;
        }

        public String getSkewX() {
            return this.skewX;
        }

        public void setSkewX(String str) {
            this.skewX = str;
        }

        public String getColor() {
            return this.color;
        }

        public void setColor(String str) {
            this.color = str;
        }

        public float getTextSize() {
            return this.textSize;
        }

        public void setTextSize(float f) {
            this.textSize = f;
        }

        public String getTransX() {
            return this.transX;
        }

        public void setTransX(String str) {
            this.transX = str;
        }

        public String getTransY() {
            return this.transY;
        }

        public void setTransY(String str) {
            this.transY = str;
        }

        public String getRotate() {
            return this.rotate;
        }

        public void setRotate(String str) {
            this.rotate = str;
        }

        public String getAlpha() {
            return this.alpha;
        }

        public void setAlpha(String str) {
            this.alpha = str;
        }

        public String getScale() {
            return this.scale;
        }

        public void setScale(String str) {
            this.scale = str;
        }

        public int getType() {
            return this.type;
        }

        public void setType(int i) {
            this.type = i;
        }
    }
}
