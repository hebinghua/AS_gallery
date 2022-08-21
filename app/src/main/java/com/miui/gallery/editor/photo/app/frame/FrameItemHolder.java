package com.miui.gallery.editor.photo.app.frame;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.FrameData;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class FrameItemHolder extends RecyclerView.ViewHolder {
    public ImageView mIcon;
    public TextView mTextView;

    public FrameItemHolder(Context context, View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mIcon = (ImageView) view.findViewById(R.id.icon);
        this.mTextView = (TextView) view.findViewById(R.id.text);
    }

    public void setFrameData(FrameData frameData) {
        this.mTextView.setText(frameData.toString());
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        int identifier = resources.getIdentifier(frameData.icon, "drawable", "com.miui.gallery");
        if (identifier == 0) {
            DefaultLogger.e("FrameItemHolder", "frame icon not valid : " + frameData.icon);
            return;
        }
        this.mIcon.setImageDrawable(resources.getDrawable(identifier));
        int identifier2 = resources.getIdentifier(frameData.talkbackName, "string", "com.miui.gallery");
        if (identifier2 == 0) {
            return;
        }
        this.mTextView.setContentDescription(resources.getString(identifier2));
    }
}
