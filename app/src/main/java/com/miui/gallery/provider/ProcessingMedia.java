package com.miui.gallery.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.photosapi.PhotosOemApi;
import com.miui.gallery.photosapi.ProcessingMetadataQuery$ProcessingUI;
import com.miui.gallery.photosapi.ProcessingMetadataQuery$ProgressStatus;

/* loaded from: classes2.dex */
public class ProcessingMedia {
    public final boolean isUsingGaussianForTemp;
    public final long mMediaStoreId;
    public final int mMediaType;
    public final String mPath;
    public final ProcessingMetadata mProcessingMetadata;
    public final ProcessingMetadataQuery$ProcessingUI mProcessingUI;
    public final String mSpecialTypeId;
    public final Uri mUri;

    public ProcessingMedia(Uri uri, long j, String str, String str2, int i, boolean z, ProcessingMetadataQuery$ProcessingUI processingMetadataQuery$ProcessingUI, ProcessingMetadata processingMetadata) {
        this.mUri = uri;
        this.mMediaStoreId = j;
        this.mPath = str;
        this.mSpecialTypeId = str2;
        this.mMediaType = i;
        this.mProcessingMetadata = processingMetadata;
        this.isUsingGaussianForTemp = z;
        this.mProcessingUI = processingMetadataQuery$ProcessingUI;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public long getMediaStoreId() {
        return this.mMediaStoreId;
    }

    public String getPath() {
        return this.mPath;
    }

    public boolean isUsingGaussianForTemp() {
        return this.isUsingGaussianForTemp;
    }

    public ProcessingMetadataQuery$ProcessingUI getProcessingUI() {
        return this.mProcessingUI;
    }

    public ProcessingMetadata getProcessingMetadata() {
        return this.mProcessingMetadata;
    }

    /* loaded from: classes2.dex */
    public static final class Factory {
        public final Context context;

        public Factory(Context context) {
            this.context = context;
        }

        public ProcessingMedia build(long j, String str, int i, boolean z, ProcessingMetadataQuery$ProcessingUI processingMetadataQuery$ProcessingUI, ProcessingMetadata processingMetadata) {
            Uri mediaStoreUri;
            if (i == 0) {
                mediaStoreUri = getProcessingUri(j);
            } else {
                mediaStoreUri = getMediaStoreUri(j, i);
            }
            return new ProcessingMedia(mediaStoreUri, j, str, null, i, z, processingMetadataQuery$ProcessingUI, processingMetadata);
        }

        public final Uri getMediaStoreUri(long j, int i) {
            if (i == 3) {
                return MediaStore.Video.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(String.valueOf(j)).build();
            }
            return MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(String.valueOf(j)).build();
        }

        public final Uri getProcessingUri(long j) {
            return PhotosOemApi.getQueryProcessingUri(this.context, j);
        }
    }

    public String toString() {
        return "ProcessingMedia{mUri=" + this.mUri + ", mMediaStoreId=" + this.mMediaStoreId + ", mPath='" + this.mPath + CoreConstants.SINGLE_QUOTE_CHAR + ", mSpecialTypeId='" + this.mSpecialTypeId + CoreConstants.SINGLE_QUOTE_CHAR + ", mMediaType=" + this.mMediaType + ", mProcessingMetadata=" + this.mProcessingMetadata + '}';
    }

    /* loaded from: classes2.dex */
    public static final class ProcessingMetadata {
        public final int mProgressPercentage;
        public final ProcessingMetadataQuery$ProgressStatus mProgressStatus;

        public ProcessingMetadata(ProcessingMetadataQuery$ProgressStatus processingMetadataQuery$ProgressStatus, int i) {
            this.mProgressStatus = processingMetadataQuery$ProgressStatus;
            this.mProgressPercentage = i;
        }

        public ProcessingMetadataQuery$ProgressStatus getProgressStatus() {
            return this.mProgressStatus;
        }

        public int getProgressPercentage() {
            return this.mProgressPercentage;
        }

        public String toString() {
            return "ProcessingMetadata{mProgressStatus=" + this.mProgressStatus + ", mProgressPercentage=" + this.mProgressPercentage + '}';
        }
    }
}
