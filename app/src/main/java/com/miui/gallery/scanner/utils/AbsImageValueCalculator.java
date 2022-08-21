package com.miui.gallery.scanner.utils;

import java.io.IOException;

/* loaded from: classes2.dex */
public abstract class AbsImageValueCalculator extends AbsValueCalculator {
    public abstract String calcExifAperture() throws IOException;

    public abstract String calcExifDateTime(long j, boolean z) throws IOException;

    public abstract String calcExifExposureTime() throws IOException;

    public abstract Integer calcExifFlash() throws IOException;

    public abstract String calcExifFocalLength() throws IOException;

    public abstract String calcExifGpsAltitude() throws IOException;

    public abstract int calcExifGpsAltitudeRef() throws IOException;

    public abstract String calcExifGpsDateStamp(long j, boolean z) throws IOException;

    public abstract String calcExifGpsProcessingMethod() throws IOException;

    public abstract String calcExifGpsTimeStamp(long j, boolean z) throws IOException;

    public abstract String calcExifISO() throws IOException;

    public abstract String calcExifMake() throws IOException;

    public abstract String calcExifModel() throws IOException;

    public abstract int calcExifWhiteBalance() throws IOException;

    public abstract String calcScreenshotsLocation() throws IOException;

    public AbsImageValueCalculator(String str) {
        super(str);
    }
}
