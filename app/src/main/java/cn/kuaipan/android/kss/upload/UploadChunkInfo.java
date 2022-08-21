package cn.kuaipan.android.kss.upload;

import cn.kuaipan.android.kss.KssDef;
import cn.kuaipan.android.utils.ApiDataHelper;
import java.util.HashSet;
import java.util.Map;

/* loaded from: classes.dex */
public class UploadChunkInfo implements KssDef {
    public static final HashSet<String> sReRequestSet;
    public static final HashSet<String> sReTrySet;
    public static final HashSet<String> sSessionExpiredSet;
    public final String commit_meta;
    public ServerExpect expect_info;
    public long left_bytes;
    public long next_pos;
    public final String stat;
    public final String upload_id;

    public UploadChunkInfo(long j, long j2, String str) {
        this.stat = "CONTINUE_UPLOAD";
        this.next_pos = j;
        this.left_bytes = j2;
        this.upload_id = str;
        this.commit_meta = null;
    }

    public UploadChunkInfo(Map<String, Object> map) {
        this.stat = ApiDataHelper.asString(map, "stat");
        this.next_pos = ApiDataHelper.asNumber(map.get("next_pos"), -1).longValue();
        this.left_bytes = ApiDataHelper.asNumber(map.get("left_bytes"), -1).longValue();
        this.upload_id = ApiDataHelper.asString(map, "upload_id");
        this.commit_meta = ApiDataHelper.asString(map, "commit_meta");
    }

    public boolean isComplete() {
        return "BLOCK_COMPLETED".equalsIgnoreCase(this.stat);
    }

    public boolean isContinue() {
        return "CONTINUE_UPLOAD".equalsIgnoreCase(this.stat);
    }

    static {
        HashSet<String> hashSet = new HashSet<>();
        sReRequestSet = hashSet;
        HashSet<String> hashSet2 = new HashSet<>();
        sReTrySet = hashSet2;
        HashSet<String> hashSet3 = new HashSet<>();
        sSessionExpiredSet = hashSet3;
        hashSet.add("ERR_INVALID_FILE_META");
        hashSet.add("ERR_INVALID_BLOCK_META");
        hashSet.add("ERR_INVALID_UPLOAD_ID");
        hashSet.add("ERR_INVALID_CHUNK_POS");
        hashSet.add("ERR_INVALID_CHUNK_SIZE");
        hashSet.add("ERR_CHUNK_OUT_OF_RANGE");
        hashSet2.add("ERR_CHUNK_CORRUPTED");
        hashSet2.add("ERR_SERVER_EXCEPTION");
        hashSet2.add("ERR_STORAGE_REQUEST_ERROR");
        hashSet2.add("ERR_STORAGE_REQUEST_FAILED");
        hashSet3.add("ERR_INVALID_FILE_META");
        hashSet3.add("ERR_INVALID_BLOCK_META");
        hashSet3.add("ERR_INVALID_UPLOAD_ID");
    }

    public boolean canRetry() {
        String str = this.stat;
        return sReTrySet.contains(str == null ? null : str.toUpperCase());
    }

    public boolean needBlockRetry() {
        return "ERR_BLOCK_CORRUPTED".equalsIgnoreCase(this.stat);
    }

    public boolean isSessionExpired() {
        String str = this.stat;
        return sSessionExpiredSet.contains(str == null ? null : str.toUpperCase());
    }
}
