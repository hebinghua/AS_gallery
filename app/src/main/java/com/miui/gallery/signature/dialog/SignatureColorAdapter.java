package com.miui.gallery.signature.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.signature.SignatureColor;
import com.miui.gallery.signature.widget.SelectColorView;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import java.util.List;

/* loaded from: classes2.dex */
public class SignatureColorAdapter extends RecyclerView.Adapter<SignatureColorViewHolder> {
    public Context mContext;
    public OnClickListener mOnClickListener;
    public int mSelectIndex;
    public List<ColorData> mSignatureColors;

    /* loaded from: classes2.dex */
    public interface OnClickListener {
        void onClick(SignatureColor signatureColor);
    }

    public static /* synthetic */ void $r8$lambda$rS4kc0jhR1MAgLNihR2LoiG3WIo(SignatureColorAdapter signatureColorAdapter, int i, ColorData colorData, View view) {
        signatureColorAdapter.lambda$onBindViewHolder$0(i, colorData, view);
    }

    public SignatureColorAdapter(Context context, List<ColorData> list) {
        this.mContext = context;
        this.mSignatureColors = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public SignatureColorViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SignatureColorViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.signature_color_item_view, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<ColorData> list = this.mSignatureColors;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SignatureColorViewHolder signatureColorViewHolder, final int i) {
        final ColorData colorData = this.mSignatureColors.get(i);
        signatureColorViewHolder.mSelectColorView.init(this.mContext.getResources().getColor(colorData.innerCircleColor.mColorId), colorData.outSelectColor, colorData.innerCircleRadius, colorData.arcStrokeWidth);
        signatureColorViewHolder.mSelectColorView.setChecked(this.mSelectIndex == i);
        signatureColorViewHolder.mSelectColorView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.signature.dialog.SignatureColorAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SignatureColorAdapter.$r8$lambda$rS4kc0jhR1MAgLNihR2LoiG3WIo(SignatureColorAdapter.this, i, colorData, view);
            }
        });
        signatureColorViewHolder.mSelectColorView.setContentDescription(colorData.innerCircleColor.mTalkbackName);
    }

    public /* synthetic */ void lambda$onBindViewHolder$0(int i, ColorData colorData, View view) {
        setSelection(i);
        OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(colorData.innerCircleColor);
        }
    }

    public void setSelection(int i) {
        this.mSelectIndex = i;
        notifyDataSetChanged();
    }

    public void setSelectionWithColor(SignatureColor signatureColor) {
        for (int i = 0; i < this.mSignatureColors.size(); i++) {
            if (this.mSignatureColors.get(i).innerCircleColor == signatureColor) {
                this.mSelectIndex = i;
                notifyDataSetChanged();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class SignatureColorViewHolder extends RecyclerView.ViewHolder {
        public SelectColorView mSelectColorView;

        public SignatureColorViewHolder(View view) {
            super(view);
            this.mSelectColorView = (SelectColorView) view.findViewById(R.id.color_view);
            FolmeUtil.setCustomTouchAnim(this.mSelectColorView, new AnimParams.Builder().setTint(0.1f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
        }
    }

    /* loaded from: classes2.dex */
    public static class ColorData {
        public float arcStrokeWidth;
        public SignatureColor innerCircleColor;
        public float innerCircleRadius;
        public int outSelectColor;

        public ColorData(SignatureColor signatureColor, int i, float f, float f2, String str) {
            this.innerCircleColor = signatureColor;
            this.outSelectColor = i;
            this.innerCircleRadius = f;
            this.arcStrokeWidth = f2;
            signatureColor.mTalkbackName = str;
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
