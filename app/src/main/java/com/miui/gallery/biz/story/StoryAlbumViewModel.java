package com.miui.gallery.biz.story;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelKt;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.arch.events.FinishEvent;
import com.miui.gallery.arch.events.SingleLiveEvent;
import com.miui.gallery.arch.livedata.ContentLiveData;
import com.miui.gallery.arch.livedata.ContentLiveDataKt;
import com.miui.gallery.arch.viewmodel.BaseViewModel;
import com.miui.gallery.biz.story.data.DownloadCommand;
import com.miui.gallery.biz.story.data.DownloadStatus;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.biz.story.data.MediaStats;
import com.miui.gallery.biz.story.domain.DeleteCard;
import com.miui.gallery.biz.story.domain.ParseDownloadTasks;
import com.miui.gallery.biz.story.domain.RenameCard;
import com.miui.gallery.biz.story.domain.UpdateCard;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.picker.uri.Downloader;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.mediaeditor.api.MediaEditorIntentUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: StoryAlbumViewModel.kt */
@SuppressLint({"StaticFieldLeak"})
/* loaded from: classes.dex */
public final class StoryAlbumViewModel extends BaseViewModel {
    public static final Companion Companion = new Companion(null);
    public static final String[] projection = StoryAlbumAdapter.PROJECTION;
    public static final String[] selectionArgs = null;
    public static final Uri uri;
    public final MutableLiveData<Card> _card;
    public final MutableLiveData<DownloadStatus> _downloadCallbacks;
    public final MutableLiveData<DownloadCommand> _downloadCommands;
    public final MutableLiveData<MediaStats> _mediaStats;
    public final ContentLiveData<MediaInfo> _medias;
    public final long cardId;
    public final Context context;
    public final DeleteCard deleteCard;
    public final LiveData<SingleLiveEvent<DownloadStatus>> downloadCallbacks;
    public final LiveData<SingleLiveEvent<DownloadCommand>> downloadCommands;
    public Downloader downloader;
    public final Lazy durationComparator$delegate;
    public final CoroutineScope mainScope;
    public final LiveData<List<MediaInfo>> medias;
    public final ParseDownloadTasks parseDownloadTasks;
    public final RenameCard renameCard;
    public final Lazy scoreComparator$delegate;
    public Set<String> sha1OfSelectedMedias;
    public final Lazy timeComparator$delegate;
    public final LiveData<List<MediaInfo>> topMedias;
    public final UpdateCard updateCard;

    /* compiled from: StoryAlbumViewModel.kt */
    /* loaded from: classes.dex */
    public interface AssistedVMFactory {
        StoryAlbumViewModel create(long j);
    }

    public static final /* synthetic */ MutableLiveData access$get_downloadCallbacks$p(StoryAlbumViewModel storyAlbumViewModel) {
        return storyAlbumViewModel._downloadCallbacks;
    }

    public static final /* synthetic */ void access$parseFailResult(StoryAlbumViewModel storyAlbumViewModel, List list) {
        storyAlbumViewModel.parseFailResult(list);
    }

    public static final /* synthetic */ void access$parseSuccessResult(StoryAlbumViewModel storyAlbumViewModel, List list) {
        storyAlbumViewModel.parseSuccessResult(list);
    }

    public StoryAlbumViewModel(Context context, CoroutineScope mainScope, long j, UpdateCard updateCard, ParseDownloadTasks parseDownloadTasks, DeleteCard deleteCard, RenameCard renameCard) {
        Unit unit;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mainScope, "mainScope");
        Intrinsics.checkNotNullParameter(updateCard, "updateCard");
        Intrinsics.checkNotNullParameter(parseDownloadTasks, "parseDownloadTasks");
        Intrinsics.checkNotNullParameter(deleteCard, "deleteCard");
        Intrinsics.checkNotNullParameter(renameCard, "renameCard");
        this.context = context;
        this.mainScope = mainScope;
        this.cardId = j;
        this.updateCard = updateCard;
        this.parseDownloadTasks = parseDownloadTasks;
        this.deleteCard = deleteCard;
        this.renameCard = renameCard;
        this.scoreComparator$delegate = LazyKt__LazyJVMKt.lazy(StoryAlbumViewModel$scoreComparator$2.INSTANCE);
        this.timeComparator$delegate = LazyKt__LazyJVMKt.lazy(StoryAlbumViewModel$timeComparator$2.INSTANCE);
        this.durationComparator$delegate = LazyKt__LazyJVMKt.lazy(StoryAlbumViewModel$durationComparator$2.INSTANCE);
        this.sha1OfSelectedMedias = new LinkedHashSet();
        MutableLiveData<Card> mutableLiveData = new MutableLiveData<>();
        this._card = mutableLiveData;
        this._mediaStats = new MutableLiveData<>();
        ContentLiveData<MediaInfo> contentLiveData$default = ContentLiveDataKt.contentLiveData$default(this, context, uri, projection, getSelection(), selectionArgs, "alias_create_time DESC ", null, StoryAlbumViewModel$_medias$1.INSTANCE, 64, null);
        this._medias = contentLiveData$default;
        LiveData<List<MediaInfo>> map = Transformations.map(contentLiveData$default, new Function() { // from class: com.miui.gallery.biz.story.StoryAlbumViewModel$special$$inlined$map$1
            @Override // androidx.arch.core.util.Function
            public final List<? extends MediaInfo> apply(List<? extends MediaInfo> list) {
                MutableLiveData mutableLiveData2;
                MutableLiveData mutableLiveData3;
                MutableLiveData mutableLiveData4;
                List<? extends MediaInfo> list2 = list;
                if (list2 == null) {
                    return CollectionsKt__CollectionsKt.emptyList();
                }
                LinkedHashSet linkedHashSet = new LinkedHashSet();
                Iterator<? extends MediaInfo> it = list2.iterator();
                boolean z = false;
                int i = 0;
                int i2 = 0;
                while (true) {
                    boolean z2 = true;
                    if (!it.hasNext()) {
                        break;
                    }
                    MediaInfo next = it.next();
                    if (next.isVideo()) {
                        i2++;
                    } else {
                        i++;
                    }
                    String sha1 = next.getSha1();
                    if (sha1 != null && sha1.length() != 0) {
                        z2 = false;
                    }
                    if (!z2) {
                        String sha12 = next.getSha1();
                        Intrinsics.checkNotNull(sha12);
                        linkedHashSet.add(sha12);
                    }
                }
                mutableLiveData2 = StoryAlbumViewModel.this._mediaStats;
                mutableLiveData2.setValue(new MediaStats(i, i2));
                StoryAlbumViewModel.this.sha1OfSelectedMedias = linkedHashSet;
                mutableLiveData3 = StoryAlbumViewModel.this._card;
                Card card = (Card) mutableLiveData3.getValue();
                if (card == null) {
                    return list2;
                }
                StoryAlbumViewModel storyAlbumViewModel = StoryAlbumViewModel.this;
                mutableLiveData4 = storyAlbumViewModel._mediaStats;
                T value = mutableLiveData4.getValue();
                Intrinsics.checkNotNull(value);
                if (((MediaStats) value).isShowVideo() != card.isShowVideo()) {
                    z = true;
                }
                storyAlbumViewModel.updateCard(card, z);
                return list2;
            }
        });
        Intrinsics.checkNotNullExpressionValue(map, "crossinline transform: (…p(this) { transform(it) }");
        this.medias = map;
        LiveData<List<MediaInfo>> map2 = Transformations.map(contentLiveData$default, new Function() { // from class: com.miui.gallery.biz.story.StoryAlbumViewModel$special$$inlined$map$2
            @Override // androidx.arch.core.util.Function
            public final List<? extends MediaInfo> apply(List<? extends MediaInfo> list) {
                return StoryAlbumViewModel.getTopMedias$default(StoryAlbumViewModel.this, list, 0, 2, null);
            }
        });
        Intrinsics.checkNotNullExpressionValue(map2, "crossinline transform: (…p(this) { transform(it) }");
        this.topMedias = map2;
        MutableLiveData<DownloadStatus> mutableLiveData2 = new MutableLiveData<>();
        this._downloadCallbacks = mutableLiveData2;
        LiveData<SingleLiveEvent<DownloadStatus>> map3 = Transformations.map(mutableLiveData2, new Function() { // from class: com.miui.gallery.biz.story.StoryAlbumViewModel$special$$inlined$map$3
            @Override // androidx.arch.core.util.Function
            public final SingleLiveEvent<? extends DownloadStatus> apply(DownloadStatus downloadStatus) {
                return new SingleLiveEvent<>(downloadStatus);
            }
        });
        Intrinsics.checkNotNullExpressionValue(map3, "crossinline transform: (…p(this) { transform(it) }");
        this.downloadCallbacks = map3;
        MutableLiveData<DownloadCommand> mutableLiveData3 = new MutableLiveData<>();
        this._downloadCommands = mutableLiveData3;
        LiveData<SingleLiveEvent<DownloadCommand>> map4 = Transformations.map(mutableLiveData3, new Function() { // from class: com.miui.gallery.biz.story.StoryAlbumViewModel$special$$inlined$map$4
            @Override // androidx.arch.core.util.Function
            public final SingleLiveEvent<? extends DownloadCommand> apply(DownloadCommand downloadCommand) {
                return new SingleLiveEvent<>(downloadCommand);
            }
        });
        Intrinsics.checkNotNullExpressionValue(map4, "crossinline transform: (…p(this) { transform(it) }");
        this.downloadCommands = map4;
        Card cardByCardId = CardManager.getInstance().getCardByCardId(j);
        if (cardByCardId == null) {
            unit = null;
        } else {
            List<String> selectedMediaSha1s = cardByCardId.getSelectedMediaSha1s();
            if (selectedMediaSha1s == null || selectedMediaSha1s.isEmpty()) {
                DefaultLogger.w("StoryAlbumViewModel", "Media of card: " + cardByCardId + " is empty");
                publish(new FinishEvent());
            } else {
                mutableLiveData.setValue(cardByCardId);
                List<String> selectedMediaSha1s2 = cardByCardId.getSelectedMediaSha1s();
                Intrinsics.checkNotNullExpressionValue(selectedMediaSha1s2, "it.selectedMediaSha1s");
                this.sha1OfSelectedMedias = CollectionsKt___CollectionsKt.toMutableSet(selectedMediaSha1s2);
                contentLiveData$default.setSelection(getSelection());
            }
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            DefaultLogger.e("StoryAlbumViewModel", Intrinsics.stringPlus("Didn't find card with id: ", Long.valueOf(j)));
            publish(new FinishEvent());
        }
    }

    public final Comparator<MediaInfo> getScoreComparator() {
        return (Comparator) this.scoreComparator$delegate.mo119getValue();
    }

    public final Comparator<MediaInfo> getTimeComparator() {
        return (Comparator) this.timeComparator$delegate.mo119getValue();
    }

    public final Comparator<MediaInfo> getDurationComparator() {
        return (Comparator) this.durationComparator$delegate.mo119getValue();
    }

    public final LiveData<Card> getCard() {
        return this._card;
    }

    public final LiveData<MediaStats> getMediaStats() {
        return this._mediaStats;
    }

    public final LiveData<List<MediaInfo>> getMedias() {
        return this.medias;
    }

    public final LiveData<List<MediaInfo>> getTopMedias() {
        return this.topMedias;
    }

    public final LiveData<SingleLiveEvent<DownloadStatus>> getDownloadCallbacks() {
        return this.downloadCallbacks;
    }

    public final LiveData<SingleLiveEvent<DownloadCommand>> getDownloadCommands() {
        return this.downloadCommands;
    }

    public final String getSelection() {
        return "(localGroupId!=-1000) AND sha1 IN ('" + CollectionsKt___CollectionsKt.joinToString$default(this.sha1OfSelectedMedias, "','", null, null, 0, null, null, 62, null) + "')";
    }

    public final void updateCard(Card card, boolean z) {
        BuildersKt__Builders_commonKt.launch$default(this.mainScope, null, null, new StoryAlbumViewModel$updateCard$1(this, card, z, null), 3, null);
    }

    public final void renameCard(String newName) {
        Intrinsics.checkNotNullParameter(newName, "newName");
        Card value = this._card.getValue();
        if (value == null) {
            return;
        }
        BuildersKt__Builders_commonKt.launch$default(ViewModelKt.getViewModelScope(this), null, null, new StoryAlbumViewModel$renameCard$1$1(value, newName, this, null), 3, null);
    }

    public final void deleteCard() {
        Card value = this._card.getValue();
        Integer num = null;
        if (value != null) {
            BuildersKt__Builders_commonKt.launch$default(this.mainScope, null, null, new StoryAlbumViewModel$deleteCard$1$1(this, value, null), 3, null);
        }
        String ref = AutoTracking.getRef();
        Card value2 = this._card.getValue();
        if (value2 != null) {
            num = Integer.valueOf(value2.getScenarioId());
        }
        TrackController.trackClick("403.38.0.1.18797", ref, String.valueOf(num));
    }

    public final void reloadMedias() {
        ContentLiveData<MediaInfo> contentLiveData = this._medias;
        contentLiveData.setSelection(getSelection());
        contentLiveData.invalidate();
    }

    public final void removeMediaBy(List<String> sha1s) {
        Intrinsics.checkNotNullParameter(sha1s, "sha1s");
        if (sha1s.isEmpty()) {
            return;
        }
        Set<String> set = this.sha1OfSelectedMedias;
        if ((set == null || set.isEmpty()) || !set.removeAll(sha1s)) {
            return;
        }
        reloadMedias();
        Pair[] pairArr = new Pair[2];
        pairArr[0] = TuplesKt.to(MiStat.Param.COUNT, String.valueOf(sha1s.size()));
        Card value = this._card.getValue();
        pairArr[1] = TuplesKt.to("scenario_id", String.valueOf(value == null ? null : Integer.valueOf(value.getScenarioId())));
        SamplingStatHelper.recordCountEvent("assistant", "assistant_card_remove_photos", MapsKt__MapsKt.mapOf(pairArr));
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x002f, code lost:
        if ((r0 == null ? false : r0.isShowVideo()) != false) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void check4Download() {
        /*
            r8 = this;
            boolean r0 = com.miui.gallery.vlog.VlogEntranceUtils.isAvailable()
            r1 = 0
            if (r0 == 0) goto L32
            boolean r0 = com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager.isDeviceSupportVideo()
            if (r0 == 0) goto L32
            androidx.lifecycle.MutableLiveData<com.miui.gallery.biz.story.data.MediaStats> r0 = r8._mediaStats
            java.lang.Object r0 = r0.getValue()
            com.miui.gallery.biz.story.data.MediaStats r0 = (com.miui.gallery.biz.story.data.MediaStats) r0
            if (r0 != 0) goto L19
            r0 = r1
            goto L1d
        L19:
            boolean r0 = r0.isShowVideo()
        L1d:
            if (r0 != 0) goto L31
            androidx.lifecycle.MutableLiveData<com.miui.gallery.card.Card> r0 = r8._card
            java.lang.Object r0 = r0.getValue()
            com.miui.gallery.card.Card r0 = (com.miui.gallery.card.Card) r0
            if (r0 != 0) goto L2b
            r0 = r1
            goto L2f
        L2b:
            boolean r0 = r0.isShowVideo()
        L2f:
            if (r0 == 0) goto L32
        L31:
            r1 = 1
        L32:
            kotlinx.coroutines.CoroutineScope r2 = androidx.lifecycle.ViewModelKt.getViewModelScope(r8)
            r3 = 0
            r4 = 0
            com.miui.gallery.biz.story.StoryAlbumViewModel$check4Download$1 r5 = new com.miui.gallery.biz.story.StoryAlbumViewModel$check4Download$1
            r0 = 0
            r5.<init>(r8, r1, r0)
            r6 = 3
            r7 = 0
            kotlinx.coroutines.BuildersKt.launch$default(r2, r3, r4, r5, r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.biz.story.StoryAlbumViewModel.check4Download():void");
    }

    public final void startDownload() {
        cancelDownload();
        DownloadCommand value = this._downloadCommands.getValue();
        List<Downloader.DownloadTask> tasks = value == null ? null : value.getTasks();
        if (tasks == null || tasks.isEmpty()) {
            DefaultLogger.w("StoryAlbumViewModel", "Request to download files, but nothing to do");
            return;
        }
        Downloader downloader = new Downloader(new ArrayList(tasks), new Downloader.DownloadListener() { // from class: com.miui.gallery.biz.story.StoryAlbumViewModel$startDownload$1
            public int total;

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onStart(List<Downloader.DownloadTask> list) {
                this.total = list == null ? 0 : list.size();
                MutableLiveData access$get_downloadCallbacks$p = StoryAlbumViewModel.access$get_downloadCallbacks$p(StoryAlbumViewModel.this);
                if (list == null) {
                    list = CollectionsKt__CollectionsKt.emptyList();
                }
                access$get_downloadCallbacks$p.setValue(new DownloadStatus.STARTED(list));
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onEnd(List<Downloader.DownloadResult> success, List<Downloader.DownloadResult> fails) {
                Intrinsics.checkNotNullParameter(success, "success");
                Intrinsics.checkNotNullParameter(fails, "fails");
                StoryAlbumViewModel.access$get_downloadCallbacks$p(StoryAlbumViewModel.this).setValue(new DownloadStatus.COMPLETED(success, fails));
                StoryAlbumViewModel.access$parseSuccessResult(StoryAlbumViewModel.this, success);
                StoryAlbumViewModel.access$parseFailResult(StoryAlbumViewModel.this, fails);
                StoryAlbumViewModel.this.cancelDownload();
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onCancelled(List<Downloader.DownloadResult> success, List<Downloader.DownloadResult> fails) {
                Intrinsics.checkNotNullParameter(success, "success");
                Intrinsics.checkNotNullParameter(fails, "fails");
                StoryAlbumViewModel.access$get_downloadCallbacks$p(StoryAlbumViewModel.this).setValue(new DownloadStatus.CANCELLED(success, fails));
                StoryAlbumViewModel.access$parseSuccessResult(StoryAlbumViewModel.this, success);
                StoryAlbumViewModel.this.cancelDownload();
            }

            @Override // com.miui.gallery.picker.uri.Downloader.DownloadListener
            public void onUpdate(List<Downloader.DownloadResult> success, List<Downloader.DownloadResult> fails) {
                Intrinsics.checkNotNullParameter(success, "success");
                Intrinsics.checkNotNullParameter(fails, "fails");
                StoryAlbumViewModel.access$get_downloadCallbacks$p(StoryAlbumViewModel.this).setValue(new DownloadStatus.PROGRESS(this.total <= 0 ? 0.0f : success.size() / this.total));
            }
        });
        downloader.start();
        this.downloader = downloader;
    }

    public final void parseFailResult(List<? extends Downloader.DownloadResult> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (Downloader.DownloadResult downloadResult : list) {
            Downloader.DownloadTask downloadTask = downloadResult.mTask;
            downloadTask.mType = DownloadType.ORIGIN;
            arrayList.add(downloadTask);
        }
        this._downloadCommands.setValue(new DownloadCommand(true, arrayList));
    }

    public final void parseSuccessResult(List<? extends Downloader.DownloadResult> list) {
        List<MediaInfo> value;
        if (!(list == null || list.isEmpty()) && (value = this.medias.getValue()) != null) {
            for (MediaInfo mediaInfo : value) {
                Iterator<? extends Downloader.DownloadResult> it = list.iterator();
                while (true) {
                    if (it.hasNext()) {
                        Downloader.DownloadResult next = it.next();
                        if (Intrinsics.areEqual(CloudUriAdapter.getDownloadUri(mediaInfo.getId()), next.mTask.mUri)) {
                            DefaultLogger.d("StoryAlbumViewModel", Intrinsics.stringPlus("Update path: ", next.mPath));
                            DownloadType downloadType = next.mTask.mType;
                            if (downloadType == DownloadType.ORIGIN || downloadType == DownloadType.ORIGIN_FORCE) {
                                mediaInfo.setFilePath(next.mPath);
                            } else if (downloadType == DownloadType.THUMBNAIL) {
                                mediaInfo.setThumbPath(next.mPath);
                            }
                        }
                    }
                }
            }
        }
    }

    public final void cancelDownload() {
        Downloader downloader = this.downloader;
        if (downloader != null) {
            Intrinsics.checkNotNull(downloader);
            downloader.cancel();
            Downloader downloader2 = this.downloader;
            Intrinsics.checkNotNull(downloader2);
            downloader2.destroy();
            this.downloader = null;
        }
    }

    public final void pauseDownload() {
        Downloader downloader = this.downloader;
        if (downloader == null) {
            return;
        }
        downloader.pause();
    }

    public final void resumeDownload() {
        Downloader downloader = this.downloader;
        if (downloader == null) {
            return;
        }
        downloader.resume();
    }

    public final void gotoPreviewSelectPage(Fragment fragment, int i, int i2, ImageLoadParams imageLoadParams, long[] selectedIds, int[] selectedPositions) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(selectedIds, "selectedIds");
        Intrinsics.checkNotNullParameter(selectedPositions, "selectedPositions");
        IntentUtil.gotoPreviewSelectPage(fragment, uri, i, i2, getSelection(), selectionArgs, "alias_create_time DESC ", imageLoadParams, selectedIds, selectedPositions, null, null);
        Card value = this._card.getValue();
        SamplingStatHelper.recordCountEvent("assistant", "assistant_card_share_photos", MapsKt__MapsJVMKt.mapOf(TuplesKt.to("scenario_id", String.valueOf(value == null ? null : Integer.valueOf(value.getScenarioId())))));
    }

    public final void gotoPhotoPage(Fragment fragment, ViewGroup adapterView, int i, int i2, ImageLoadParams imageLoadParams) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        Intrinsics.checkNotNullParameter(adapterView, "adapterView");
        new PhotoPageIntent.Builder(fragment, InternalPhotoPageActivity.class).setAdapterView(adapterView).setUri(uri).setSelection(getSelection()).setSelectionArgs(selectionArgs).setOrderBy("alias_create_time DESC ").setAlbumId(2147483640L).setUnfoldBurst(true).setInitPosition(i).setCount(i2).setImageLoadParams(imageLoadParams).build().gotoPhotoPage();
    }

    public final List<MediaInfo> getTopImages(List<MediaInfo> list, int i) {
        if ((list == null || list.isEmpty()) || i <= 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            if (!((MediaInfo) obj).isVideo()) {
                arrayList.add(obj);
            }
        }
        List<MediaInfo> sortedWith = CollectionsKt___CollectionsKt.sortedWith(arrayList, getScoreComparator());
        if (sortedWith.size() > i) {
            sortedWith = sortedWith.subList(0, i);
        }
        Collections.sort(sortedWith, getTimeComparator());
        return sortedWith;
    }

    public final List<MediaInfo> getTopVideos(List<MediaInfo> list, int i) {
        if ((list == null || list.isEmpty()) || i <= 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            if (((MediaInfo) obj).isVideo()) {
                arrayList.add(obj);
            }
        }
        List<MediaInfo> sortedWith = CollectionsKt___CollectionsKt.sortedWith(arrayList, getDurationComparator());
        if (sortedWith.size() > i) {
            sortedWith = sortedWith.subList(0, i);
        }
        Collections.sort(sortedWith, getDurationComparator());
        return sortedWith;
    }

    public static /* synthetic */ List getTopMedias$default(StoryAlbumViewModel storyAlbumViewModel, List list, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 20;
        }
        return storyAlbumViewModel.getTopMedias(list, i);
    }

    public final List<MediaInfo> getTopMedias(List<MediaInfo> list, int i) {
        if (!(list == null || list.isEmpty()) && i > 0) {
            List<MediaInfo> sortedWith = CollectionsKt___CollectionsKt.sortedWith(list, getScoreComparator());
            if (sortedWith.size() > i) {
                sortedWith = sortedWith.subList(0, i);
            }
            Collections.sort(sortedWith, getTimeComparator());
            return sortedWith;
        }
        return CollectionsKt__CollectionsKt.emptyList();
    }

    public final void navigateToVLog(FragmentActivity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        DefaultLogger.d("StoryAlbumViewModel", "Navigate to VLog");
        if (!MediaEditorInstaller.getInstance().installIfNotExist(activity, null, false)) {
            return;
        }
        List<MediaInfo> topVideos = getTopVideos(this.medias.getValue(), 30);
        if (!topVideos.isEmpty()) {
            ArrayList arrayList = new ArrayList(topVideos.size());
            for (MediaInfo mediaInfo : topVideos) {
                String filePath = mediaInfo.getFilePath();
                if (filePath != null) {
                    arrayList.add(filePath);
                }
            }
            if (MediaEditorUtils.isMediaEditorAvailable()) {
                MediaEditorIntentUtils.startVlogFromStory(activity, "vlog_template_rixi", arrayList);
            } else {
                IntentUtil.startVlogFromStory(activity, "vlog_template_rixi", arrayList);
            }
            recordSliderClick();
            return;
        }
        DefaultLogger.w("StoryAlbumViewModel", "No videos");
    }

    public final void navigateToPhotoMovie(FragmentActivity activity) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        DefaultLogger.d("StoryAlbumViewModel", "Navigate to PhotoMovie");
        Card value = this._card.getValue();
        if (value != null && MediaEditorInstaller.getInstance().installIfNotExist(activity, null, false)) {
            if (MediaEditorUtils.isMediaEditorAvailable()) {
                List<MediaInfo> topImages = getTopImages(getMedias().getValue(), 20);
                if (!TypeIntrinsics.isMutableList(topImages)) {
                    topImages = CollectionsKt___CollectionsKt.toMutableList((Collection) topImages);
                }
                MediaEditorIntentUtils.startPhotoMovie(activity, topImages, value.getRowId(), CardUtil.getMovieTemplateFromCard(value), value.getTitle(), value.getDescription());
            } else {
                List<MediaInfo> topImages2 = getTopImages(getMedias().getValue(), 20);
                if (!TypeIntrinsics.isMutableList(topImages2)) {
                    topImages2 = CollectionsKt___CollectionsKt.toMutableList((Collection) topImages2);
                }
                IntentUtil.startPhotoMovie(activity, topImages2, value.getRowId(), CardUtil.getMovieTemplateFromCard(value), value.getTitle(), value.getDescription());
            }
            recordSliderClick();
        }
    }

    public final void recordSliderClick() {
        Card value = this._card.getValue();
        if (value == null) {
            return;
        }
        SamplingStatHelper.recordCountEvent("assistant", "assistant_card_detail_header_click", MapsKt__MapsJVMKt.mapOf(TuplesKt.to("scenario_id", String.valueOf(value.getScenarioId()))));
    }

    /* compiled from: StoryAlbumViewModel.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    static {
        Uri URI_OWNER_ALBUM_DETAIL_MEDIA = GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA;
        Intrinsics.checkNotNullExpressionValue(URI_OWNER_ALBUM_DETAIL_MEDIA, "URI_OWNER_ALBUM_DETAIL_MEDIA");
        int i = 0;
        Pair[] pairArr = {TuplesKt.to("remove_duplicate_items", "true")};
        Uri.Builder buildUpon = URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon();
        while (i < 1) {
            Pair pair = pairArr[i];
            i++;
            buildUpon.appendQueryParameter((String) pair.getFirst(), (String) pair.getSecond());
        }
        Uri build = buildUpon.build();
        Intrinsics.checkNotNullExpressionValue(build, "{\n        buildUpon().ap…}\n        }.build()\n    }");
        uri = build;
    }
}
