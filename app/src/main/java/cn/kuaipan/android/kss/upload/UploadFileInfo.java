package cn.kuaipan.android.kss.upload;

import android.text.TextUtils;
import android.util.Log;
import cn.kuaipan.android.exception.KscException;
import cn.kuaipan.android.exception.KscRuntimeException;
import cn.kuaipan.android.kss.KssDef;
import cn.kuaipan.android.utils.Encode;
import com.baidu.platform.comapi.map.MapBundleKey;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class UploadFileInfo implements KssDef {
    public final ArrayList<BlockInfo> mBlockInfos = new ArrayList<>();
    public String mSha1;

    /* loaded from: classes.dex */
    public static class BlockInfo {
        public final String md5;
        public final String sha1;
        public final int size;

        public BlockInfo(String str, String str2, int i) {
            this.sha1 = str;
            this.md5 = str2;
            this.size = i;
        }
    }

    public static UploadFileInfo getFileInfo(KssUploadFile kssUploadFile) throws KscException, InterruptedException {
        try {
            return getFileInfoInner(kssUploadFile.getInputStream(), kssUploadFile.filePath);
        } catch (IOException e) {
            throw KscException.newException(e, null);
        }
    }

    public static UploadFileInfo getFileInfoInner(InputStream inputStream, String str) throws KscException, InterruptedException {
        StringBuilder sb;
        boolean z;
        try {
            try {
                UploadFileInfo uploadFileInfo = new UploadFileInfo();
                MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                MessageDigest messageDigest2 = MessageDigest.getInstance("SHA1");
                MessageDigest messageDigest3 = MessageDigest.getInstance("MD5");
                byte[] bArr = new byte[8192];
                int i = 1;
                long j = 0;
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read < 0) {
                        break;
                    }
                    j += read;
                    messageDigest.update(bArr, 0, read);
                    long j2 = i * 4194304;
                    if (j < j2) {
                        messageDigest2.update(bArr, 0, read);
                        messageDigest3.update(bArr, 0, read);
                    } else {
                        int i2 = read - ((int) (j - j2));
                        i++;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("blockoffset: ");
                        sb2.append(i2);
                        sb2.append(" len: ");
                        sb2.append(read);
                        sb2.append(" pos: ");
                        sb2.append(j);
                        sb2.append(" blockIndex");
                        sb2.append(i);
                        sb2.append(bArr);
                        sb2.append(" blockOffset > input.length: ");
                        if (i2 > 8192) {
                            sb = sb2;
                            z = true;
                        } else {
                            sb = sb2;
                            z = false;
                        }
                        sb.append(z);
                        Log.d("UploadFileInfo", sb.toString());
                        messageDigest2.update(bArr, 0, i2);
                        messageDigest3.update(bArr, 0, i2);
                        uploadFileInfo.appendBlock(Encode.byteArrayToHexString(messageDigest2.digest()), Encode.byteArrayToHexString(messageDigest3.digest()), 4194304L);
                        if (read > i2) {
                            int i3 = read - i2;
                            messageDigest2.update(bArr, i2, i3);
                            messageDigest3.update(bArr, i2, i3);
                        }
                    }
                }
                long j3 = i * 4194304;
                if (j3 > j && j3 < j + 4194304) {
                    uploadFileInfo.appendBlock(Encode.byteArrayToHexString(messageDigest2.digest()), Encode.byteArrayToHexString(messageDigest3.digest()), j - ((i - 1) * 4194304));
                } else if (j == 0) {
                    throw new KscRuntimeException(500003, str + " read error.");
                }
                uploadFileInfo.setSha1(Encode.byteArrayToHexString(messageDigest.digest()));
                try {
                    inputStream.close();
                } catch (Throwable unused) {
                }
                return uploadFileInfo;
            } catch (IOException e) {
                throw KscException.newException(e, null);
            } catch (NoSuchAlgorithmException e2) {
                throw new KscRuntimeException(500005, e2);
            }
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (Throwable unused2) {
            }
            throw th;
        }
    }

    public UploadFileInfo() {
    }

    public UploadFileInfo(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.mSha1 = jSONObject.optString("sha1");
            JSONArray optJSONArray = jSONObject.optJSONArray("block_infos");
            if (optJSONArray == null) {
                return;
            }
            int length = optJSONArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject optJSONObject = optJSONArray.optJSONObject(i);
                String str2 = null;
                String optString = optJSONObject == null ? null : optJSONObject.optString("sha1");
                if (optJSONObject != null) {
                    str2 = optJSONObject.optString("md5");
                }
                int i2 = -1;
                if (optJSONObject != null) {
                    i2 = optJSONObject.optInt(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, -1);
                }
                if (!TextUtils.isEmpty(optString) && !TextUtils.isEmpty(str2) && i2 >= 0) {
                    appendBlock(optString, str2, i2);
                }
            }
        } catch (JSONException e) {
            Log.w("UploadFileInfo", "Failed parser UploadFileInfo from a String. The String:" + str, e);
        }
    }

    public final JSONArray getBlockInfos() {
        try {
            JSONArray jSONArray = new JSONArray();
            Iterator<BlockInfo> it = this.mBlockInfos.iterator();
            while (it.hasNext()) {
                BlockInfo next = it.next();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("sha1", next.sha1);
                jSONObject.put("md5", next.md5);
                jSONObject.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, next.size);
                jSONArray.put(jSONObject);
            }
            return jSONArray;
        } catch (Throwable unused) {
            Log.w("UploadFileInfo", "Failed generate Json String for UploadRequestInfo");
            return null;
        }
    }

    public void setSha1(String str) {
        this.mSha1 = str;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public void appendBlock(String str, String str2, long j) {
        this.mBlockInfos.add(new BlockInfo(str, str2, (int) j));
    }

    public BlockInfo getBlockInfo(int i) {
        if (i >= this.mBlockInfos.size()) {
            return null;
        }
        return this.mBlockInfos.get(i);
    }

    public String getKssString() {
        JSONArray blockInfos = getBlockInfos();
        if (blockInfos == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("block_infos", blockInfos);
            return jSONObject.toString();
        } catch (Throwable unused) {
            return String.valueOf(jSONObject);
        }
    }

    public String toString() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("block_infos", getBlockInfos());
            jSONObject.put("sha1", this.mSha1);
        } catch (Throwable unused) {
        }
        return String.valueOf(jSONObject);
    }
}
