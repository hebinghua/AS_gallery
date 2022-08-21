package cn.wps.kmo.kmoservice_sdk.common;

import android.net.Uri;
import cn.wps.kmo.kmoservice_sdk.utils.SdkUtils;
import java.util.List;

/* loaded from: classes.dex */
public class TaskData {
    public String appType;
    public SdkUtils.ICallback mCallback;
    public List<Uri> mImgPaths;
    public int mMode;
    public String taskName;

    public TaskData(List<Uri> list, int i, SdkUtils.ICallback iCallback) {
        this.mImgPaths = list;
        this.mMode = i;
        this.mCallback = iCallback;
    }

    public void setAppType(String str) {
        this.appType = str;
    }

    public void setTaskName(String str) {
        this.taskName = str;
    }
}
