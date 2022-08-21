package cn.kuaipan.android.kss.upload;

import android.util.Log;
import cn.kuaipan.android.kss.IKssUploadRequestResult;
import cn.kuaipan.android.kss.KssDef;
import cn.kuaipan.android.utils.OAuthTimeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class KssUploadInfo implements KssDef {
    public boolean mBroken;
    public ServerExpect mExpectInfo;
    public final UploadFileInfo mFileInfo;
    public final long mGenerateTime;
    public long mMaxChunkSize;
    public final IKssUploadRequestResult mRequestResult;
    public String mUploadId;

    public KssUploadInfo(UploadFileInfo uploadFileInfo, IKssUploadRequestResult iKssUploadRequestResult) {
        this(uploadFileInfo, iKssUploadRequestResult, OAuthTimeUtils.currentTime());
    }

    public KssUploadInfo(UploadFileInfo uploadFileInfo, IKssUploadRequestResult iKssUploadRequestResult, long j) {
        this.mBroken = false;
        this.mMaxChunkSize = 4194304L;
        this.mExpectInfo = null;
        this.mFileInfo = uploadFileInfo;
        this.mRequestResult = iKssUploadRequestResult;
        this.mGenerateTime = j;
    }

    public UploadFileInfo getFileInfo() {
        return this.mFileInfo;
    }

    public IKssUploadRequestResult getRequestResult() {
        return this.mRequestResult;
    }

    public boolean isCompleted() {
        IKssUploadRequestResult iKssUploadRequestResult = this.mRequestResult;
        return iKssUploadRequestResult != null && iKssUploadRequestResult.isCompleted();
    }

    public long getGenerateTime() {
        return this.mGenerateTime;
    }

    public String getCommitString() {
        JSONObject jSONObject;
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("file_meta", this.mRequestResult.getFileMeta());
            int blockCount = this.mRequestResult.getBlockCount();
            JSONArray jSONArray = new JSONArray();
            if (this.mRequestResult != null && blockCount > 0) {
                for (int i = 0; i < blockCount; i++) {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("commit_meta", this.mRequestResult.getBlock(i).meta);
                    jSONArray.put(jSONObject3);
                }
            }
            jSONObject2.put("commit_metas", jSONArray);
            jSONObject = jSONObject2;
        } catch (JSONException unused) {
            Log.w("KssUploadInfo", "Failed generate Json String for UploadRequestResult");
            jSONObject = null;
        }
        return String.valueOf(jSONObject);
    }

    public boolean isBroken() {
        return this.mBroken;
    }

    public void markBroken() {
        this.mBroken = true;
    }

    public String getUploadId() {
        return this.mUploadId;
    }

    public void setUploadId(String str) {
        this.mUploadId = str;
    }

    public long getMaxChunkSize() {
        return this.mMaxChunkSize;
    }

    public void setMaxChunkSize(long j) {
        this.mMaxChunkSize = j;
    }
}
