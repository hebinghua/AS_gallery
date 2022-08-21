package com.miui.gallery.scanner.core.task.convertor.internal.base;

import android.content.Context;
import android.database.Cursor;
import com.google.common.collect.Lists;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.ScanTaskConfigFactory;
import com.miui.gallery.scanner.core.task.semi.ImprintedScanPathsTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes2.dex */
public class AllAlbumDirectoriesScanner implements IScanner {
    public static final String[] FIND_OLD_PROJECTION = {"localPath"};

    @Override // com.miui.gallery.scanner.core.task.convertor.internal.base.IScanner
    public SemiScanTask[] createTasks(Context context) {
        TaskDeDupUtil taskDeDupUtil = new TaskDeDupUtil(context);
        ScanTaskConfig scanTaskConfig = ScanTaskConfigFactory.get(2);
        return new SemiScanTask[]{scanCamera(context, taskDeDupUtil, scanTaskConfig), scanExists(context, taskDeDupUtil, scanTaskConfig), scanWhiteList(context, taskDeDupUtil, scanTaskConfig)};
    }

    public static SemiScanTask scanCamera(Context context, TaskDeDupUtil taskDeDupUtil, ScanTaskConfig scanTaskConfig) {
        return ImprintedScanPathsTask.create(context, taskDeDupUtil.filter(Lists.newArrayList(MIUIStorageConstants.DIRECTORY_CAMERA_PATH), true), scanTaskConfig, "scanCamera");
    }

    public static SemiScanTask scanExists(Context context, TaskDeDupUtil taskDeDupUtil, ScanTaskConfig scanTaskConfig) {
        return ImprintedScanPathsTask.create(context, taskDeDupUtil.filter((ArrayList) SafeDBUtil.safeQuery(context, GalleryContract.Album.URI, FIND_OLD_PROJECTION, "(localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom'))) AND  ( attributes & 2048 = 0)", (String[]) null, "dateModified DESC", new SafeDBUtil.QueryHandler<ArrayList<String>>() { // from class: com.miui.gallery.scanner.core.task.convertor.internal.base.AllAlbumDirectoriesScanner.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public ArrayList<String> mo1808handle(Cursor cursor) {
                if (cursor != null) {
                    ArrayList<String> arrayList = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        arrayList.add(cursor.getString(0));
                    }
                    return arrayList;
                }
                return null;
            }
        }), false), scanTaskConfig, "scanExists");
    }

    public static SemiScanTask scanWhiteList(Context context, TaskDeDupUtil taskDeDupUtil, ScanTaskConfig scanTaskConfig) {
        return ImprintedScanPathsTask.create(context, taskDeDupUtil.filter(CloudControlStrategyHelper.getAlbumsInWhiteList(), true), scanTaskConfig, "scanWhiteList");
    }

    /* loaded from: classes2.dex */
    public static class TaskDeDupUtil {
        public static final Comparator<String> DIRECTORY_COMPARATOR = new Comparator<String>() { // from class: com.miui.gallery.scanner.core.task.convertor.internal.base.AllAlbumDirectoriesScanner.TaskDeDupUtil.1
            @Override // java.util.Comparator
            public int compare(String str, String str2) {
                return -Long.compare(new File(str).lastModified(), new File(str2).lastModified());
            }
        };
        public final List<Integer> mBucketIds = new ArrayList();
        public final Context mContext;

        public TaskDeDupUtil(Context context) {
            this.mContext = context;
        }

        public List<String> filter(List<String> list, boolean z) {
            ArrayList arrayList = new ArrayList();
            if (!BaseMiscUtil.isValid(list)) {
                return Collections.emptyList();
            }
            for (String str : list) {
                String[] absolutePath = StorageUtils.getAbsolutePath(this.mContext, StorageUtils.ensureCommonRelativePath(str));
                if (absolutePath != null) {
                    for (String str2 : absolutePath) {
                        File file = new File(str2);
                        if ((!z || file.exists()) && (!file.exists() || file.isDirectory())) {
                            int bucketID = BaseFileUtils.getBucketID(str2);
                            if (!this.mBucketIds.contains(Integer.valueOf(bucketID))) {
                                arrayList.add(str2);
                                this.mBucketIds.add(Integer.valueOf(bucketID));
                            }
                        }
                    }
                }
            }
            Collections.sort(arrayList, DIRECTORY_COMPARATOR);
            return arrayList;
        }
    }
}
