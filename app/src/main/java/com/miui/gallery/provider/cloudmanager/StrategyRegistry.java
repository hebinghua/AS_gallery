package com.miui.gallery.provider.cloudmanager;

import com.miui.gallery.provider.cloudmanager.method.album.CreateAlbumMethod;
import com.miui.gallery.provider.cloudmanager.method.album.DeleteAlbumMethod;
import com.miui.gallery.provider.cloudmanager.method.album.DoChangeAlbumSortPositionMethod;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.provider.cloudmanager.method.album.RenameAlbumMethod;
import com.miui.gallery.provider.cloudmanager.method.album.SetAlbumAttributesMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.AddRemoveFavoriteMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.AddToAlbumMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.DeleteMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.EditPhotoDateTimeMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.rename.RenameMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.AddSecretMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.RemoveSecretMethod;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class StrategyRegistry {
    public final Map<String, Object> mMap;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final StrategyRegistry INSTANCE = new StrategyRegistry();
    }

    public StrategyRegistry() {
        this.mMap = new HashMap(13);
    }

    public static StrategyRegistry getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized Object get(String str) {
        Object obj;
        obj = this.mMap.get(str);
        if (obj == null) {
            char c = 65535;
            switch (str.hashCode()) {
                case -1403114652:
                    if (str.equals("set_album_attributes")) {
                        c = '\n';
                        break;
                    }
                    break;
                case -1366429410:
                    if (str.equals("update_photo_datetime")) {
                        c = 6;
                        break;
                    }
                    break;
                case -1335458389:
                    if (str.equals("delete")) {
                        c = 4;
                        break;
                    }
                    break;
                case -934594754:
                    if (str.equals("rename")) {
                        c = 7;
                        break;
                    }
                    break;
                case -732362532:
                    if (str.equals("replace_album_cover")) {
                        c = 3;
                        break;
                    }
                    break;
                case -434226469:
                    if (str.equals("delete_album")) {
                        c = 2;
                        break;
                    }
                    break;
                case 305017390:
                    if (str.equals("rename_album")) {
                        c = 0;
                        break;
                    }
                    break;
                case 792511881:
                    if (str.equals("add_to_album")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 947438425:
                    if (str.equals("add_remove_favorite")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1333032651:
                    if (str.equals("remove_secret")) {
                        c = '\f';
                        break;
                    }
                    break;
                case 1766199755:
                    if (str.equals("change_album_sort_position")) {
                        c = 5;
                        break;
                    }
                    break;
                case 1861604716:
                    if (str.equals("create_album")) {
                        c = '\t';
                        break;
                    }
                    break;
                case 2045774862:
                    if (str.equals("add_secret")) {
                        c = 11;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    obj = new RenameAlbumMethod();
                    this.mMap.put(str, obj);
                    break;
                case 1:
                    obj = new AddRemoveFavoriteMethod();
                    this.mMap.put(str, obj);
                    break;
                case 2:
                    obj = new DeleteAlbumMethod();
                    this.mMap.put(str, obj);
                    break;
                case 3:
                    obj = new DoReplaceAlbumCoverMethod();
                    this.mMap.put(str, obj);
                    break;
                case 4:
                    obj = new DeleteMethod();
                    this.mMap.put(str, obj);
                    break;
                case 5:
                    obj = new DoChangeAlbumSortPositionMethod();
                    this.mMap.put(str, obj);
                    break;
                case 6:
                    obj = new EditPhotoDateTimeMethod();
                    this.mMap.put(str, obj);
                    break;
                case 7:
                    obj = new RenameMethod();
                    this.mMap.put(str, obj);
                    break;
                case '\b':
                    obj = new AddToAlbumMethod();
                    this.mMap.put(str, obj);
                    break;
                case '\t':
                    obj = new CreateAlbumMethod();
                    this.mMap.put(str, obj);
                    break;
                case '\n':
                    obj = new SetAlbumAttributesMethod();
                    this.mMap.put(str, obj);
                    break;
                case 11:
                    obj = new AddSecretMethod();
                    this.mMap.put(str, obj);
                    break;
                case '\f':
                    obj = new RemoveSecretMethod();
                    this.mMap.put(str, obj);
                    break;
            }
        }
        return obj;
    }
}
