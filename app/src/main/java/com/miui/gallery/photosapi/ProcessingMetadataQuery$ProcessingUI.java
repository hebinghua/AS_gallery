package com.miui.gallery.photosapi;

/* loaded from: classes2.dex */
public enum ProcessingMetadataQuery$ProcessingUI {
    NONE(0),
    CIRCLE(1);
    
    private final int identifier;

    ProcessingMetadataQuery$ProcessingUI(int i) {
        this.identifier = i;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public static ProcessingMetadataQuery$ProcessingUI fromIdentifier(int i) {
        ProcessingMetadataQuery$ProcessingUI processingMetadataQuery$ProcessingUI = NONE;
        return i == processingMetadataQuery$ProcessingUI.getIdentifier() ? processingMetadataQuery$ProcessingUI : CIRCLE;
    }
}
