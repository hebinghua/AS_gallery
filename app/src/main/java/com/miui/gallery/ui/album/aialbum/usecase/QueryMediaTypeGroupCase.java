package com.miui.gallery.ui.album.aialbum.usecase;

import android.net.Uri;
import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor;
import com.miui.gallery.base_optimization.clean.thread.SubScribeThreadExecutor;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.provider.InternalContract$Media;
import com.miui.gallery.ui.album.common.MediaGroupTypeViewBean;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class QueryMediaTypeGroupCase extends HotUseCase<List<MediaGroupTypeViewBean>, RequestBean> {
    public AbstractCloudRepository mCloudRepository;

    public QueryMediaTypeGroupCase(AbstractCloudRepository abstractCloudRepository) {
        this(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor(), abstractCloudRepository);
    }

    public QueryMediaTypeGroupCase(SubScribeThreadExecutor subScribeThreadExecutor, ObserveThreadExecutor observeThreadExecutor, AbstractCloudRepository abstractCloudRepository) {
        super(subScribeThreadExecutor, observeThreadExecutor);
        this.mCloudRepository = abstractCloudRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<List<MediaGroupTypeViewBean>> buildFlowable(final RequestBean requestBean) {
        if (requestBean == null) {
            return Flowable.empty();
        }
        return this.mCloudRepository.queryMediaTypeCount(requestBean.getFlags(), requestBean.getMediaType(), requestBean.getFilterMimeTypes()).map(new Function<PageResults<Map<Long, Integer>>, List<MediaGroupTypeViewBean>>() { // from class: com.miui.gallery.ui.album.aialbum.usecase.QueryMediaTypeGroupCase.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public List<MediaGroupTypeViewBean> mo2564apply(PageResults<Map<Long, Integer>> pageResults) throws Exception {
                LinkedList linkedList = new LinkedList();
                for (Map.Entry<Long, Integer> entry : pageResults.getResult().entrySet()) {
                    long longValue = entry.getKey().longValue();
                    int parseSpecialTypeNameRes = SpecialTypeMediaUtils.parseSpecialTypeNameRes(longValue, requestBean.isParseChild());
                    int parseSpecialTypeId = SpecialTypeMediaUtils.parseSpecialTypeId(longValue, requestBean.isParseChild());
                    if (parseSpecialTypeNameRes != 0 && parseSpecialTypeId != 0) {
                        String string = ResourceUtils.getString(parseSpecialTypeNameRes);
                        linkedList.add(new MediaGroupTypeViewBean(parseSpecialTypeId, ResourceUtils.getResourceUri(SpecialTypeMediaUtils.parseSpecialTypeCoverRes(longValue)), string, QueryMediaTypeGroupCase.this.createIntentActionUri(longValue, string)));
                    }
                }
                return linkedList;
            }
        });
    }

    public final String createIntentActionUri(long j, String str) {
        String str2;
        Uri.Builder appendQueryParameter = GalleryContract.Common.URI_ALBUM_PAGE.buildUpon().appendQueryParameter("extra_from_type", String.valueOf(1016)).appendQueryParameter("id", String.valueOf(2147383645)).appendQueryParameter("query", str);
        if ((64 & j) == 0 && (8388608 & j) == 0) {
            str2 = (8589934592L & j) != 0 ? "alias_rubbish=0 AND alias_hidden=0 AND mimeType='image/gif'" : String.format(Locale.US, "alias_hidden=0 AND alias_rubbish=0 AND (specialTypeFlags !=0 AND specialTypeFlags & %s != 0)%s", Long.valueOf(j), "");
        } else {
            str2 = "alias_rubbish=0 AND alias_hidden=0 AND alias_is_burst=1";
        }
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            acquire.append(str2);
            acquire.append(" AND ");
            acquire.append(InternalContract$Media.MEDIA_DETAIL_EXTRA_SELECTION);
            if (GalleryPreferences.LocalMode.isOnlyShowLocalPhoto()) {
                acquire.append(" AND ");
                acquire.append(InternalContract$Cloud.ALIAS_LOCAL_MEDIA);
            }
            appendQueryParameter.appendQueryParameter("querySelection", acquire.toString());
            Pools.getStringBuilderPool().release(acquire);
            return appendQueryParameter.toString();
        } catch (Throwable th) {
            Pools.getStringBuilderPool().release(acquire);
            throw th;
        }
    }

    /* loaded from: classes2.dex */
    public static class RequestBean {
        public String[] filterMimeTypes;
        public final long[] flags;
        public int mediaType;
        public final boolean parseChild;

        public RequestBean(long[] jArr, boolean z) {
            this.mediaType = -1;
            this.flags = jArr;
            this.parseChild = z;
        }

        public RequestBean(long[] jArr, int i, String[] strArr, boolean z) {
            this.mediaType = -1;
            this.flags = jArr;
            this.mediaType = i;
            this.filterMimeTypes = strArr;
            this.parseChild = z;
        }

        public long[] getFlags() {
            return this.flags;
        }

        public boolean isParseChild() {
            return this.parseChild;
        }

        public int getMediaType() {
            return this.mediaType;
        }

        public String[] getFilterMimeTypes() {
            return this.filterMimeTypes;
        }
    }
}
