package com.miui.gallery.editor.photo.core.imports.text.textstyle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.HostAbility;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;

/* loaded from: classes2.dex */
public class TextEditStyleAdapter extends Adapter<NormalStyleViewHolder> {
    public final Context mContext;
    public HostAbility mHostAbility;
    public DialogStatusData mInitializeData;
    public final LayoutInflater mLayoutInflater;
    public int[] mStyleIcon = {R.drawable.text_edit_style_bold, R.drawable.text_edit_style_stroke, R.drawable.text_edit_style_projection, R.drawable.text_edit_style_align_left, R.drawable.text_edit_style_align_center, R.drawable.text_edit_style_align_right};
    public String[] mTexts;

    public TextEditStyleAdapter(Context context) {
        this.mContext = context;
        this.mTexts = context.getResources().getStringArray(R.array.text_style);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public NormalStyleViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new NormalStyleViewHolder(this.mLayoutInflater.inflate(R.layout.text_edit_dialog_options_style_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(NormalStyleViewHolder normalStyleViewHolder, int i) {
        boolean z;
        int color;
        super.onBindViewHolder((TextEditStyleAdapter) normalStyleViewHolder, i);
        if (i == 0) {
            z = this.mInitializeData.textBold;
        } else if (i == 1) {
            z = this.mInitializeData.isStroke;
        } else if (i == 2) {
            z = this.mInitializeData.textShadow;
        } else {
            z = i == 3 ? this.mInitializeData.textAlignment == AutoLineLayout.TextAlignment.LEFT : !(i == 4 ? this.mInitializeData.textAlignment != AutoLineLayout.TextAlignment.CENTER : !(i == 5 && this.mInitializeData.textAlignment == AutoLineLayout.TextAlignment.RIGHT));
        }
        normalStyleViewHolder.mTextView.setText(this.mTexts[i]);
        normalStyleViewHolder.mStyleImageIv.setBackgroundResource(this.mStyleIcon[i]);
        normalStyleViewHolder.mRootView.setBackgroundResource(R.drawable.text_edit_style_adpater_item_background);
        if (!this.mInitializeData.mIsWatermark || i != 1) {
            normalStyleViewHolder.mRootView.setSelected(z);
            normalStyleViewHolder.mStyleImageIv.setSelected(z);
            TextView textView = normalStyleViewHolder.mStyleNameTv;
            if (z) {
                color = ContextCompat.getColor(this.mContext, R.color.text_edit_dialog_options_style_item_style_name_text_color_highlight);
            } else {
                color = ContextCompat.getColor(this.mContext, R.color.text_edit_dialog_options_style_item_style_name_text_color_normal);
            }
            textView.setTextColor(color);
            return;
        }
        normalStyleViewHolder.mRootView.setSelected(false);
        normalStyleViewHolder.mStyleImageIv.setSelected(false);
        normalStyleViewHolder.mStyleImageIv.getBackground().setAlpha(77);
        normalStyleViewHolder.mStyleNameTv.setTextColor(ContextCompat.getColor(this.mContext, R.color.text_edit_dialog_options_style_item_style_name_text_color_not_enabled));
    }

    public void setDialogStatusData(DialogStatusData dialogStatusData) {
        this.mInitializeData = dialogStatusData;
        if (dialogStatusData != null) {
            notifyDataSetChanged();
        }
    }

    public DialogStatusData getInitializeData() {
        return this.mInitializeData;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, android.view.View.OnClickListener
    public void onClick(View view) {
        super.onClick(view);
        int intValue = ((Integer) view.getTag()).intValue();
        if (intValue != 1) {
            if (intValue == 3) {
                notifyItemChanged(4);
                notifyItemChanged(5);
            } else if (intValue == 4) {
                notifyItemChanged(3);
                notifyItemChanged(5);
            } else if (intValue == 5) {
                notifyItemChanged(3);
                notifyItemChanged(4);
            }
        } else if (this.mInitializeData.isSubstrate) {
            this.mHostAbility.showInnerToast(view.getContext().getResources().getString(R.string.both_stroke_and_substrate_are_not_supported));
        }
        notifyItemChanged(intValue);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        String[] strArr = this.mTexts;
        if (strArr == null) {
            return 0;
        }
        return strArr.length;
    }

    /* loaded from: classes2.dex */
    public static class NormalStyleViewHolder extends RecyclerView.ViewHolder {
        public View mRootView;
        public ImageView mStyleImageIv;
        public TextView mStyleNameTv;
        public TextView mTextView;

        public NormalStyleViewHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mRootView = view;
            this.mTextView = (TextView) view.findViewById(R.id.tv_style_name);
            this.mStyleImageIv = (ImageView) view.findViewById(R.id.iv_style_image);
            this.mStyleNameTv = (TextView) view.findViewById(R.id.tv_style_name);
        }
    }

    public void setHostAbility(HostAbility hostAbility) {
        this.mHostAbility = hostAbility;
    }
}
