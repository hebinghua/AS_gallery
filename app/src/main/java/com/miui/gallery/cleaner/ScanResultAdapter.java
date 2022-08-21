package com.miui.gallery.cleaner;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.glide.BindImageHelper;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ScanResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int mContentLayoutMinHeight;
    public Context mContext;
    public View mHeaderView;
    public List<ScanResult> mDataList = null;
    public final ArraySet<NotifyObserver> mNotifyObservers = new ArraySet<>();
    public final RequestOptions mRequestOptions = GlideOptions.microThumbOf().mo964lock();

    public ScanResultAdapter(Context context) {
        this.mContext = context;
        this.mContentLayoutMinHeight = context.getResources().getDimensionPixelSize(R.dimen.cleaner_item_content_layout_min_height);
    }

    public void updateDataList(List<ScanResult> list) {
        this.mDataList = list;
    }

    public List<ScanResult> getDataList() {
        return this.mDataList;
    }

    public int getDataItemSize() {
        List<ScanResult> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (this.mHeaderView == null || i != 0) {
            ScanResult scanResult = this.mDataList.get(i);
            return (scanResult.getType() != 0 || scanResult.getSize() >= 0) ? 0 : 2;
        }
        return 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getDataItemSize() + (this.mHeaderView == null ? 0 : 1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new HeaderHolder(this.mHeaderView);
        }
        if (i == 2) {
            return new SlimBackupDisableResultHolder(LayoutInflater.from(this.mContext).inflate(R.layout.slim_backup_disable_result_layout, viewGroup, false));
        }
        return new ScanResultHolder(LayoutInflater.from(this.mContext).inflate(R.layout.cleaner_scan_result_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        super.onViewRecycled(viewHolder);
        if (viewHolder instanceof ScanResultHolder) {
            ((ScanResultHolder) viewHolder).unbind();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 0) {
            if (this.mHeaderView != null) {
                i--;
            }
            ((ScanResultHolder) viewHolder).bindData(this.mDataList.get(i), i);
        } else if (itemViewType != 2) {
        } else {
            if (this.mHeaderView != null) {
                i--;
            }
            ((SlimBackupDisableResultHolder) viewHolder).bindData(this.mDataList.get(i), i);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
        if (adapterDataObserver == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized (this.mNotifyObservers) {
            Iterator<NotifyObserver> it = this.mNotifyObservers.iterator();
            while (it.hasNext()) {
                if (it.next().getDataObserver() == adapterDataObserver) {
                    throw new IllegalStateException("Observer " + adapterDataObserver + " is already registered.");
                }
            }
            NotifyObserver notifyObserver = new NotifyObserver(adapterDataObserver);
            this.mNotifyObservers.add(notifyObserver);
            super.registerAdapterDataObserver(notifyObserver);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
        if (adapterDataObserver == null) {
            throw new IllegalArgumentException("The observer is null.");
        }
        synchronized (this.mNotifyObservers) {
            Iterator<NotifyObserver> it = this.mNotifyObservers.iterator();
            while (it.hasNext()) {
                NotifyObserver next = it.next();
                if (next.getDataObserver() == adapterDataObserver) {
                    super.unregisterAdapterDataObserver(next);
                    it.remove();
                }
            }
            throw new IllegalStateException("Observer " + adapterDataObserver + " was not registered.");
        }
    }

    /* loaded from: classes.dex */
    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View view) {
            super(view);
        }
    }

    /* loaded from: classes.dex */
    public class ScanResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mAction;
        public View mContentLayout;
        public ViewGroup.LayoutParams mContentLayoutParams;
        public TextView mDescription;
        public int mDisplayAmount;
        public ImageView[] mImages;
        public ScanResult mScanResult;
        public TextView mSubtitle;
        public TextView mTitle;
        public RelativeLayout.LayoutParams mTitleLayoutParams;

        public ScanResultHolder(View view) {
            super(view);
            TextView textView = (TextView) view.findViewById(R.id.title);
            this.mTitle = textView;
            this.mTitleLayoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
            this.mSubtitle = (TextView) view.findViewById(R.id.subtitle);
            this.mDescription = (TextView) view.findViewById(R.id.description);
            TextView textView2 = (TextView) view.findViewById(R.id.action);
            this.mAction = textView2;
            textView2.setOnClickListener(this);
            view.setOnClickListener(this);
            FolmeUtil.setDefaultTouchAnim(this.mAction, null, false, false, true);
            this.mDisplayAmount = view.getContext().getResources().getInteger(R.integer.cleaner_image_column);
            ImageView[] imageViewArr = new ImageView[10];
            this.mImages = imageViewArr;
            imageViewArr[0] = (ImageView) view.findViewById(R.id.image0);
            this.mImages[1] = (ImageView) view.findViewById(R.id.image1);
            this.mImages[2] = (ImageView) view.findViewById(R.id.image2);
            this.mImages[3] = (ImageView) view.findViewById(R.id.image3);
            this.mImages[4] = (ImageView) view.findViewById(R.id.image4);
            this.mImages[5] = (ImageView) view.findViewById(R.id.image5);
            this.mImages[6] = (ImageView) view.findViewById(R.id.image6);
            this.mImages[7] = (ImageView) view.findViewById(R.id.image7);
            this.mImages[8] = (ImageView) view.findViewById(R.id.image8);
            this.mImages[9] = (ImageView) view.findViewById(R.id.image9);
            View findViewById = view.findViewById(R.id.content_layout);
            this.mContentLayout = findViewById;
            this.mContentLayoutParams = findViewById.getLayoutParams();
        }

        public void bindData(ScanResult scanResult, int i) {
            ScanResult.ResultImage resultImage;
            this.mScanResult = scanResult;
            this.mTitle.setText(scanResult.getTitle());
            this.mSubtitle.setText(scanResult.getMergedSubTitle(ScanResultAdapter.this.mContext));
            if (scanResult.getAction() <= 0) {
                this.mAction.setVisibility(4);
            } else {
                this.mAction.setVisibility(0);
                this.mAction.setText(scanResult.getAction());
            }
            if (TextUtils.isEmpty(ScanResultAdapter.this.mContext.getString(scanResult.getDescription()))) {
                this.mDescription.setVisibility(8);
                RelativeLayout.LayoutParams layoutParams = this.mTitleLayoutParams;
                layoutParams.topMargin = 0;
                layoutParams.addRule(15);
                this.mContentLayoutParams.height = ScanResultAdapter.this.mContentLayoutMinHeight;
            } else {
                this.mDescription.setVisibility(0);
                if (scanResult.getType() == 0) {
                    this.mDescription.setText(ScanResultAdapter.this.mContext.getString(scanResult.getDescription(), 30));
                } else {
                    this.mDescription.setText(scanResult.getDescription());
                }
                this.mTitleLayoutParams.removeRule(15);
                this.mContentLayoutParams.height = -2;
            }
            this.mTitle.setLayoutParams(this.mTitleLayoutParams);
            this.mContentLayout.setLayoutParams(this.mContentLayoutParams);
            ScanResult.ResultImage[] images = scanResult.getImages();
            for (int i2 = 0; i2 < this.mDisplayAmount; i2++) {
                if (images.length > i2 && (resultImage = images[i2]) != null) {
                    this.mImages[i2].setVisibility(0);
                    BindImageHelper.bindImage(resultImage.mPath, CloudUriAdapter.getDownloadUri(resultImage.mId), DownloadType.MICRO, this.mImages[i2], ScanResultAdapter.this.mRequestOptions);
                } else {
                    this.mImages[i2].setVisibility(4);
                }
            }
        }

        public void unbind() {
            ScanResult scanResult = this.mScanResult;
            if (scanResult == null || scanResult.getImages() == null) {
                return;
            }
            ScanResult.ResultImage[] images = this.mScanResult.getImages();
            int i = 0;
            while (true) {
                ImageView[] imageViewArr = this.mImages;
                if (i >= imageViewArr.length) {
                    return;
                }
                if (images.length > i) {
                    BindImageHelper.cancel(imageViewArr[i]);
                }
                i++;
            }
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            this.mScanResult.onClick(ScanResultAdapter.this.mContext);
        }
    }

    /* loaded from: classes.dex */
    public class SlimBackupDisableResultHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mAction;
        public ScanResult mScanResult;

        public SlimBackupDisableResultHolder(View view) {
            super(view);
            TextView textView = (TextView) view.findViewById(R.id.action);
            this.mAction = textView;
            textView.setOnClickListener(this);
            FolmeUtil.setDefaultTouchAnim(this.mAction, null, false, false, true);
        }

        public void bindData(ScanResult scanResult, int i) {
            this.mScanResult = scanResult;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            this.mScanResult.onClick(ScanResultAdapter.this.mContext);
        }
    }

    /* loaded from: classes.dex */
    public class NotifyObserver extends RecyclerView.AdapterDataObserver {
        public RecyclerView.AdapterDataObserver mDataObserver;

        public NotifyObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mDataObserver = adapterDataObserver;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            this.mDataObserver.onChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2) {
            this.mDataObserver.onItemRangeChanged(i + getHeaderSize(), i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2, Object obj) {
            this.mDataObserver.onItemRangeChanged(i + getHeaderSize(), i2, obj);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int i, int i2) {
            this.mDataObserver.onItemRangeInserted(i + getHeaderSize(), i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int i, int i2, int i3) {
            this.mDataObserver.onItemRangeMoved(i, i2, i3);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int i, int i2) {
            this.mDataObserver.onItemRangeRemoved(i + getHeaderSize(), i2);
        }

        public int getHeaderSize() {
            return ScanResultAdapter.this.mHeaderView == null ? 0 : 1;
        }

        public RecyclerView.AdapterDataObserver getDataObserver() {
            return this.mDataObserver;
        }
    }
}
