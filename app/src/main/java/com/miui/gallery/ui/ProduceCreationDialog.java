package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.CreationStrategy;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.util.ArtStillEntranceUtils;
import com.miui.gallery.util.ArtStillLibraryLoaderHelper;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.IDPhotoEntranceUtils;
import com.miui.gallery.util.IDPhotoLibraryLoaderHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.MagicMattingEntranceUtils;
import com.miui.gallery.util.MagicMattingLibraryLoaderHelper;
import com.miui.gallery.util.MovieLibraryLoaderHelper;
import com.miui.gallery.util.PhotoMovieEntranceUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.VideoPostDownloadManager;
import com.miui.gallery.util.VideoPostEntranceUtils;
import com.miui.gallery.util.VideoPostLibraryLoaderHelper;
import com.miui.gallery.util.VlogLibraryLoaderHelper;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MacaronInstaller;
import com.miui.gallery.util.market.PrintInstaller;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import java.util.LinkedList;
import java.util.List;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class ProduceCreationDialog extends GalleryDialogFragment {
    public View mContentView;
    public AlertDialog mDialog;
    public CreationAdapter mMultiCreationAdapter;
    public GalleryRecyclerView mMultiRecyclerView;
    public TextView mMultiText;
    public OnOperationSelectedListener mOnOperationSelectedListener;
    public CreationAdapter mSingleCreationAdapter;
    public GalleryRecyclerView mSingleRecyclerView;
    public TextView mSingleText;
    public TextView mTitle;
    public VideoPostDownloadManager.DownloadStateListener mVideoPostDownloadListener = new VideoPostDownloadManager.DownloadStateListener() { // from class: com.miui.gallery.ui.ProduceCreationDialog.1
        @Override // com.miui.gallery.util.VideoPostDownloadManager.DownloadStateListener
        public void onDownloading() {
            ProduceCreationDialog.this.mSingleCreationAdapter.setRemainWhenClick(3, true);
            ProduceCreationDialog.this.mSingleCreationAdapter.setInformation(3, GalleryApp.sGetAndroidContext().getString(R.string.loading));
            ProduceCreationDialog.this.mSingleCreationAdapter.notifyDataSetChanged();
        }

        @Override // com.miui.gallery.util.VideoPostDownloadManager.DownloadStateListener
        public void onFinish(boolean z) {
            DefaultLogger.d("ProduceCreationDialog", "VideoPostDownload onFinish: " + z);
            ProduceCreationDialog.this.mSingleCreationAdapter.setRemainWhenClick(3, false);
            ProduceCreationDialog.this.mSingleCreationAdapter.setInformation(3, null);
            ProduceCreationDialog.this.mSingleCreationAdapter.notifyDataSetChanged();
        }
    };
    public LibraryLoaderHelper.DownloadStateListener mMagicMattingDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.ProduceCreationDialog.2
        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onDownloading() {
            ProduceCreationDialog.this.mSingleCreationAdapter.setRemainWhenClick(0, true);
            ProduceCreationDialog.this.mSingleCreationAdapter.setInformation(0, GalleryApp.sGetAndroidContext().getString(R.string.loading));
            ProduceCreationDialog.this.mSingleCreationAdapter.notifyDataSetChanged();
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onFinish(boolean z, int i) {
            ProduceCreationDialog.this.mSingleCreationAdapter.setRemainWhenClick(0, false);
            ProduceCreationDialog.this.mSingleCreationAdapter.setInformation(0, null);
            ProduceCreationDialog.this.mSingleCreationAdapter.notifyDataSetChanged();
        }
    };
    public LibraryLoaderHelper.DownloadStateListener mIDPhotoDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.ProduceCreationDialog.3
        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onDownloading() {
            ProduceCreationDialog.this.mSingleCreationAdapter.setRemainWhenClick(1, true);
            ProduceCreationDialog.this.mSingleCreationAdapter.setInformation(1, GalleryApp.sGetAndroidContext().getString(R.string.loading));
            ProduceCreationDialog.this.mSingleCreationAdapter.notifyDataSetChanged();
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onFinish(boolean z, int i) {
            ProduceCreationDialog.this.mSingleCreationAdapter.setRemainWhenClick(1, false);
            ProduceCreationDialog.this.mSingleCreationAdapter.setInformation(1, null);
            ProduceCreationDialog.this.mSingleCreationAdapter.notifyDataSetChanged();
        }
    };
    public LibraryLoaderHelper.DownloadStateListener mArtStillDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.ProduceCreationDialog.4
        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onDownloading() {
            ProduceCreationDialog.this.mSingleCreationAdapter.setRemainWhenClick(2, true);
            ProduceCreationDialog.this.mSingleCreationAdapter.setInformation(2, GalleryApp.sGetAndroidContext().getString(R.string.loading));
            ProduceCreationDialog.this.mSingleCreationAdapter.notifyDataSetChanged();
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onFinish(boolean z, int i) {
            ProduceCreationDialog.this.mSingleCreationAdapter.setRemainWhenClick(2, false);
            ProduceCreationDialog.this.mSingleCreationAdapter.setInformation(2, null);
            ProduceCreationDialog.this.mSingleCreationAdapter.notifyDataSetChanged();
        }
    };
    public PrintInstaller.InstallStateListener mInstallStateListener = new PrintInstaller.InstallStateListener() { // from class: com.miui.gallery.ui.ProduceCreationDialog.5
        @Override // com.miui.gallery.util.market.PrintInstaller.InstallStateListener
        public void onInstallLimited() {
        }

        @Override // com.miui.gallery.util.market.PrintInstaller.InstallStateListener
        public void onInstalling() {
            ProduceCreationDialog.this.mMultiCreationAdapter.setRemainWhenClick(8, true);
            ProduceCreationDialog.this.mMultiCreationAdapter.setInformation(8, GalleryApp.sGetAndroidContext().getString(R.string.loading));
            ProduceCreationDialog.this.mMultiCreationAdapter.notifyDataSetChanged();
        }

        @Override // com.miui.gallery.util.market.PrintInstaller.InstallStateListener
        public void onFinish(boolean z, int i, int i2) {
            ProduceCreationDialog.this.mMultiCreationAdapter.setRemainWhenClick(8, false);
            ProduceCreationDialog.this.mMultiCreationAdapter.setInformation(8, null);
            ProduceCreationDialog.this.mMultiCreationAdapter.notifyDataSetChanged();
        }
    };
    public LibraryLoaderHelper.DownloadStateListener mVlogDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.ProduceCreationDialog.6
        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onDownloading() {
            ProduceCreationDialog.this.mMultiCreationAdapter.setRemainWhenClick(7, true);
            ProduceCreationDialog.this.mMultiCreationAdapter.setInformation(7, GalleryApp.sGetAndroidContext().getString(R.string.loading));
            ProduceCreationDialog.this.mMultiCreationAdapter.notifyDataSetChanged();
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onFinish(boolean z, int i) {
            ProduceCreationDialog.this.mMultiCreationAdapter.setRemainWhenClick(7, false);
            ProduceCreationDialog.this.mMultiCreationAdapter.setInformation(7, null);
            ProduceCreationDialog.this.mMultiCreationAdapter.notifyDataSetChanged();
        }
    };
    public LibraryLoaderHelper.DownloadStateListener mDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.ui.ProduceCreationDialog.7
        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onDownloading() {
            ProduceCreationDialog.this.mMultiCreationAdapter.setRemainWhenClick(6, true);
            ProduceCreationDialog.this.mMultiCreationAdapter.setInformation(6, GalleryApp.sGetAndroidContext().getString(R.string.loading));
            ProduceCreationDialog.this.mMultiCreationAdapter.notifyDataSetChanged();
        }

        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
        public void onFinish(boolean z, int i) {
            ProduceCreationDialog.this.mMultiCreationAdapter.setRemainWhenClick(6, false);
            ProduceCreationDialog.this.mMultiCreationAdapter.setInformation(6, null);
            ProduceCreationDialog.this.mMultiCreationAdapter.notifyDataSetChanged();
        }
    };
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.ui.ProduceCreationDialog.8
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            if (recyclerView.getId() == R.id.multi_recycler_view) {
                if (i >= ProduceCreationDialog.this.mMultiCreationModels.size() || ProduceCreationDialog.this.mOnOperationSelectedListener == null) {
                    return true;
                }
                CreationModel creationModel = (CreationModel) ProduceCreationDialog.this.mMultiCreationModels.get(i);
                if (creationModel.isRemainWhenClick || !ProduceCreationDialog.this.mOnOperationSelectedListener.onOperationSelected(creationModel.creationId)) {
                    return true;
                }
                ProduceCreationDialog.this.dismissSafely();
                return true;
            } else if (recyclerView.getId() != R.id.single_recycler_view || i >= ProduceCreationDialog.this.mSingleCreationModels.size() || ProduceCreationDialog.this.mOnOperationSelectedListener == null) {
                return true;
            } else {
                CreationModel creationModel2 = (CreationModel) ProduceCreationDialog.this.mSingleCreationModels.get(i);
                if (creationModel2.isRemainWhenClick || !ProduceCreationDialog.this.mOnOperationSelectedListener.onOperationSelected(creationModel2.creationId)) {
                    return true;
                }
                ProduceCreationDialog.this.dismissSafely();
                return true;
            }
        }
    };
    public List<CreationModel> mSingleCreationModels = new LinkedList();
    public List<CreationModel> mMultiCreationModels = new LinkedList();

    /* loaded from: classes2.dex */
    public interface OnOperationSelectedListener {
        boolean onOperationSelected(int i);
    }

    /* loaded from: classes2.dex */
    public static class CreationModel {
        public int creationId;
        public int iconRes;
        public String informationString;
        public boolean isRemainWhenClick = false;
        public int titleResourceId;

        public CreationModel(int i, int i2, int i3) {
            this.creationId = i;
            this.titleResourceId = i2;
            this.iconRes = i3;
        }
    }

    public ProduceCreationDialog() {
        CreationStrategy creationStrategy = CloudControlStrategyHelper.getCreationStrategy();
        if (MagicMattingEntranceUtils.isAvailable()) {
            this.mSingleCreationModels.add(new CreationModel(0, R.string.home_menu_magic_matting, R.drawable.assistant_page_recommend));
        }
        if (IDPhotoEntranceUtils.isAvailable()) {
            this.mSingleCreationModels.add(new CreationModel(1, R.string.home_menu_id_photo, R.drawable.assistant_page_idphoto));
        }
        if (ArtStillEntranceUtils.isAvailable()) {
            this.mSingleCreationModels.add(new CreationModel(2, R.string.home_menu_art_still, R.drawable.assistant_page_artstill));
        }
        if (VideoPostEntranceUtils.isAvailable()) {
            this.mSingleCreationModels.add(new CreationModel(3, R.string.home_menu_video_post, R.drawable.assistant_page_videopost));
        }
        if (MacaronInstaller.isFunctionOn()) {
            this.mSingleCreationModels.add(new CreationModel(4, R.string.home_menu_macarons, R.drawable.assitant_page_macaron));
        }
        if (creationStrategy.isCollageEntryEnable()) {
            this.mMultiCreationModels.add(new CreationModel(5, R.string.home_menu_collage, R.drawable.assistant_page_collage));
        }
        if (creationStrategy.isPhotoMovieEntryEnable() && PhotoMovieEntranceUtils.isDeviceSupportPhotoMovie()) {
            this.mMultiCreationModels.add(new CreationModel(6, R.string.home_menu_photo_movie, R.drawable.assistant_page_photomovie));
        }
        if (creationStrategy.isVlogEntryEnable() && VlogEntranceUtils.isAvailable()) {
            this.mMultiCreationModels.add(new CreationModel(7, VlogUtils.getVlogNameResId(), R.drawable.assistant_page_vlog));
        }
        if (PrintInstaller.getInstance().isPhotoPrintEnable() && creationStrategy.isPrintEntryEnable()) {
            this.mMultiCreationModels.add(new CreationModel(8, R.string.home_menu_print, R.drawable.assitant_page_macaron));
        }
        if (creationStrategy.isCreatePdfEnable()) {
            this.mMultiCreationModels.add(new CreationModel(9, R.string.operation_pic_to_pdf, R.drawable.assistant_page_pdf));
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            removeSelf();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        MagicMattingLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mMagicMattingDownloadStateListener);
        IDPhotoLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mIDPhotoDownloadStateListener);
        ArtStillLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mArtStillDownloadStateListener);
        VideoPostDownloadManager.getInstance().setDownloadStateListener(this.mVideoPostDownloadListener);
        PrintInstaller.getInstance().addInstallStateListener(this.mInstallStateListener);
        MovieLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mDownloadStateListener);
        VlogLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mVlogDownloadStateListener);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        PrintInstaller.getInstance().removeInstallStateListener(this.mInstallStateListener);
        MovieLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mDownloadStateListener);
        VlogLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mVlogDownloadStateListener);
        MagicMattingLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mMagicMattingDownloadStateListener);
        IDPhotoLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mIDPhotoDownloadStateListener);
        ArtStillLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mArtStillDownloadStateListener);
        VideoPostDownloadManager.getInstance().removeDownloadStateListener();
    }

    public void setOnOperationSelectedListener(OnOperationSelectedListener onOperationSelectedListener) {
        this.mOnOperationSelectedListener = onOperationSelectedListener;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View inflate = View.inflate(getActivity(), R.layout.produce_dialog_layout, null);
        this.mContentView = inflate;
        this.mTitle = (TextView) inflate.findViewById(R.id.title);
        this.mSingleText = (TextView) this.mContentView.findViewById(R.id.single_text);
        this.mMultiText = (TextView) this.mContentView.findViewById(R.id.multi_text);
        this.mSingleRecyclerView = (GalleryRecyclerView) this.mContentView.findViewById(R.id.single_recycler_view);
        this.mMultiRecyclerView = (GalleryRecyclerView) this.mContentView.findViewById(R.id.multi_recycler_view);
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new StartEndSmoothScrollerController(getActivity()));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mSingleRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        CreationAdapter creationAdapter = new CreationAdapter(this.mSingleCreationModels);
        this.mSingleCreationAdapter = creationAdapter;
        this.mSingleRecyclerView.setAdapter(creationAdapter);
        this.mSingleCreationAdapter.setOnItemClickListener(this.mOnItemClickListener);
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager2 = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager2.setSmoothScroller(new StartEndSmoothScrollerController(getActivity()));
        customScrollerLinearLayoutManager2.setOrientation(0);
        this.mMultiRecyclerView.setLayoutManager(customScrollerLinearLayoutManager2);
        CreationAdapter creationAdapter2 = new CreationAdapter(this.mMultiCreationModels);
        this.mMultiCreationAdapter = creationAdapter2;
        this.mMultiRecyclerView.setAdapter(creationAdapter2);
        this.mMultiCreationAdapter.setOnItemClickListener(this.mOnItemClickListener);
        if (this.mSingleCreationModels.size() <= 0) {
            this.mSingleText.setVisibility(8);
            this.mMultiText.setVisibility(8);
            this.mSingleRecyclerView.setVisibility(8);
        }
        if (this.mMultiCreationModels.size() <= 0) {
            this.mSingleText.setVisibility(8);
            this.mMultiText.setVisibility(8);
            this.mMultiRecyclerView.setVisibility(8);
        }
        if (MagicMattingLibraryLoaderHelper.getInstance().getLoaderState() == 1) {
            this.mSingleCreationAdapter.setRemainWhenClick(0, true);
            this.mSingleCreationAdapter.setInformation(0, GalleryApp.sGetAndroidContext().getString(R.string.loading));
        }
        if (IDPhotoLibraryLoaderHelper.getInstance().getLoaderState() == 1) {
            this.mSingleCreationAdapter.setRemainWhenClick(1, true);
            this.mSingleCreationAdapter.setInformation(1, GalleryApp.sGetAndroidContext().getString(R.string.loading));
        }
        if (ArtStillLibraryLoaderHelper.getInstance().getLoaderState() == 1) {
            this.mSingleCreationAdapter.setRemainWhenClick(2, true);
            this.mSingleCreationAdapter.setInformation(2, GalleryApp.sGetAndroidContext().getString(R.string.loading));
        }
        if (VideoPostLibraryLoaderHelper.getInstance().getLoaderState() == 1) {
            this.mSingleCreationAdapter.setRemainWhenClick(3, true);
            this.mSingleCreationAdapter.setInformation(3, GalleryApp.sGetAndroidContext().getString(R.string.loading));
        }
        if (PrintInstaller.getInstance().isPrintInstalling()) {
            this.mMultiCreationAdapter.setRemainWhenClick(8, true);
            this.mMultiCreationAdapter.setInformation(8, GalleryApp.sGetAndroidContext().getString(R.string.loading));
        }
        if (MovieLibraryLoaderHelper.getInstance().getLoaderState() == 1) {
            this.mMultiCreationAdapter.setRemainWhenClick(6, true);
            this.mMultiCreationAdapter.setInformation(6, GalleryApp.sGetAndroidContext().getString(R.string.loading));
        }
        if (VlogLibraryLoaderHelper.getInstance().getLoaderState() == 1) {
            this.mMultiCreationAdapter.setRemainWhenClick(7, true);
            this.mMultiCreationAdapter.setInformation(7, GalleryApp.sGetAndroidContext().getString(R.string.loading));
        }
        builder.setView(this.mContentView);
        AlertDialog create = builder.create();
        this.mDialog = create;
        return create;
    }

    public final void removeSelf() {
        if (isAdded()) {
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    /* loaded from: classes2.dex */
    public class CreationAdapter extends Adapter<CreationHolder> {
        public List<CreationModel> mCreationModelList;

        public CreationAdapter(List<CreationModel> list) {
            this.mCreationModelList = list;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public CreationHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new CreationHolder(getInflater().inflate(R.layout.creation_dialog_item, viewGroup, false));
        }

        @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mCreationModelList.size();
        }

        @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(CreationHolder creationHolder, int i) {
            super.onBindViewHolder((CreationAdapter) creationHolder, i);
            creationHolder.bindView(this.mCreationModelList.get(i));
            View view = creationHolder.itemView;
            FolmeUtil.setDefaultTouchAnim(view, view.findViewById(R.id.icon), null, false, true, true);
        }

        public void setRemainWhenClick(int i, boolean z) {
            for (CreationModel creationModel : this.mCreationModelList) {
                if (creationModel.creationId == i) {
                    creationModel.isRemainWhenClick = z;
                    return;
                }
            }
        }

        public void setInformation(int i, String str) {
            for (CreationModel creationModel : this.mCreationModelList) {
                if (creationModel.creationId == i) {
                    creationModel.informationString = str;
                    return;
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public class CreationHolder extends RecyclerView.ViewHolder {
        public ImageView mIcon;
        public TextView mTitle;

        public CreationHolder(View view) {
            super(view);
            this.mIcon = (ImageView) view.findViewById(R.id.icon);
            this.mTitle = (TextView) view.findViewById(R.id.creation_text);
        }

        public void bindView(CreationModel creationModel) {
            if (!TextUtils.isEmpty(creationModel.informationString)) {
                this.mTitle.setText(creationModel.informationString);
            } else {
                this.mTitle.setText(creationModel.titleResourceId);
            }
            this.mIcon.setImageDrawable(ProduceCreationDialog.this.getResources().getDrawable(creationModel.iconRes));
        }
    }

    public static boolean checkCreationCondition(Context context, int i, List<CheckableAdapter.CheckedItem> list) {
        if (!BaseMiscUtil.isValid(list)) {
            DefaultLogger.e("ProduceCreationDialog", "Checked Items is null");
            return false;
        }
        BaseProduceFilter baseProduceFilter = null;
        if (i == 5) {
            baseProduceFilter = new ProduceCollageFilter(list);
        } else if (i == 6) {
            baseProduceFilter = new ProducePhotoMovieFilter(list);
        } else if (i == 8) {
            if (containVideo(list)) {
                ToastUtils.makeText(context, context.getString(R.string.unsupport_video, getCreationName(context, i)));
                return false;
            }
            int printMaxImageCount = CloudControlStrategyHelper.getCreationStrategy().getPrintMaxImageCount();
            if (list.size() > printMaxImageCount) {
                ToastUtils.makeText(context, context.getResources().getQuantityString(R.plurals.creation_max_image, printMaxImageCount, Integer.valueOf(printMaxImageCount)));
                return false;
            }
        } else if (i == 7) {
            baseProduceFilter = new ProduceVlogFilter(list);
        } else if (i == 4) {
            if (containVideo(list)) {
                ToastUtils.makeText(context, context.getString(R.string.unsupport_video, getCreationName(context, i)));
                return false;
            } else if (list.size() > 1) {
                ToastUtils.makeText(context, (int) R.string.macarons_select_image_range);
                return false;
            } else if (MacaronInstaller.isMimeTypeSupport(list.get(0).getMimeType())) {
                return true;
            } else {
                ToastUtils.makeText(context, context.getString(R.string.prompt_not_support_format));
                return false;
            }
        } else if (i == 0) {
            baseProduceFilter = new ProduceMagicFilter(list);
        } else if (i == 1) {
            baseProduceFilter = new ProduceIDPhotoFilter(list);
        } else if (i == 2) {
            baseProduceFilter = new ProduceArtStillFilter(list);
        } else if (i == 3) {
            baseProduceFilter = new ProduceVideoPostFilter(list);
        }
        if (baseProduceFilter != null && !baseProduceFilter.isSupported()) {
            return false;
        }
        return IntentUtil.checkCreationCondition((FragmentActivity) context, i);
    }

    public static boolean containVideo(List<CheckableAdapter.CheckedItem> list) {
        for (CheckableAdapter.CheckedItem checkedItem : list) {
            if (BaseFileMimeUtil.isVideoFromMimeType(checkedItem.getMimeType())) {
                return true;
            }
        }
        return false;
    }

    public static String getCreationName(Context context, int i) {
        switch (i) {
            case 0:
                return context.getString(R.string.home_menu_magic_matting);
            case 1:
                return context.getString(R.string.home_menu_id_photo);
            case 2:
                return context.getString(R.string.home_menu_art_still);
            case 3:
                return context.getString(R.string.home_menu_video_post);
            case 4:
                return context.getString(R.string.home_menu_macarons);
            case 5:
                return context.getString(R.string.home_menu_collage);
            case 6:
                return context.getString(R.string.home_menu_photo_movie);
            case 7:
                return context.getString(VlogUtils.getVlogNameResId());
            default:
                return context.getString(R.string.home_menu_print);
        }
    }
}
