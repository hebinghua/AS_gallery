package com.miui.gallery.gallerywidget.common;

/* loaded from: classes2.dex */
public interface IWidgetProviderConfig {
    int getRemoteViewID();

    WidgetSize getWidgetType();

    /* loaded from: classes2.dex */
    public enum WidgetSize {
        SIZE_2_2(1),
        SIZE_4_2(2),
        SIZE_4_4(3),
        SIZE_2_3(4);
        
        private final int value;

        WidgetSize(int i) {
            this.value = i;
        }

        public int getValue() {
            return this.value;
        }
    }
}
