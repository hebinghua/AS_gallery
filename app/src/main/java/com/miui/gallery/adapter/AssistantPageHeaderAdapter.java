package com.miui.gallery.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.activity.RecommendActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyLibraryLoaderHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.reddot.DisplayStatusManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AssistantPageHeaderItem;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.ArtStillEntranceUtils;
import com.miui.gallery.util.ArtStillLibraryLoaderHelper;
import com.miui.gallery.util.FeatureUtil;
import com.miui.gallery.util.IDPhotoEntranceUtils;
import com.miui.gallery.util.IDPhotoLibraryLoaderHelper;
import com.miui.gallery.util.MIUICommunityGalleryEntranceUtils;
import com.miui.gallery.util.MagicMattingEntranceUtils;
import com.miui.gallery.util.MagicMattingLibraryLoaderHelper;
import com.miui.gallery.util.MovieLibraryLoaderHelper;
import com.miui.gallery.util.PhotoMovieEntranceUtils;
import com.miui.gallery.util.VideoPostDownloadManager;
import com.miui.gallery.util.VideoPostEntranceUtils;
import com.miui.gallery.util.VlogLibraryLoaderHelper;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.market.MacaronInstaller;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.mediaeditor.api.MediaEditorApiHelper;
import com.miui.mediaeditor.api.MediaEditorIntentUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class AssistantPageHeaderAdapter extends BaseRecyclerAdapter<Entrance, BaseViewHolder> {
    public FragmentActivity mActivity;
    public LayoutInflater mLayoutInflater;
    public ArrayList<Entrance> mEntranceArrayList = new ArrayList<>();
    public ArrayList<Entrance> mTempArrayList = new ArrayList<>();

    public AssistantPageHeaderAdapter(FragmentActivity fragmentActivity, boolean z) {
        this.mActivity = fragmentActivity;
        this.mLayoutInflater = LayoutInflater.from(fragmentActivity);
        if (z) {
            fillEntranceList();
        }
    }

    public final void fillEntranceList() {
        this.mTempArrayList.add(new CollageEntrance(0, R.string.home_menu_collage, R.drawable.assistant_page_collage));
        int i = 1;
        if (PhotoMovieEntranceUtils.isDeviceSupportPhotoMovie()) {
            this.mTempArrayList.add(new PhotoMovieEntrance(1, R.string.home_menu_photo_movie, R.drawable.assistant_page_photomovie));
            i = 2;
        }
        if (VlogEntranceUtils.isAvailable()) {
            this.mTempArrayList.add(new VlogEntrance(i, VlogUtils.getVlogNameResId(), R.drawable.assistant_page_vlog));
            i++;
        }
        if (MacaronInstaller.isFunctionOn()) {
            this.mTempArrayList.add(new MacaronsEntrance(i, R.string.home_menu_macarons, R.drawable.assitant_page_macaron));
            i++;
        }
        if (FeatureUtil.isReplaceAssistantPageRecommend()) {
            this.mTempArrayList.add(new FilterSkyEntrance(i, R.string.photo_editor_sky, R.drawable.assistant_page_recommend));
            i++;
        } else if (!MagicMattingEntranceUtils.isAvailable()) {
            this.mTempArrayList.add(new RecommendEntrance(i, R.string.assistant_page_discovery, R.drawable.assistant_page_recommend));
        }
        if (MagicMattingEntranceUtils.isAvailable()) {
            this.mTempArrayList.add(new MagicMattingEntrance(i, R.string.home_menu_magic_matting, R.drawable.assistant_page_recommend));
            i++;
        }
        if (IDPhotoEntranceUtils.isAvailable()) {
            this.mTempArrayList.add(new IDPhotoEntrance(i, R.string.home_menu_id_photo, R.drawable.assistant_page_idphoto));
            i++;
        }
        if (ArtStillEntranceUtils.isAvailable()) {
            this.mTempArrayList.add(new ArtStillEntrance(i, R.string.home_menu_art_still, R.drawable.assistant_page_artstill));
            i++;
        }
        if (VideoPostEntranceUtils.isAvailable()) {
            this.mTempArrayList.add(new VideoPostEntrance(i, R.string.home_menu_video_post, R.drawable.assistant_page_videopost));
        }
        if (MIUICommunityGalleryEntranceUtils.IS_AVAILABLE) {
            this.mTempArrayList.add(new MIUICommunityGalleryEntrance(i, R.string.assistant_page_community, R.drawable.ic_assistant_page_community));
        }
        this.mEntranceArrayList.addAll(this.mTempArrayList);
    }

    public void clearEntranceList() {
        this.mEntranceArrayList.clear();
    }

    public void resetEntranceList() {
        if (this.mTempArrayList.isEmpty()) {
            fillEntranceList();
        } else {
            this.mEntranceArrayList.clear();
            this.mEntranceArrayList.addAll(this.mTempArrayList);
        }
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BaseViewHolder(this.mLayoutInflater.inflate(R.layout.assistant_page_header_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        AssistantPageHeaderItem assistantPageHeaderItem = (AssistantPageHeaderItem) baseViewHolder.itemView;
        assistantPageHeaderItem.bindData(this.mActivity, mo1558getItem(i));
        assistantPageHeaderItem.setDescendantFocusability(393216);
        assistantPageHeaderItem.setContentDescription(assistantPageHeaderItem.getContext().getResources().getString(mo1558getItem(i).mNameRes));
        View view = baseViewHolder.itemView;
        FolmeUtil.setDefaultTouchAnim(view, view.findViewById(R.id.icon), null, false, true, false);
        Folme.useAt(assistantPageHeaderItem.getIcon()).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(assistantPageHeaderItem.getIcon(), new AnimConfig[0]);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        Entrance mo1558getItem = mo1558getItem(i);
        if (mo1558getItem != null) {
            return mo1558getItem.mIndex;
        }
        return -1L;
    }

    @Override // com.miui.gallery.adapter.BaseRecyclerAdapter, com.miui.gallery.adapter.IBaseRecyclerAdapter
    /* renamed from: getItem */
    public Entrance mo1558getItem(int i) {
        if (i < 0 || i >= this.mEntranceArrayList.size()) {
            return null;
        }
        return this.mEntranceArrayList.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mEntranceArrayList.size();
    }

    /* loaded from: classes.dex */
    public static abstract class Entrance implements View.OnClickListener {
        public String mFeatureName;
        public int mIconRes;
        public int mIndex;
        public int mNameRes;

        public Entrance(int i, int i2, int i3, String str) {
            this.mIndex = i;
            this.mNameRes = i2;
            this.mIconRes = i3;
            this.mFeatureName = str;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            DisplayStatusManager.setRedDotClicked(this.mFeatureName);
        }

        public String getFeatureName() {
            return this.mFeatureName;
        }
    }

    /* loaded from: classes.dex */
    public abstract class MediaEditorEntrance extends Entrance {
        public abstract void performClick(View view);

        public MediaEditorEntrance(int i, int i2, int i3, String str) {
            super(i, i2, i3, str);
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.Entrance, android.view.View.OnClickListener
        public void onClick(View view) {
            super.onClick(view);
            if (!MediaEditorInstaller.getInstance().installIfNotExist(AssistantPageHeaderAdapter.this.mActivity, null, false)) {
                return;
            }
            performClick(view);
        }
    }

    /* loaded from: classes.dex */
    public class CollageEntrance extends MediaEditorEntrance {
        public CollageEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "collage");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.MediaEditorEntrance
        public void performClick(View view) {
            if (MediaEditorUtils.isMediaEditorAvailable()) {
                MediaEditorIntentUtils.startCollagePicker(AssistantPageHeaderAdapter.this.mActivity);
            } else {
                ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_COLLAGE_PAGE);
            }
            SamplingStatHelper.recordCountEvent("assistant", "home_page_menu_collage");
            TrackController.trackClick("403.8.3.1.11137", "403.8.0.1.11136");
        }
    }

    /* loaded from: classes.dex */
    public class PhotoMovieEntrance extends MediaEditorEntrance {
        public PhotoMovieEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "photo_movie");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.MediaEditorEntrance
        public void performClick(View view) {
            if (!MediaEditorUtils.isMediaEditorAvailable() || !MediaEditorApiHelper.isDeviceSupportPhotoMovie()) {
                if (MovieLibraryLoaderHelper.getInstance().checkAbleOrDownload(AssistantPageHeaderAdapter.this.mActivity)) {
                    ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_PHOTO_MOVIE);
                }
            } else {
                MediaEditorIntentUtils.startPickerForPhotoMovieTemp(AssistantPageHeaderAdapter.this.mActivity);
            }
            SamplingStatHelper.recordCountEvent("assistant", "home_page_menu_photo_movie");
            TrackController.trackClick("403.8.3.1.11138", "403.8.0.1.11136");
        }
    }

    /* loaded from: classes.dex */
    public class VlogEntrance extends MediaEditorEntrance {
        public VlogEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "vlog");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.MediaEditorEntrance
        public void performClick(View view) {
            if (!MediaEditorUtils.isMediaEditorAvailable() || !MediaEditorApiHelper.isDeviceSupportVlog()) {
                if (VlogLibraryLoaderHelper.getInstance().checkAbleOrDownload(AssistantPageHeaderAdapter.this.mActivity)) {
                    ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_VLOG);
                }
            } else {
                MediaEditorIntentUtils.startVlogFromPickerTemp(AssistantPageHeaderAdapter.this.mActivity);
            }
            SamplingStatHelper.recordCountEvent("assistant", "home_page_menu_vlog");
            TrackController.trackClick("403.8.3.1.11139", "403.8.0.1.11136");
        }
    }

    /* loaded from: classes.dex */
    public class MacaronsEntrance extends Entrance {
        public MacaronsEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "macarons");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.Entrance, android.view.View.OnClickListener
        public void onClick(View view) {
            super.onClick(view);
            ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_MACARONS_PAGE);
            SamplingStatHelper.recordCountEvent("assistant", "home_page_menu_macaron");
        }
    }

    /* loaded from: classes.dex */
    public class RecommendEntrance extends Entrance {
        public RecommendEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "recommendation");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.Entrance, android.view.View.OnClickListener
        public void onClick(View view) {
            super.onClick(view);
            if (AssistantPageHeaderAdapter.this.mActivity != null) {
                AssistantPageHeaderAdapter.this.mActivity.startActivity(new Intent(AssistantPageHeaderAdapter.this.mActivity, RecommendActivity.class));
            }
            SamplingStatHelper.recordCountEvent("assistant", "assistant_page_discover");
            TrackController.trackClick("403.8.3.1.11140", "403.8.0.1.11136");
        }
    }

    /* loaded from: classes.dex */
    public class FilterSkyEntrance extends MediaEditorEntrance {
        public FilterSkyEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "filter_sky");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.MediaEditorEntrance
        public void performClick(View view) {
            if (MediaEditorUtils.isMediaEditorAvailable()) {
                MediaEditorIntentUtils.startFilterSkyFromPickerWithFunctionModel(AssistantPageHeaderAdapter.this.mActivity);
            } else if (SkyLibraryLoaderHelper.INSTANCE.checkAbleOrDownload(AssistantPageHeaderAdapter.this.mActivity)) {
                ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_FILTER_SKY);
            }
            SamplingStatHelper.recordCountEvent("assistant", "home_page_filter_sky");
        }
    }

    /* loaded from: classes.dex */
    public class MagicMattingEntrance extends MediaEditorEntrance {
        public MagicMattingEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "magic_matting");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.MediaEditorEntrance
        public void performClick(View view) {
            if (!MediaEditorUtils.isMediaEditorAvailable() || !MediaEditorApiHelper.isDeviceSupportMagicMatting()) {
                if (MagicMattingLibraryLoaderHelper.getInstance().checkAbleOrDownload(AssistantPageHeaderAdapter.this.mActivity)) {
                    ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_MAGIC_MATTING);
                }
            } else {
                MediaEditorIntentUtils.startMagicMattingFromPickerTemp(AssistantPageHeaderAdapter.this.mActivity);
            }
            TrackController.trackClick("403.8.3.1.11143", "403.8.0.1.11136");
        }
    }

    /* loaded from: classes.dex */
    public class IDPhotoEntrance extends MediaEditorEntrance {
        public IDPhotoEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "id_photo");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.MediaEditorEntrance
        public void performClick(View view) {
            if (!MediaEditorUtils.isMediaEditorAvailable() || !MediaEditorApiHelper.isDeviceSupportIDPhoto()) {
                if (IDPhotoLibraryLoaderHelper.getInstance().checkAbleOrDownload(AssistantPageHeaderAdapter.this.mActivity)) {
                    ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_ID_PHOTO);
                }
            } else {
                MediaEditorIntentUtils.startActivityWithTag(AssistantPageHeaderAdapter.this.mActivity, "idPhoto");
            }
            TrackController.trackClick("403.8.3.1.11141", "403.8.0.1.11136");
        }
    }

    /* loaded from: classes.dex */
    public class ArtStillEntrance extends MediaEditorEntrance {
        public ArtStillEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "art_still");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.MediaEditorEntrance
        public void performClick(View view) {
            if (!MediaEditorUtils.isMediaEditorAvailable() || !MediaEditorApiHelper.isDeviceSupportArtStill()) {
                if (ArtStillLibraryLoaderHelper.getInstance().checkAbleOrDownload(AssistantPageHeaderAdapter.this.mActivity)) {
                    ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_ART_STILL);
                }
            } else {
                MediaEditorIntentUtils.startActivityWithTag(AssistantPageHeaderAdapter.this.mActivity, "artStill");
            }
            TrackController.trackClick("403.8.3.1.11142", "403.8.0.1.11136");
        }
    }

    /* loaded from: classes.dex */
    public class VideoPostEntrance extends MediaEditorEntrance {
        public VideoPostEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "video_post");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.MediaEditorEntrance
        public void performClick(View view) {
            if (!MediaEditorUtils.isMediaEditorAvailable() || !MediaEditorApiHelper.isDeviceSupportVideoPost()) {
                if (VideoPostDownloadManager.getInstance().checkAbleOrDownload(AssistantPageHeaderAdapter.this.mActivity)) {
                    ActionURIHandler.handleUri(AssistantPageHeaderAdapter.this.mActivity, GalleryContract.Common.URI_VIDEO_POST);
                }
            } else {
                MediaEditorIntentUtils.startActivityWithTag(AssistantPageHeaderAdapter.this.mActivity, "videoPost");
            }
            TrackController.trackClick("403.8.3.1.11144", "403.8.0.1.11136");
        }
    }

    /* loaded from: classes.dex */
    public class MIUICommunityGalleryEntrance extends Entrance {
        public MIUICommunityGalleryEntrance(int i, int i2, int i3) {
            super(i, i2, i3, "community");
        }

        @Override // com.miui.gallery.adapter.AssistantPageHeaderAdapter.Entrance, android.view.View.OnClickListener
        public void onClick(View view) {
            super.onClick(view);
            int communityVersionCode = MIUICommunityGalleryEntranceUtils.getCommunityVersionCode();
            AssistantPageHeaderAdapter.this.mActivity.startActivity(new Intent("android.intent.action.VIEW", MIUICommunityGalleryEntranceUtils.getCommunityUri(communityVersionCode)));
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.8.3.1.14988");
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, communityVersionCode >= 0 ? "app" : "web");
            TrackController.trackClick(hashMap);
        }
    }
}
