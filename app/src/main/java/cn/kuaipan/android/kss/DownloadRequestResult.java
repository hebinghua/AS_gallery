package cn.kuaipan.android.kss;

import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.kss.IKssDownloadRequestResult;
import cn.kuaipan.android.utils.ApiDataHelper;
import cn.kuaipan.android.utils.Encode;
import com.baidu.platform.comapi.map.MapBundleKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DownloadRequestResult implements IKssDownloadRequestResult, KssDef {
    public IKssDownloadRequestResult.Block[] blocks;
    public byte[] secure_key;
    public final String stat;

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public long getModifyTime() {
        return -1L;
    }

    public DownloadRequestResult(Map<String, Object> map) throws KscException {
        String asString = ApiDataHelper.asString(map, "stat");
        this.stat = asString;
        if (!"OK".equalsIgnoreCase(asString)) {
            return;
        }
        this.secure_key = Encode.hexStringToByteArray(ApiDataHelper.asString(map, "secure_key"));
        Collection<Map> collection = (Collection) map.get("blocks");
        if (collection == null) {
            return;
        }
        this.blocks = new IKssDownloadRequestResult.Block[collection.size()];
        int i = 0;
        for (Map map2 : collection) {
            String asString2 = ApiDataHelper.asString(map2, "sha1");
            long longValue = ApiDataHelper.asNumber(map2.get(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE), 0).longValue();
            String[] strArr = null;
            Collection<String> collection2 = (Collection) map2.get("urls");
            if (collection2 != null) {
                strArr = new String[collection2.size()];
                int i2 = 0;
                for (String str : collection2) {
                    strArr[i2] = str;
                    i2++;
                }
            }
            this.blocks[i] = new IKssDownloadRequestResult.Block(asString2, strArr, longValue);
            i++;
        }
    }

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public int getStatus() {
        return "OK".equalsIgnoreCase(this.stat) ? 0 : -1;
    }

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public String getMessage() {
        return this.stat;
    }

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public String getHash() {
        StringBuilder sb = new StringBuilder();
        IKssDownloadRequestResult.Block[] blockArr = this.blocks;
        sb.append(blockArr == null ? 0 : blockArr.length);
        sb.append(CoreConstants.COLON_CHAR);
        sb.append(getTotalSize());
        sb.append(CoreConstants.COLON_CHAR);
        StringBuilder sb2 = new StringBuilder();
        IKssDownloadRequestResult.Block[] blockArr2 = this.blocks;
        if (blockArr2 != null) {
            for (IKssDownloadRequestResult.Block block : blockArr2) {
                sb2.append(block.sha1);
            }
        }
        sb.append(Encode.MD5Encode(sb2.toString().getBytes()));
        return sb.toString();
    }

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public byte[] getSecureKey() {
        return this.secure_key;
    }

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public int getBlockCount() {
        IKssDownloadRequestResult.Block[] blockArr = this.blocks;
        if (blockArr == null) {
            return -1;
        }
        return blockArr.length;
    }

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public IKssDownloadRequestResult.Block getBlock(int i) {
        return this.blocks[i];
    }

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public String[] getBlockUrls(long j) {
        long j2 = 0;
        if (j < 0 || this.blocks == null) {
            return null;
        }
        int i = 0;
        while (true) {
            IKssDownloadRequestResult.Block[] blockArr = this.blocks;
            if (i >= blockArr.length) {
                return null;
            }
            long j3 = blockArr[i].size + j2;
            if (j >= j2 && j < j3) {
                return blockArr[i].urls;
            }
            i++;
            j2 = j3;
        }
    }

    public String toString() {
        JSONObject jSONObject;
        JSONArray jSONArray;
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("stat", this.stat);
            jSONObject2.put("secure_key", Encode.byteArrayToHexString(this.secure_key));
            JSONArray jSONArray2 = new JSONArray();
            IKssDownloadRequestResult.Block[] blockArr = this.blocks;
            if (blockArr != null) {
                for (IKssDownloadRequestResult.Block block : blockArr) {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("sha1", block.sha1);
                    jSONObject3.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, block.size);
                    if (block.urls != null) {
                        jSONArray = new JSONArray((Collection) Arrays.asList(block.urls));
                    } else {
                        jSONArray = new JSONArray();
                    }
                    jSONObject3.put("urls", jSONArray);
                    jSONArray2.put(jSONObject3);
                }
            }
            jSONObject2.put("blocks", jSONArray2);
            jSONObject = jSONObject2;
        } catch (JSONException unused) {
            Log.w("DownloadRequestResult", "Failed generate Json String for UploadRequestResult");
            jSONObject = null;
        }
        return String.valueOf(jSONObject);
    }

    @Override // cn.kuaipan.android.kss.IKssDownloadRequestResult
    public long getTotalSize() {
        IKssDownloadRequestResult.Block[] blockArr = this.blocks;
        long j = 0;
        if (blockArr == null) {
            return 0L;
        }
        for (IKssDownloadRequestResult.Block block : blockArr) {
            j += block.size;
        }
        return j;
    }
}
