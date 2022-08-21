package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.video.compress.Resolution;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import miuix.appcompat.app.AlertDialog;
import miuix.internal.util.AnimHelper;

/* loaded from: classes2.dex */
public class VideoCompressDialogFragment extends GalleryDialogFragment {
    public OnCompressListener mCompressListener;
    public AlertDialog mDialog;
    public ResolutionSelectorAdapter mListAdapter;
    public String mSubTitle;
    public long mVideoSize;
    public List<Resolution> mDatas = new ArrayList();
    public DialogInterface.OnClickListener mItemClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.VideoCompressDialogFragment.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (VideoCompressDialogFragment.this.mCompressListener == null) {
                return;
            }
            Resolution resolution = (Resolution) VideoCompressDialogFragment.this.mDatas.get(i);
            HashMap hashMap = new HashMap();
            hashMap.put("resolution", String.format(Locale.US, "%dx%d", Integer.valueOf(resolution.videoWidth), Integer.valueOf(resolution.videoHeight)));
            SamplingStatHelper.recordCountEvent("video", "video_compress_after", hashMap);
            VideoCompressDialogFragment.this.mCompressListener.onResolutionSelect(resolution.videoWidth, resolution.videoHeight);
        }
    };

    /* loaded from: classes2.dex */
    public interface OnCompressListener {
        void onResolutionSelect(int i, int i2);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mListAdapter = new ResolutionSelectorAdapter(getActivity());
        if (bundle != null) {
            removeSelf();
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.video_compress_dialog_title, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.tv_subtitle)).setText(getSubTitle());
        AlertDialog create = new AlertDialog.Builder(getActivity()).setAdapter(this.mListAdapter, this.mItemClickListener).setCustomTitle(inflate).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
        this.mDialog = create;
        return create;
    }

    public void setDatas(List<Resolution> list) {
        this.mDatas = list;
    }

    public void setVideoSize(long j) {
        this.mVideoSize = j;
    }

    public void setSubTitle(String str) {
        this.mSubTitle = str;
    }

    public void setOnCompressListener(OnCompressListener onCompressListener) {
        this.mCompressListener = onCompressListener;
    }

    public final String getSubTitle() {
        return this.mSubTitle;
    }

    public final void removeSelf() {
        if (isAdded()) {
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    /* loaded from: classes2.dex */
    public class ResolutionSelectorAdapter extends BaseAdapter {
        public Context context;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public ResolutionSelectorAdapter(Context context) {
            this.context = context;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (VideoCompressDialogFragment.this.mDatas == null) {
                return 0;
            }
            return VideoCompressDialogFragment.this.mDatas.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return VideoCompressDialogFragment.this.mDatas.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(this.context).inflate(R.layout.video_compress_selector_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.titleView = (TextView) view.findViewById(R.id.title);
                viewHolder.sizeView = (TextView) view.findViewById(R.id.size);
                view.setTag(viewHolder);
                AnimHelper.addPressAnimWithBg(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.titleView.setText(VideoCompressDialogFragment.this.getContext().getResources().getString(((Resolution) VideoCompressDialogFragment.this.mDatas.get(i)).title));
            viewHolder.sizeView.setText(VideoCompressDialogFragment.this.getContext().getResources().getString(((Resolution) VideoCompressDialogFragment.this.mDatas.get(i)).subTitle, FormatUtil.formatFileSize(VideoCompressDialogFragment.this.getContext(), ((float) VideoCompressDialogFragment.this.mVideoSize) * ((Resolution) VideoCompressDialogFragment.this.mDatas.get(i)).ratio)));
            return view;
        }

        /* loaded from: classes2.dex */
        public class ViewHolder {
            public TextView sizeView;
            public TextView titleView;

            public ViewHolder() {
            }
        }
    }
}
