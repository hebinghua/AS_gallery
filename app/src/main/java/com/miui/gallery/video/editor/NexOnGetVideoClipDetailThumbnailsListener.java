package com.miui.gallery.video.editor;

import android.graphics.Bitmap;
import com.miui.gallery.video.editor.VideoEditor;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class NexOnGetVideoClipDetailThumbnailsListener extends nexClip.OnGetVideoClipDetailThumbnailsListener {
    public VideoEditor.OnGetVideoThumbnailListener listener;
    public List<VideoThumbnail> thumbnails;

    public NexOnGetVideoClipDetailThumbnailsListener(VideoEditor.OnGetVideoThumbnailListener onGetVideoThumbnailListener) {
        this.listener = onGetVideoThumbnailListener;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexClip.OnGetVideoClipDetailThumbnailsListener
    public void onGetDetailThumbnailResult(int i, Bitmap bitmap, int i2, int i3, int i4) {
        if (i == nexClip.OnGetVideoClipDetailThumbnailsListener.kEvent_Ok) {
            if (this.thumbnails == null) {
                this.thumbnails = new ArrayList(i3);
            }
            this.thumbnails.add(new VideoThumbnail(Bitmap.createBitmap(bitmap), i4));
        } else if (i == nexClip.OnGetVideoClipDetailThumbnailsListener.kEvent_Completed) {
            this.listener.onGetVideoThumbnailCompleted(this.thumbnails);
        } else {
            this.listener.onGetVideoThumbnailCompleted(null);
        }
    }
}
