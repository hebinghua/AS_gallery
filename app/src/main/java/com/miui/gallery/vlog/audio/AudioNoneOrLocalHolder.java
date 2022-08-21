package com.miui.gallery.vlog.audio;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.entity.AudioData;

/* loaded from: classes2.dex */
public class AudioNoneOrLocalHolder extends RecyclerView.ViewHolder {
    public ImageView mImageView;
    public View mSelectorView;

    public AudioNoneOrLocalHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mImageView = (ImageView) view.findViewById(R$id.img);
        this.mSelectorView = view.findViewById(R$id.selector_view);
    }

    public void setIconPath(AudioData audioData, int i, int i2, boolean z) {
        if (audioData.type.equals("type_local")) {
            if (z) {
                this.mImageView.setImageResource(R$drawable.ic_audio_menu_edit);
            } else {
                this.mImageView.setImageResource(i);
            }
            this.itemView.setSelected(z);
            this.itemView.setContentDescription(this.mImageView.getResources().getString(R$string.vlog_talkback_audio_local));
        } else if (!audioData.type.equals("type_none")) {
        } else {
            this.mImageView.setImageResource(i);
            this.mSelectorView.setVisibility(z ? 0 : 8);
            this.itemView.setContentDescription(this.mImageView.getResources().getString(R$string.vlog_talkback_audio_null));
        }
    }
}
