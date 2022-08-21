package com.miui.gallery.photosapi;

/* loaded from: classes2.dex */
public enum ProcessingMetadataQuery$ProgressStatus {
    INDETERMINATE(1),
    DETERMINATE(2);
    
    private final int identifier;

    ProcessingMetadataQuery$ProgressStatus(int i) {
        this.identifier = i;
    }

    public int getIdentifier() {
        return this.identifier;
    }

    public static ProcessingMetadataQuery$ProgressStatus fromIdentifier(int i) {
        ProcessingMetadataQuery$ProgressStatus processingMetadataQuery$ProgressStatus = DETERMINATE;
        return i == processingMetadataQuery$ProgressStatus.getIdentifier() ? processingMetadataQuery$ProgressStatus : INDETERMINATE;
    }
}
