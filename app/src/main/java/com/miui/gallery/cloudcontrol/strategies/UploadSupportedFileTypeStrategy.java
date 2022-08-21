package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;
import com.miui.gallery.cloudcontrol.Merger;
import com.miui.gallery.util.LazyValue;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class UploadSupportedFileTypeStrategy extends BaseStrategy {
    public static final LazyValue<Void, Merger<UploadSupportedFileTypeStrategy>> SUPPORTED_TYPE_MERGER = new LazyValue<Void, Merger<UploadSupportedFileTypeStrategy>>() { // from class: com.miui.gallery.cloudcontrol.strategies.UploadSupportedFileTypeStrategy.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Merger<UploadSupportedFileTypeStrategy> mo1272onInit(Void r1) {
            return new Merger<UploadSupportedFileTypeStrategy>() { // from class: com.miui.gallery.cloudcontrol.strategies.UploadSupportedFileTypeStrategy.1.1
                @Override // com.miui.gallery.cloudcontrol.Merger
                public UploadSupportedFileTypeStrategy merge(UploadSupportedFileTypeStrategy uploadSupportedFileTypeStrategy, UploadSupportedFileTypeStrategy uploadSupportedFileTypeStrategy2) {
                    UploadSupportedFileTypeStrategy uploadSupportedFileTypeStrategy3 = new UploadSupportedFileTypeStrategy();
                    List mergedList = getMergedList(uploadSupportedFileTypeStrategy.mImageFileTypes, uploadSupportedFileTypeStrategy2.mImageFileTypes);
                    if (mergedList != null) {
                        uploadSupportedFileTypeStrategy3.mImageFileTypes = new ArrayList(mergedList);
                    }
                    List mergedList2 = getMergedList(uploadSupportedFileTypeStrategy.mVideoFileTypes, uploadSupportedFileTypeStrategy2.mVideoFileTypes);
                    if (mergedList2 != null) {
                        uploadSupportedFileTypeStrategy3.mVideoFileTypes = new ArrayList(mergedList2);
                    }
                    uploadSupportedFileTypeStrategy3.doAdditionalProcessing();
                    return uploadSupportedFileTypeStrategy3;
                }
            };
        }

        public final List<Object> getMergedList(List<Object> list, List<Object> list2) {
            return (list == null || (list2 != null && list.size() <= list2.size())) ? list2 : list;
        }
    };
    @SerializedName("image")
    private List<Object> mImageFileTypes;
    @SerializedName("video")
    private List<Object> mVideoFileTypes;
}
