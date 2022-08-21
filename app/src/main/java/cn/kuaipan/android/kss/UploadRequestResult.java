package cn.kuaipan.android.kss;

import android.util.Log;
import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.kss.IKssUploadRequestResult;
import cn.kuaipan.android.utils.ApiDataHelper;
import cn.kuaipan.android.utils.Encode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class UploadRequestResult implements IKssUploadRequestResult, KssDef {
    public IKssUploadRequestResult.Block[] blocks;
    public final String file_meta;
    public String[] node_urls;
    public final byte[] secure_key;

    public UploadRequestResult(Map<String, Object> map) throws KscException {
        this.secure_key = Encode.hexStringToByteArray(ApiDataHelper.asString(map, "secure_key"));
        this.file_meta = ApiDataHelper.asString(map, "file_meta");
        Collection<Map> collection = (Collection) map.get("block_metas");
        int i = 0;
        if (collection != null) {
            this.blocks = new IKssUploadRequestResult.Block[collection.size()];
            int i2 = 0;
            for (Map map2 : collection) {
                boolean z = ApiDataHelper.asNumber(map2.get("is_existed"), 0).intValue() != 0;
                this.blocks[i2] = new IKssUploadRequestResult.Block(ApiDataHelper.asString(map2, z ? "commit_meta" : "block_meta"), z);
                i2++;
            }
        }
        Collection<String> collection2 = (Collection) map.get("node_urls");
        if (collection2 != null) {
            this.node_urls = new String[collection2.size()];
            for (String str : collection2) {
                this.node_urls[i] = str;
                i++;
            }
        }
    }

    @Override // cn.kuaipan.android.kss.IKssUploadRequestResult
    public byte[] getSecureKey() {
        return this.secure_key;
    }

    @Override // cn.kuaipan.android.kss.IKssUploadRequestResult
    public String[] getNodeUrls() {
        return this.node_urls;
    }

    @Override // cn.kuaipan.android.kss.IKssUploadRequestResult
    public String getFileMeta() {
        return this.file_meta;
    }

    @Override // cn.kuaipan.android.kss.IKssUploadRequestResult
    public int getBlockCount() {
        IKssUploadRequestResult.Block[] blockArr = this.blocks;
        if (blockArr == null) {
            return 0;
        }
        return blockArr.length;
    }

    @Override // cn.kuaipan.android.kss.IKssUploadRequestResult
    public IKssUploadRequestResult.Block getBlock(int i) {
        IKssUploadRequestResult.Block[] blockArr = this.blocks;
        if (blockArr == null || i >= blockArr.length) {
            return null;
        }
        return blockArr[i];
    }

    @Override // cn.kuaipan.android.kss.IKssUploadRequestResult
    public boolean isCompleted() {
        IKssUploadRequestResult.Block[] blockArr = this.blocks;
        if (blockArr == null) {
            return true;
        }
        for (IKssUploadRequestResult.Block block : blockArr) {
            if (!block.exist) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        JSONObject jSONObject;
        Object jSONArray;
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("secure_key", Encode.byteArrayToHexString(this.secure_key));
            jSONObject2.put("file_meta", this.file_meta);
            if (this.node_urls != null) {
                jSONArray = new JSONArray((Collection) Arrays.asList(this.node_urls));
            } else {
                jSONArray = new JSONArray();
            }
            jSONObject2.put("node_urls", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            IKssUploadRequestResult.Block[] blockArr = this.blocks;
            if (blockArr != null) {
                for (IKssUploadRequestResult.Block block : blockArr) {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("is_existed", block.exist ? 1 : 0);
                    if (block.exist) {
                        jSONObject3.put("commit_meta", block.meta);
                    } else {
                        jSONObject3.put("block_meta", block.meta);
                    }
                    jSONArray2.put(jSONObject3);
                }
            }
            jSONObject2.put("block_metas", jSONArray2);
            jSONObject = jSONObject2;
        } catch (JSONException unused) {
            Log.w("UploadRequestResult", "Failed generate Json String for UploadRequestResult");
            jSONObject = null;
        }
        return String.valueOf(jSONObject);
    }
}
