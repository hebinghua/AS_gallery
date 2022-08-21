package com.miui.gallery.ui.album.common;

import android.content.Intent;
import android.util.Pair;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.picker.PickAlbumDetailActivity;
import com.miui.gallery.picker.PickGalleryActivity;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.ui.AlbumDetailReplaceAlbumCoverImmersionMenu;
import com.miui.gallery.ui.ListGalleryDialogFragment;
import com.miui.gallery.ui.album.common.usecase.DoReplaceAlbumCoverCase;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.GalleryProgressDisposableSubscriber;
import com.miui.gallery.util.SimpleDisposableSubscriber;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.FlowableSubscriber;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/* loaded from: classes2.dex */
public class ReplaceAlbumCoverUtils {

    /* loaded from: classes2.dex */
    public interface CallBack {
        void onFailed(Collection<Album> collection, long j);

        void onSuccess(List<Pair<Album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> list);
    }

    public static void startReplaceAlbumCoverProcess(Collection<Album> collection, GalleryFragment galleryFragment, CallBack callBack, int i) {
        View findViewById;
        if (i == 1013) {
            showReplaceCoverModeDialog(collection, galleryFragment, callBack);
        } else if (i != 1012 || galleryFragment.getActivity() == null || (findViewById = galleryFragment.getActivity().getWindow().getDecorView().findViewById(R.id.more)) == null) {
        } else {
            showReplaceCoverModeImmersionMenuDialog(collection.iterator().next(), galleryFragment, findViewById, callBack);
        }
    }

    public static void showReplaceCoverModeDialog(final Collection<Album> collection, final GalleryFragment galleryFragment, final CallBack callBack) {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            return;
        }
        boolean isNeedAddRecoverCoverItem = isNeedAddRecoverCoverItem(collection);
        if (collection.size() == 1 && !isNeedAddRecoverCoverItem) {
            startPhotoPickerByReplaceAlbumCover(galleryFragment, collection.iterator().next());
            TrackController.trackClick("403.7.4.1.10347", "403.7.4.1.10542", collection.size());
        } else if (!isNeedAddRecoverCoverItem) {
            startPhotoPickerByReplaceAlbumCover(galleryFragment, null);
        } else {
            ArrayList<ListGalleryDialogFragment.ItemData> arrayList = new ArrayList<>(2);
            arrayList.add(new ListGalleryDialogFragment.ItemData(R.id.replace_album_cover_custom_mode, R.string.operation_replace_album_cover_custom_mode));
            arrayList.add(new ListGalleryDialogFragment.ItemData(R.id.replace_album_cover_default_mode, R.string.operation_replace_album_cover_default_mode));
            ListGalleryDialogFragment listGalleryDialogFragment = new ListGalleryDialogFragment();
            listGalleryDialogFragment.setDatas(arrayList);
            listGalleryDialogFragment.setOnOperationSelectedListener(new ListGalleryDialogFragment.OnOperationSelectedListener() { // from class: com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.1
                @Override // com.miui.gallery.ui.ListGalleryDialogFragment.OnOperationSelectedListener
                public boolean onOperationSelected(int i) {
                    boolean z = false;
                    switch (i) {
                        case R.id.replace_album_cover_custom_mode /* 2131363201 */:
                            if (collection.size() == 1) {
                                ReplaceAlbumCoverUtils.startPhotoPickerByReplaceAlbumCover(galleryFragment, (Album) collection.iterator().next());
                            } else {
                                ReplaceAlbumCoverUtils.startPhotoPickerByReplaceAlbumCover(galleryFragment, null);
                            }
                            TrackController.trackClick("403.7.4.1.10347", "403.7.4.1.10542", collection.size());
                            return true;
                        case R.id.replace_album_cover_default_mode /* 2131363202 */:
                            Collection collection2 = collection;
                            if (collection2.size() > 1) {
                                z = true;
                            }
                            ReplaceAlbumCoverUtils.doRecoverAlbumCover(collection2, z, galleryFragment, callBack);
                            TrackController.trackClick("403.7.4.1.10348", "403.7.4.1.10542", collection.size());
                            return true;
                        default:
                            return false;
                    }
                }
            });
            listGalleryDialogFragment.showAllowingStateLoss(galleryFragment.getFragmentManager(), "ProduceCreationDialog");
        }
    }

    public static void showReplaceCoverModeImmersionMenuDialog(Album album, GalleryFragment galleryFragment, View view, CallBack callBack) {
        new AlbumDetailReplaceAlbumCoverImmersionMenu(galleryFragment, album, callBack).showImmersionMenu(view);
    }

    public static void startPhotoPickerByReplaceAlbumCover(GalleryFragment galleryFragment, Album album) {
        if (album != null) {
            Intent intent = new Intent();
            intent.setClass(galleryFragment.getContext(), PickAlbumDetailActivity.class);
            intent.putExtra("other_share_album", album.isOtherShareAlbum());
            intent.putExtra("owner_share_album", album.isOwnerShareAlbum());
            intent.putExtra("album_id", album.getAlbumId());
            intent.putExtra("album_server_id", album.getServerId());
            intent.putExtra("album_local_path", album.getLocalPath());
            intent.putExtra("screenshot_album", album.isScreenshotsAlbum());
            intent.putExtra("screenrecorder_album", album.isScreenRecorderAlbum());
            intent.putExtra("baby_album", album.isBabyAlbum());
            intent.putExtra("album_name", album.getDisplayedAlbumName());
            intent.putExtra("pick-upper-bound", 1);
            intent.putExtra("pick-need-id", true);
            intent.putExtra("pick-owner", true);
            intent.putExtra("picker_result_set", PickerActivity.copyPicker(null));
            intent.putExtra("picker_media_type", Picker.MediaType.ALL.ordinal());
            intent.putExtra("extra_from_type", 1014);
            galleryFragment.startActivityForResult(intent, 58);
            return;
        }
        Intent intent2 = new Intent(galleryFragment.getActivity(), PickGalleryActivity.class);
        intent2.putExtra("picker_media_type", Picker.MediaType.ALL.ordinal());
        intent2.putExtra("pick-upper-bound", 1);
        intent2.putExtra("pick-need-id", true);
        intent2.putExtra("pick-owner", true);
        intent2.putExtra("extra_from_type", 1014);
        galleryFragment.startActivityForResult(intent2, 57);
    }

    public static boolean isNeedAddRecoverCoverItem(Collection<Album> collection) {
        for (Album album : collection) {
            if (album.isManualSetCover()) {
                return true;
            }
        }
        return false;
    }

    public static void doRecoverAlbumCover(Collection<Album> collection, boolean z, GalleryFragment galleryFragment, CallBack callBack) {
        doReplaceAlbumCover(collection, -1L, z, galleryFragment, callBack);
    }

    public static void doRecoverAlbumCover(Album album, boolean z, GalleryFragment galleryFragment, CallBack callBack) {
        doRecoverAlbumCover(Collections.singletonList(album), z, galleryFragment, callBack);
    }

    public static void doReplaceAlbumCover(final Collection<Album> collection, final long j, boolean z, GalleryFragment galleryFragment, final CallBack callBack) {
        FlowableSubscriber flowableSubscriber;
        int i = 0;
        DebugUtil.logEventTime("operationTrace", "replace_album_cover", false);
        DoReplaceAlbumCoverCase doReplaceAlbumCoverCase = new DoReplaceAlbumCoverCase((AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY));
        long[] jArr = new long[collection.size()];
        for (Album album : collection) {
            if (album != null) {
                jArr[i] = album.getAlbumId();
                i++;
            }
        }
        if (z) {
            flowableSubscriber = new GalleryProgressDisposableSubscriber<ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>>(galleryFragment.getActivity()) { // from class: com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.2
                @Override // org.reactivestreams.Subscriber
                public void onNext(ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult> arrayList) {
                    ReplaceAlbumCoverUtils.dispatchResult(collection, j, arrayList, callBack);
                }
            };
        } else {
            flowableSubscriber = new SimpleDisposableSubscriber<ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>>() { // from class: com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils.3
                @Override // org.reactivestreams.Subscriber
                public void onNext(ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult> arrayList) {
                    ReplaceAlbumCoverUtils.dispatchResult(collection, j, arrayList, callBack);
                }
            };
        }
        doReplaceAlbumCoverCase.execute(flowableSubscriber, new DoReplaceAlbumCoverCase.DoReplaceAlbumCoverRequestBean(j, jArr));
    }

    public static void dispatchResult(Collection<Album> collection, long j, ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult> arrayList, CallBack callBack) {
        if (callBack == null) {
            return;
        }
        boolean z = true;
        DebugUtil.logEventTime("operationTrace", "replace_album_cover", true);
        int i = 0;
        if (j != -1 || collection.size() != 1) {
            z = false;
        }
        if (z && collection.iterator().next().isVirtualAlbum()) {
            callBack.onSuccess(Collections.EMPTY_LIST);
        } else if (Objects.isNull(collection) || Objects.isNull(arrayList) || (!z && collection.size() != arrayList.size())) {
            callBack.onFailed(collection, -1L);
        } else {
            LinkedList linkedList = new LinkedList();
            for (final Album album : collection) {
                DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult doReplaceAlbumCoverResult = arrayList.get(i);
                if (doReplaceAlbumCoverResult.getAlbumId() != album.getAlbumId()) {
                    Optional findFirst = arrayList.stream().filter(new Predicate() { // from class: com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils$$ExternalSyntheticLambda0
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            boolean lambda$dispatchResult$0;
                            lambda$dispatchResult$0 = ReplaceAlbumCoverUtils.lambda$dispatchResult$0(Album.this, (DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult) obj);
                            return lambda$dispatchResult$0;
                        }
                    }).findFirst();
                    if (findFirst.isPresent()) {
                        doReplaceAlbumCoverResult = (DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult) findFirst.get();
                    }
                }
                linkedList.add(Pair.create(album, doReplaceAlbumCoverResult));
                i++;
            }
            callBack.onSuccess(linkedList);
        }
    }

    public static /* synthetic */ boolean lambda$dispatchResult$0(Album album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult doReplaceAlbumCoverResult) {
        return doReplaceAlbumCoverResult.getAlbumId() == album.getAlbumId();
    }

    public static boolean handleActivityResultByReplaceAlbumCover(GalleryFragment galleryFragment, int i, int i2, Intent intent, Collection<Album> collection, CallBack callBack) {
        Set set;
        if (i == 57) {
            DefaultLogger.d("ReplaceAlbumCoverUtils", "封面替换 Picker结果返回");
            handleActivityResultByReplaceAlbumCover(galleryFragment, collection, callBack, intent);
            return true;
        } else if (i != 58) {
            return false;
        } else {
            DefaultLogger.d("ReplaceAlbumCoverUtils", "封面替换 Picker结果返回");
            if (i2 == 4) {
                if (callBack != null) {
                    callBack.onFailed(collection, -1L);
                }
            } else if (i2 == 3) {
                startPhotoPickerByReplaceAlbumCover(galleryFragment, null);
            } else if (intent != null && (set = (Set) intent.getSerializableExtra("internal_key_updated_selection")) != null && !set.isEmpty()) {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(Long.valueOf((String) set.iterator().next()));
                intent.putExtra("pick-result-data", arrayList);
                handleActivityResultByReplaceAlbumCover(galleryFragment, collection, callBack, intent);
            }
            return true;
        }
    }

    public static void handleActivityResultByReplaceAlbumCover(GalleryFragment galleryFragment, Collection<Album> collection, CallBack callBack, Intent intent) {
        if (intent == null) {
            if (callBack == null) {
                return;
            }
            callBack.onFailed(collection, -1L);
            return;
        }
        ArrayList arrayList = (ArrayList) intent.getSerializableExtra("pick-result-data");
        if (arrayList == null || arrayList.isEmpty()) {
            if (callBack == null) {
                return;
            }
            callBack.onFailed(collection, -1L);
            return;
        }
        long longValue = ((Long) arrayList.get(0)).longValue();
        if (collection == null || collection.size() <= 0) {
            callBack.onFailed(collection, longValue);
        } else {
            doReplaceAlbumCover(collection, longValue, collection.size() > 1, galleryFragment, callBack);
        }
    }

    public static void setVirtualAlbumCoverId(long j, long j2) {
        if (Album.isAllPhotosAlbum(j2)) {
            AlbumConfigSharedPreferences.getInstance().putLong(GalleryPreferences.PrefKeys.RECENT_ALBUM_COVER_ID, j);
        } else if (Album.isVideoAlbum(j2)) {
            AlbumConfigSharedPreferences.getInstance().putLong(GalleryPreferences.PrefKeys.VIDEO_ALBUM_COVER_ID, j);
        } else if (Album.isFavoritesAlbum(j2)) {
            AlbumConfigSharedPreferences.getInstance().putLong(GalleryPreferences.PrefKeys.FAVORITES_ALBUM_COVER_ID, j);
        } else if (!Album.isScreenshotsRecorders(j2)) {
        } else {
            AlbumConfigSharedPreferences.getInstance().putLong(GalleryPreferences.PrefKeys.SCREENSHOTS_RECORDERS_ALBUM_COVER_ID, j);
        }
    }

    public static long getVirtualAlbumCoverId(long j) {
        if (Album.isAllPhotosAlbum(j)) {
            return AlbumConfigSharedPreferences.getInstance().getLong(GalleryPreferences.PrefKeys.RECENT_ALBUM_COVER_ID, -1L);
        }
        if (Album.isVideoAlbum(j)) {
            return AlbumConfigSharedPreferences.getInstance().getLong(GalleryPreferences.PrefKeys.VIDEO_ALBUM_COVER_ID, -1L);
        }
        if (Album.isFavoritesAlbum(j)) {
            return AlbumConfigSharedPreferences.getInstance().getLong(GalleryPreferences.PrefKeys.FAVORITES_ALBUM_COVER_ID, -1L);
        }
        if (!Album.isScreenshotsRecorders(j)) {
            return -1L;
        }
        return AlbumConfigSharedPreferences.getInstance().getLong(GalleryPreferences.PrefKeys.SCREENSHOTS_RECORDERS_ALBUM_COVER_ID, -1L);
    }

    public static Album generateVirtualAlbumBean(long j) {
        long virtualAlbumCoverId = getVirtualAlbumCoverId(j);
        Album album = new Album(j);
        album.setServerId(AlbumDataHelper.getSystemAlbumServerIdByLocalId(j));
        if (virtualAlbumCoverId == -1) {
            return album;
        }
        album.setManualSetCover(true);
        return album;
    }
}
