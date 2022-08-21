package com.miui.gallery.signature.dialog.manage;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.miui.gallery.R;
import com.miui.gallery.signature.SignatureColor;
import com.miui.gallery.signature.dialog.manage.SignatureManageAdapter;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SignatureManageAdapter extends Adapter<SignatureViewHolder> {
    public static final ColorStateList TINT_FOR_NIGHT_MODE = new ColorStateList(new int[][]{new int[]{0}}, new int[]{-1});
    public ClickDeleteSignatureListener mClickDeleteSignatureListener;
    public Context mContext;
    public List<SignatureManageModel> mSignatureManageModels;

    /* loaded from: classes2.dex */
    public interface ClickDeleteSignatureListener {
        void onDelete(SignatureManageModel signatureManageModel, int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return i == 0 ? 2 : 3;
    }

    public SignatureManageAdapter(Context context, List<SignatureManageModel> list) {
        this.mContext = context;
        this.mSignatureManageModels = list;
        insertAddSignatureItem();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public SignatureViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SignatureViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.signature_manage_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SignatureViewHolder signatureViewHolder, int i) {
        super.onBindViewHolder((SignatureManageAdapter) signatureViewHolder, i);
        signatureViewHolder.bind(this.mSignatureManageModels.get(i), i == 0 ? 2 : 3, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<SignatureManageModel> list = this.mSignatureManageModels;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public void setSignatureManageModels(List<SignatureManageModel> list) {
        this.mSignatureManageModels = list;
        insertAddSignatureItem();
        notifyDataSetChanged();
    }

    public final void insertAddSignatureItem() {
        if (this.mSignatureManageModels == null) {
            this.mSignatureManageModels = new ArrayList();
        }
        this.mSignatureManageModels.add(0, null);
    }

    public List<SignatureManageModel> getSignatureManageModels() {
        return this.mSignatureManageModels;
    }

    public SignatureManageModel getItemData(int i) {
        List<SignatureManageModel> list = this.mSignatureManageModels;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mSignatureManageModels.get(i);
    }

    /* loaded from: classes2.dex */
    public class SignatureViewHolder extends RecyclerView.ViewHolder {
        public TextView mAddNewSignature;
        public FrameLayout mDeleteLayout;
        public AppCompatImageView mImageView;

        public static /* synthetic */ void $r8$lambda$dc3SaU0qUQHNL5sojFVi_4bwuvI(SignatureViewHolder signatureViewHolder, SignatureManageModel signatureManageModel, int i, View view) {
            signatureViewHolder.lambda$bind$0(signatureManageModel, i, view);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SignatureViewHolder(View view) {
            super(view);
            SignatureManageAdapter.this = r3;
            FolmeUtil.setCustomTouchAnim(view, new AnimParams.Builder().setTint(0.1f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
            this.mImageView = (AppCompatImageView) view.findViewById(R.id.signature_manage_item_display);
            this.mDeleteLayout = (FrameLayout) view.findViewById(R.id.signature_manage_item_delete);
            this.mAddNewSignature = (TextView) view.findViewById(R.id.add_new_signature);
        }

        public void bind(final SignatureManageModel signatureManageModel, int i, final int i2) {
            if (i == 2) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) this.itemView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                this.itemView.setLayoutParams(layoutParams);
            }
            this.mDeleteLayout.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.signature.dialog.manage.SignatureManageAdapter$SignatureViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    SignatureManageAdapter.SignatureViewHolder.$r8$lambda$dc3SaU0qUQHNL5sojFVi_4bwuvI(SignatureManageAdapter.SignatureViewHolder.this, signatureManageModel, i2, view);
                }
            });
            if (i == 2) {
                View view = this.itemView;
                view.setContentDescription(view.getResources().getString(R.string.signature_view_add_signature_String));
                this.mAddNewSignature.setTextColor(SignatureManageAdapter.this.mContext.getResources().getColor(R.color.text_editor_add_new_signature));
                this.mAddNewSignature.setVisibility(0);
                this.mDeleteLayout.setVisibility(8);
                this.mImageView.setVisibility(8);
                return;
            }
            View view2 = this.itemView;
            view2.setContentDescription(this.itemView.getResources().getString(R.string.signature_manage_dialog_title_string) + i2);
            this.mAddNewSignature.setVisibility(8);
            this.mDeleteLayout.setVisibility(0);
            this.mImageView.setVisibility(0);
            this.mImageView.setImageTintMode(PorterDuff.Mode.SRC_IN);
            if (SignatureColor.isDefaultColorWithPath(signatureManageModel.getPath())) {
                this.mImageView.setImageTintList(SignatureManageAdapter.TINT_FOR_NIGHT_MODE);
            } else {
                this.mImageView.setImageTintList(null);
            }
            Glide.with(this.mImageView).mo990load(Scheme.FILE.wrap(signatureManageModel.getPath())).into(this.mImageView);
        }

        public /* synthetic */ void lambda$bind$0(SignatureManageModel signatureManageModel, int i, View view) {
            if (SignatureManageAdapter.this.mClickDeleteSignatureListener != null) {
                SignatureManageAdapter.this.mClickDeleteSignatureListener.onDelete(signatureManageModel, i);
            }
        }
    }

    public void setClickDeleteSignatureListener(ClickDeleteSignatureListener clickDeleteSignatureListener) {
        this.mClickDeleteSignatureListener = clickDeleteSignatureListener;
    }
}
