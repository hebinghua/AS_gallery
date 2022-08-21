package com.miui.gallery.cloudcontrol.strategies;

import com.google.gson.annotations.SerializedName;
import com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class WhiteAlbumsStrategy extends BaseStrategy {
    @SerializedName("albums")
    private List<AlbumsStrategy.Album> mAlbums;
    public transient HashMap<String, AlbumsStrategy.Album> mAlbumsMap;
    public transient ArrayList<String> mWhiteList;

    @Override // com.miui.gallery.cloudcontrol.strategies.BaseStrategy
    public void doAdditionalProcessing() {
        if (BaseMiscUtil.isValid(this.mAlbums)) {
            HashMap<String, AlbumsStrategy.Album> hashMap = this.mAlbumsMap;
            if (hashMap == null) {
                this.mAlbumsMap = new HashMap<>();
            } else {
                hashMap.clear();
            }
            ArrayList<String> arrayList = this.mWhiteList;
            if (arrayList == null) {
                this.mWhiteList = new ArrayList<>();
            } else {
                arrayList.clear();
            }
            for (AlbumsStrategy.Album album : this.mAlbums) {
                if (album.getPath() != null) {
                    this.mAlbumsMap.put(album.getPath().toLowerCase(), album);
                    this.mWhiteList.add(album.getPath());
                }
            }
        }
    }

    public boolean isWhiteAlbum(String str) {
        ArrayList<String> arrayList = this.mWhiteList;
        if (arrayList != null && !arrayList.isEmpty()) {
            Iterator<String> it = this.mWhiteList.iterator();
            while (it.hasNext()) {
                if (it.next().equalsIgnoreCase(str)) {
                    return true;
                }
            }
        }
        return false;
    }
}
