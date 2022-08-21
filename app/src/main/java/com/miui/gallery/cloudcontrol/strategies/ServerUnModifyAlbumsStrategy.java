package com.miui.gallery.cloudcontrol.strategies;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ServerUnModifyAlbumsStrategy extends BaseStrategy {
    @SerializedName("albums")
    private List<ServerUnModifyAlbum> mAlbums;

    public static ServerUnModifyAlbumsStrategy createDefault() {
        ServerUnModifyAlbumsStrategy serverUnModifyAlbumsStrategy = new ServerUnModifyAlbumsStrategy();
        ArrayList arrayList = new ArrayList();
        serverUnModifyAlbumsStrategy.mAlbums = arrayList;
        arrayList.add(new ServerUnModifyAlbum(1L, "untitled"));
        serverUnModifyAlbumsStrategy.mAlbums.add(new ServerUnModifyAlbum(2L, "snapshot"));
        return serverUnModifyAlbumsStrategy;
    }

    public String getServerAlbumName(long j) {
        List<ServerUnModifyAlbum> list = this.mAlbums;
        if (list != null) {
            for (ServerUnModifyAlbum serverUnModifyAlbum : list) {
                if (serverUnModifyAlbum.getServerId() == j) {
                    return serverUnModifyAlbum.getName();
                }
            }
            return null;
        }
        return null;
    }

    public boolean containsName(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        List<ServerUnModifyAlbum> list = this.mAlbums;
        if (list == null) {
            return false;
        }
        for (ServerUnModifyAlbum serverUnModifyAlbum : list) {
            if (serverUnModifyAlbum.getName().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public List<ServerUnModifyAlbum> getAlbums() {
        return this.mAlbums;
    }

    public String toString() {
        return "ServerUnModifyAlbumsStrategy{mAlbums=" + this.mAlbums + '}';
    }

    /* loaded from: classes.dex */
    public static class ServerUnModifyAlbum {
        @SerializedName("name")
        private String mName;
        @SerializedName("serverId")
        private long mServerId;

        public ServerUnModifyAlbum(long j, String str) {
            this.mServerId = j;
            this.mName = str;
        }

        public long getServerId() {
            return this.mServerId;
        }

        public String getName() {
            return this.mName;
        }

        public String toString() {
            return "ServerUnModifyAlbum{mServerId=" + this.mServerId + ", mName=" + this.mName + '}';
        }
    }
}
