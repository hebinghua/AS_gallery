package com.miui.gallery.vlog.clip;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IClipManager;
import com.miui.gallery.vlog.sdk.interfaces.ITransManager;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.tools.VlogUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class ClipMenuModel extends AndroidViewModel {
    public List<String> mAddedVideoClips;
    public IClipManager mClipManager;
    public ITransManager mTransResManager;
    public VlogModel mVlogModel;

    public ClipMenuModel(Application application) {
        super(application);
    }

    public IClipManager getNvsClipManager(Context context) {
        if (this.mClipManager == null) {
            this.mClipManager = (IClipManager) getVlogModel(context).getSdkManager().getManagerService(3);
        }
        return this.mClipManager;
    }

    public ITransManager getTransResManager(Context context) {
        if (this.mTransResManager == null) {
            this.mTransResManager = (ITransManager) getVlogModel(context).getSdkManager().getManagerService(9);
        }
        return this.mTransResManager;
    }

    public final VlogModel getVlogModel(Context context) {
        if (this.mVlogModel == null) {
            this.mVlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) context, VlogModel.class);
        }
        return this.mVlogModel;
    }

    public List<IVideoClip> getVideoClips(MiVideoSdkManager miVideoSdkManager) {
        return miVideoSdkManager.getVideoClips();
    }

    public boolean parseIntent(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        ArrayList<String> stringArrayList = intent.getExtras().getStringArrayList("tran_code_path");
        if (!BaseMiscUtil.isValid(stringArrayList)) {
            return false;
        }
        if (this.mAddedVideoClips == null) {
            this.mAddedVideoClips = new ArrayList();
        }
        this.mAddedVideoClips.clear();
        Iterator<String> it = stringArrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (!TextUtils.isEmpty(next)) {
                this.mAddedVideoClips.add(next);
                getVlogModel(context).addNewVideo(next);
            }
        }
        return !this.mAddedVideoClips.isEmpty();
    }

    public List<String> getAddedVideoClips() {
        return this.mAddedVideoClips;
    }
}
