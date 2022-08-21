package com.miui.gallery.storage;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.common.collect.Lists;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.GalleryFilePathResolver;
import com.miui.gallery.storage.utils.IFilePathResolver;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class GalleryFilePathResolver implements IFilePathResolver {
    public final CloudIdProcessor mCloudIdProcessor = new CloudIdProcessor();
    public final AlbumIdProcessor mAlbumIdProcessor = new AlbumIdProcessor();
    public final BaseDataItemProcessor mBaseDataItemProcessor = new BaseDataItemProcessor();
    public final FileUriProcessor mFileUriProcessor = new FileUriProcessor();
    public final ContentUriProcessor mContentUriProcessor = new ContentUriProcessor();

    @Override // com.miui.gallery.storage.utils.IFilePathResolver
    public List<String> getPaths(Object obj, int i) {
        if (obj == null) {
            return Collections.emptyList();
        }
        if (i == 0) {
            return getPathsByCloudId(obj);
        }
        if (i == 1) {
            return getPathsByAlbumId(obj);
        }
        if (i == 2) {
            return getPathsByBaseDataItem(obj);
        }
        if (i == 3) {
            return getPathsByUri(obj);
        }
        return Collections.emptyList();
    }

    public final List<String> getPathsByCloudId(Object obj) {
        if (obj instanceof Long) {
            return this.mCloudIdProcessor.apply(Collections.singletonList((Long) obj));
        }
        if (obj instanceof Iterable) {
            Iterable iterable = (Iterable) obj;
            Iterator it = iterable.iterator();
            if (!it.hasNext() || !(it.next() instanceof Long)) {
                return Collections.emptyList();
            }
            return this.mCloudIdProcessor.apply((List<Long>) Lists.newArrayList(iterable));
        } else if (obj instanceof Long[]) {
            return this.mCloudIdProcessor.apply(Arrays.asList((Long[]) obj));
        } else {
            if (obj instanceof long[]) {
                return this.mCloudIdProcessor.apply((List) Arrays.stream((long[]) obj).boxed().collect(Collectors.toList()));
            }
            return Collections.emptyList();
        }
    }

    public final List<String> getPathsByAlbumId(Object obj) {
        if (obj instanceof Long) {
            return this.mAlbumIdProcessor.apply(Collections.singletonList((Long) obj));
        }
        if (obj instanceof Iterable) {
            Iterable iterable = (Iterable) obj;
            Iterator it = iterable.iterator();
            if (!it.hasNext() || !(it.next() instanceof Long)) {
                return Collections.emptyList();
            }
            return this.mAlbumIdProcessor.apply((List<Long>) Lists.newArrayList(iterable));
        } else if (obj instanceof Long[]) {
            return this.mAlbumIdProcessor.apply(Arrays.asList((Long[]) obj));
        } else {
            if (obj instanceof long[]) {
                return this.mAlbumIdProcessor.apply((List) Arrays.stream((long[]) obj).boxed().collect(Collectors.toList()));
            }
            return Collections.emptyList();
        }
    }

    public final List<String> getPathsByBaseDataItem(Object obj) {
        if (obj instanceof BaseDataItem) {
            return this.mBaseDataItemProcessor.apply(Collections.singletonList((BaseDataItem) obj));
        }
        if (obj instanceof Iterable) {
            Iterable iterable = (Iterable) obj;
            Iterator it = iterable.iterator();
            if (!it.hasNext() || !(it.next() instanceof BaseDataItem)) {
                return Collections.emptyList();
            }
            return this.mBaseDataItemProcessor.apply((List<BaseDataItem>) Lists.newArrayList(iterable));
        } else if (obj instanceof BaseDataItem[]) {
            return this.mBaseDataItemProcessor.apply(Arrays.asList((BaseDataItem[]) obj));
        } else {
            return Collections.emptyList();
        }
    }

    public final List<String> getPathsByUri(Object obj) {
        if (obj instanceof Uri) {
            Uri uri = (Uri) obj;
            if (MiStat.Param.CONTENT.equals(uri.getScheme())) {
                return this.mContentUriProcessor.apply(Collections.singletonList(uri));
            }
            return this.mFileUriProcessor.apply(Collections.singletonList(uri));
        } else if (obj instanceof Iterable) {
            Iterable iterable = (Iterable) obj;
            Iterator it = iterable.iterator();
            if (!it.hasNext()) {
                return Collections.emptyList();
            }
            Object next = it.next();
            if (!(next instanceof Uri)) {
                return Collections.emptyList();
            }
            if (MiStat.Param.CONTENT.equals(((Uri) next).getScheme())) {
                return this.mContentUriProcessor.apply((List<Uri>) Lists.newArrayList(iterable));
            }
            return this.mFileUriProcessor.apply((List<Uri>) Lists.newArrayList(iterable));
        } else if (obj instanceof Uri[]) {
            Uri[] uriArr = (Uri[]) obj;
            if (MiStat.Param.CONTENT.equals(uriArr[0].getScheme())) {
                return this.mContentUriProcessor.apply(Arrays.asList(uriArr));
            }
            return this.mFileUriProcessor.apply(Arrays.asList(uriArr));
        } else {
            return Collections.emptyList();
        }
    }

    /* loaded from: classes2.dex */
    public static class CloudIdProcessor implements Function<List<Long>, List<String>> {
        public CloudIdProcessor() {
        }

        @Override // java.util.function.Function
        public List<String> apply(List<Long> list) {
            if (!BaseMiscUtil.isValid(list)) {
                return Collections.emptyList();
            }
            return (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Media.URI, new String[]{"localFile", "thumbnailFile"}, "_id IN (" + TextUtils.join(", ", list) + ")", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<String>>() { // from class: com.miui.gallery.storage.GalleryFilePathResolver.CloudIdProcessor.1
                {
                    CloudIdProcessor.this = this;
                }

                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public List<String> mo1808handle(Cursor cursor) {
                    if (cursor == null || cursor.getCount() <= 0) {
                        return Collections.emptyList();
                    }
                    LinkedList linkedList = new LinkedList();
                    while (cursor.moveToNext()) {
                        String string = cursor.getString(0);
                        if (!TextUtils.isEmpty(string)) {
                            linkedList.add(string);
                        } else {
                            String string2 = cursor.getString(1);
                            if (!TextUtils.isEmpty(string2)) {
                                linkedList.add(string2);
                            } else {
                                DefaultLogger.w("GalleryFilePathResolver", "invalid columns found.");
                            }
                        }
                    }
                    return linkedList;
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public static class AlbumIdProcessor implements Function<List<Long>, List<String>> {
        public AlbumIdProcessor() {
        }

        @Override // java.util.function.Function
        public List<String> apply(List<Long> list) {
            if (!BaseMiscUtil.isValid(list)) {
                return Collections.emptyList();
            }
            return (List) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Media.URI, new String[]{"localFile", "thumbnailFile"}, "localGroupId IN (" + TextUtils.join(", ", list) + ")", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<String>>() { // from class: com.miui.gallery.storage.GalleryFilePathResolver.AlbumIdProcessor.1
                {
                    AlbumIdProcessor.this = this;
                }

                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public List<String> mo1808handle(Cursor cursor) {
                    if (cursor == null || cursor.getCount() <= 0) {
                        return Collections.emptyList();
                    }
                    LinkedList linkedList = new LinkedList();
                    while (cursor.moveToNext()) {
                        String string = cursor.getString(0);
                        if (!TextUtils.isEmpty(string)) {
                            linkedList.add(string);
                        } else {
                            String string2 = cursor.getString(1);
                            if (!TextUtils.isEmpty(string2)) {
                                linkedList.add(string2);
                            } else {
                                DefaultLogger.w("GalleryFilePathResolver", "invalid columns found.");
                            }
                        }
                    }
                    return linkedList;
                }
            });
        }
    }

    /* loaded from: classes2.dex */
    public static class BaseDataItemProcessor implements Function<List<BaseDataItem>, List<String>> {
        public BaseDataItemProcessor() {
        }

        @Override // java.util.function.Function
        public List<String> apply(List<BaseDataItem> list) {
            if (!BaseMiscUtil.isValid(list)) {
                return Collections.emptyList();
            }
            return (List) list.stream().map(GalleryFilePathResolver$BaseDataItemProcessor$$ExternalSyntheticLambda0.INSTANCE).filter(GalleryFilePathResolver$BaseDataItemProcessor$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
        }

        public static /* synthetic */ String lambda$apply$0(BaseDataItem baseDataItem) {
            if (baseDataItem == null) {
                return null;
            }
            return baseDataItem.getPathDisplayBetter();
        }

        public static /* synthetic */ boolean lambda$apply$1(String str) {
            return !TextUtils.isEmpty(str);
        }
    }

    /* loaded from: classes2.dex */
    public static class FileUriProcessor implements Function<List<Uri>, List<String>> {
        public FileUriProcessor() {
        }

        @Override // java.util.function.Function
        public List<String> apply(List<Uri> list) {
            if (!BaseMiscUtil.isValid(list)) {
                return Collections.emptyList();
            }
            return (List) list.stream().map(GalleryFilePathResolver$FileUriProcessor$$ExternalSyntheticLambda0.INSTANCE).filter(GalleryFilePathResolver$FileUriProcessor$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
        }

        public static /* synthetic */ String lambda$apply$0(Uri uri) {
            if (uri == null) {
                return null;
            }
            return uri.getPath();
        }

        public static /* synthetic */ boolean lambda$apply$1(String str) {
            return !TextUtils.isEmpty(str);
        }
    }

    /* loaded from: classes2.dex */
    public static class ContentUriProcessor implements Function<List<Uri>, List<String>> {
        public static /* synthetic */ String $r8$lambda$WIHV2Gifbu7bjCFpzrPTwzy3DS0(ContentUriProcessor contentUriProcessor, Uri uri) {
            return contentUriProcessor.lambda$apply$0(uri);
        }

        public ContentUriProcessor() {
        }

        @Override // java.util.function.Function
        public List<String> apply(List<Uri> list) {
            if (!BaseMiscUtil.isValid(list)) {
                return Collections.emptyList();
            }
            return (List) list.stream().map(new Function() { // from class: com.miui.gallery.storage.GalleryFilePathResolver$ContentUriProcessor$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return GalleryFilePathResolver.ContentUriProcessor.$r8$lambda$WIHV2Gifbu7bjCFpzrPTwzy3DS0(GalleryFilePathResolver.ContentUriProcessor.this, (Uri) obj);
                }
            }).filter(GalleryFilePathResolver$ContentUriProcessor$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
        }

        public /* synthetic */ String lambda$apply$0(Uri uri) {
            if (uri == null) {
                return null;
            }
            return (String) GalleryUtils.safeQuery(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null, new GalleryUtils.QueryHandler<String>() { // from class: com.miui.gallery.storage.GalleryFilePathResolver.ContentUriProcessor.1
                {
                    ContentUriProcessor.this = this;
                }

                @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
                /* renamed from: handle */
                public String mo1712handle(Cursor cursor) {
                    if (cursor == null || !cursor.moveToFirst()) {
                        return null;
                    }
                    return cursor.getString(0);
                }
            });
        }

        public static /* synthetic */ boolean lambda$apply$1(String str) {
            return !TextUtils.isEmpty(str);
        }
    }
}
