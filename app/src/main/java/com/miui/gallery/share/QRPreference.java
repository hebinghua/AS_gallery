package com.miui.gallery.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.google.zxing.WriterException;
import com.miui.gallery.R;
import com.zxing.encoding.EncodingHandler;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class QRPreference extends Preference implements View.OnClickListener {
    public WeakReference<Bitmap> mQRCodeRef;
    public View.OnClickListener mRetryListener;
    public Status mStatus;
    public String mTextQR;

    /* loaded from: classes2.dex */
    public enum Status {
        REQUESTING,
        SUCCESS,
        FAILED
    }

    public QRPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public QRPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mStatus = Status.REQUESTING;
        setLayoutResource(R.layout.share_album_qr_code_view);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.findViewById(R.id.retry_btn).setOnClickListener(this);
        update(preferenceViewHolder.itemView);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        View.OnClickListener onClickListener = this.mRetryListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }

    public void setRetryListener(View.OnClickListener onClickListener) {
        this.mRetryListener = onClickListener;
    }

    public void setStatus(Status status) {
        this.mStatus = status;
        notifyChanged();
    }

    public String getQRText() {
        return this.mTextQR;
    }

    public void setQRText(String str) {
        if (!TextUtils.equals(this.mTextQR, str)) {
            this.mTextQR = str;
            this.mQRCodeRef = null;
        }
    }

    public final void update(View view) {
        View findViewById = view.findViewById(R.id.qr_code_error);
        View findViewById2 = view.findViewById(R.id.qr_code_requesting);
        ImageView imageView = (ImageView) view.findViewById(R.id.qr_code_image);
        Status status = this.mStatus;
        if (status == Status.REQUESTING) {
            findViewById2.setVisibility(0);
            findViewById.setVisibility(8);
            imageView.setVisibility(8);
        } else if (status == Status.FAILED) {
            findViewById.setVisibility(0);
            findViewById2.setVisibility(8);
            imageView.setVisibility(8);
        } else if (status != Status.SUCCESS) {
        } else {
            imageView.setVisibility(0);
            findViewById.setVisibility(8);
            findViewById2.setVisibility(8);
            if (TextUtils.isEmpty(this.mTextQR)) {
                imageView.setImageBitmap(null);
                return;
            }
            try {
                WeakReference<Bitmap> weakReference = this.mQRCodeRef;
                Bitmap bitmap = weakReference != null ? weakReference.get() : null;
                if (bitmap == null) {
                    bitmap = EncodingHandler.createQRCode(this.mTextQR, getContext().getResources().getDimensionPixelSize(R.dimen.album_share_qr_size));
                    if (bitmap != null) {
                        this.mQRCodeRef = new WeakReference<>(bitmap);
                    }
                }
                imageView.setImageBitmap(bitmap);
            } catch (WriterException | OutOfMemoryError unused) {
                imageView.setImageBitmap(null);
            }
        }
    }
}
