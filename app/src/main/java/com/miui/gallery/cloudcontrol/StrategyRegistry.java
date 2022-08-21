package com.miui.gallery.cloudcontrol;

import com.miui.gallery.cloudcontrol.strategies.AlbumTabToolsStrategy;
import com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy;
import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.cloudcontrol.strategies.BackupPolicisStrategy;
import com.miui.gallery.cloudcontrol.strategies.CloudGuideStrategy;
import com.miui.gallery.cloudcontrol.strategies.ComponentsStrategy;
import com.miui.gallery.cloudcontrol.strategies.CreationStrategy;
import com.miui.gallery.cloudcontrol.strategies.DataLoadStrategy;
import com.miui.gallery.cloudcontrol.strategies.HiddenAlbumsStrategy;
import com.miui.gallery.cloudcontrol.strategies.IgnoreAlbumsStrategy;
import com.miui.gallery.cloudcontrol.strategies.LocationStrategy;
import com.miui.gallery.cloudcontrol.strategies.MacaronStrategy;
import com.miui.gallery.cloudcontrol.strategies.MediaFeatureCalculationStrategy;
import com.miui.gallery.cloudcontrol.strategies.MigrateStrategy;
import com.miui.gallery.cloudcontrol.strategies.OnlineVideoStrategy;
import com.miui.gallery.cloudcontrol.strategies.PhotoPrintStrategy;
import com.miui.gallery.cloudcontrol.strategies.RecommendStrategy;
import com.miui.gallery.cloudcontrol.strategies.ScannerStrategy;
import com.miui.gallery.cloudcontrol.strategies.SceneTagStructureStrategy;
import com.miui.gallery.cloudcontrol.strategies.SearchStrategy;
import com.miui.gallery.cloudcontrol.strategies.ServerReservedAlbumNamesStrategy;
import com.miui.gallery.cloudcontrol.strategies.ServerUnModifyAlbumsStrategy;
import com.miui.gallery.cloudcontrol.strategies.SlimEntrancesStrategy;
import com.miui.gallery.cloudcontrol.strategies.SyncStrategy;
import com.miui.gallery.cloudcontrol.strategies.ThirdAlbumsSortStrategy;
import com.miui.gallery.cloudcontrol.strategies.UploadSupportedFileTypeStrategy;
import com.miui.gallery.cloudcontrol.strategies.WhiteAlbumsStrategy;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class StrategyRegistry {
    public final Map<String, Class> mRegistry;

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final StrategyRegistry INSTANCE = new StrategyRegistry();
    }

    public StrategyRegistry() {
        HashMap hashMap = new HashMap(27);
        this.mRegistry = hashMap;
        hashMap.put("backup-policies", BackupPolicisStrategy.class);
        hashMap.put("photo-print", PhotoPrintStrategy.class);
        hashMap.put("server-reserved-album-names", ServerReservedAlbumNamesStrategy.class);
        hashMap.put("components-list", ComponentsStrategy.class);
        hashMap.put("recommendation", RecommendStrategy.class);
        hashMap.put("scenario-rules", AssistantScenarioStrategy.class);
        hashMap.put(MiStat.Event.SEARCH, SearchStrategy.class);
        hashMap.put("slim-entrances", SlimEntrancesStrategy.class);
        hashMap.put("hidden-albums", HiddenAlbumsStrategy.class);
        hashMap.put("media_scanner", ScannerStrategy.class);
        hashMap.put("server-unmodify-albums", ServerUnModifyAlbumsStrategy.class);
        hashMap.put("creation", CreationStrategy.class);
        hashMap.put("migrate-list", MigrateStrategy.class);
        hashMap.put("online-video", OnlineVideoStrategy.class);
        hashMap.put("upload-supported-file-types", UploadSupportedFileTypeStrategy.class);
        hashMap.put("album-tab-tools", AlbumTabToolsStrategy.class);
        hashMap.put("sync", SyncStrategy.class);
        hashMap.put("third-album-sorts", ThirdAlbumsSortStrategy.class);
        hashMap.put("album-list", AlbumsStrategy.class);
        hashMap.put("macaron", MacaronStrategy.class);
        hashMap.put("scene_tags_structure", SceneTagStructureStrategy.class);
        hashMap.put("load_strategy", DataLoadStrategy.class);
        hashMap.put("cloud-guide", CloudGuideStrategy.class);
        hashMap.put("location", LocationStrategy.class);
        hashMap.put("white-album-list", WhiteAlbumsStrategy.class);
        hashMap.put("ignore-albums", IgnoreAlbumsStrategy.class);
        hashMap.put("media-feature-calculation", MediaFeatureCalculationStrategy.class);
    }

    public static StrategyRegistry getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Class query(String str) {
        return this.mRegistry.get(str);
    }
}
