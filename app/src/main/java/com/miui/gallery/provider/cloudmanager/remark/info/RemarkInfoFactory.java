package com.miui.gallery.provider.cloudmanager.remark.info;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public class RemarkInfoFactory {
    public static long inventedId(long j) {
        return j + 2147483647L;
    }

    public static IRemarkInfo converterInfo(MediaRemarkEntity mediaRemarkEntity) {
        IRemarkInfo moveRemarkInfo;
        switch (mediaRemarkEntity.mMethodType) {
            case 1:
                moveRemarkInfo = new MoveRemarkInfo();
                break;
            case 2:
                moveRemarkInfo = new CopyRemarkInfo();
                break;
            case 3:
                moveRemarkInfo = new DeleteRemarkInfo();
                break;
            case 4:
                moveRemarkInfo = new RenameRemarkInfo();
                break;
            case 5:
                moveRemarkInfo = new EditDateTimeRemarkInfo();
                break;
            case 6:
                moveRemarkInfo = new RemoveSecretRemarkInfo();
                break;
            case 7:
                moveRemarkInfo = new MoveCloudMediaRemarkInfo();
                break;
            default:
                throw new IllegalArgumentException("MethodType error!");
        }
        moveRemarkInfo.setCloudId(mediaRemarkEntity.mCloudId);
        moveRemarkInfo.setOperationType(mediaRemarkEntity.mOperationType);
        moveRemarkInfo.setCheckValues(mediaRemarkEntity.mContentValuesJson);
        return moveRemarkInfo;
    }

    public static IRemarkInfo createMoveRemarkInfo(long j, String str, String str2) {
        MoveRemarkInfo moveRemarkInfo = new MoveRemarkInfo();
        moveRemarkInfo.setCloudId(j);
        moveRemarkInfo.setOperationType(1);
        moveRemarkInfo.setCheckValues(moveRemarkInfo.buildValues(str, str2));
        return moveRemarkInfo;
    }

    public static IRemarkInfo createCopyRemarkInfo(long j, String str, String str2) {
        CopyRemarkInfo copyRemarkInfo = new CopyRemarkInfo();
        copyRemarkInfo.setCloudId(j);
        copyRemarkInfo.setOperationType(1);
        copyRemarkInfo.setCheckValues(copyRemarkInfo.buildValues(str, str2));
        return copyRemarkInfo;
    }

    public static IRemarkInfo createDeleteRemarkInfo(long j, String str, String str2) {
        DeleteRemarkInfo deleteRemarkInfo = new DeleteRemarkInfo();
        deleteRemarkInfo.setCloudId(j);
        deleteRemarkInfo.setOperationType(1);
        deleteRemarkInfo.setCheckValues(deleteRemarkInfo.buildValues(str, str2));
        return deleteRemarkInfo;
    }

    public static IRemarkInfo createRenameRemarkInfo(long j, String str, String str2) {
        RenameRemarkInfo renameRemarkInfo = new RenameRemarkInfo();
        renameRemarkInfo.setCloudId(j);
        renameRemarkInfo.setOperationType(1);
        renameRemarkInfo.setCheckValues(renameRemarkInfo.buildValues(str, str2));
        return renameRemarkInfo;
    }

    public static IRemarkInfo createEditDateTimeRemarkInfo(long j, String str, String str2) {
        EditDateTimeRemarkInfo editDateTimeRemarkInfo = new EditDateTimeRemarkInfo();
        editDateTimeRemarkInfo.setCloudId(j);
        editDateTimeRemarkInfo.setOperationType(1);
        editDateTimeRemarkInfo.setCheckValues(editDateTimeRemarkInfo.buildValues(str, str2));
        return editDateTimeRemarkInfo;
    }

    public static IRemarkInfo createMoveCloudMediaRemarkInfo(long j, String str, String str2) {
        MoveCloudMediaRemarkInfo moveCloudMediaRemarkInfo = new MoveCloudMediaRemarkInfo();
        moveCloudMediaRemarkInfo.setCloudId(inventedId(j));
        moveCloudMediaRemarkInfo.setOperationType(1);
        moveCloudMediaRemarkInfo.setCheckValues(moveCloudMediaRemarkInfo.buildValues(getInventedPath(j, str), getInventedPath(j, str2)));
        return moveCloudMediaRemarkInfo;
    }

    public static String getInventedPath(long j, String str) {
        if (!TextUtils.isEmpty(str)) {
            return str + inventedId(j);
        }
        return str;
    }
}
