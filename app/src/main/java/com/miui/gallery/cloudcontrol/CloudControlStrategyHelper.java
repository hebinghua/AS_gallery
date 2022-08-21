package com.miui.gallery.cloudcontrol;

import android.os.Build;
import com.miui.gallery.cloudcontrol.strategies.AlbumTabToolsStrategy;
import com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy;
import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.cloudcontrol.strategies.ComponentsStrategy;
import com.miui.gallery.cloudcontrol.strategies.CreationStrategy;
import com.miui.gallery.cloudcontrol.strategies.DataLoadStrategy;
import com.miui.gallery.cloudcontrol.strategies.HiddenAlbumsStrategy;
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
import com.miui.gallery.cloudcontrol.strategies.WhiteAlbumsStrategy;
import com.miui.gallery.net.library.LibraryStrategyUtils;
import com.miui.gallery.util.LazyValue;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class CloudControlStrategyHelper {
    public static LazyValue<Object, ServerUnModifyAlbumsStrategy> sServerUnModifyAlbumsStrategy = new LazyValue<Object, ServerUnModifyAlbumsStrategy>() { // from class: com.miui.gallery.cloudcontrol.CloudControlStrategyHelper.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public ServerUnModifyAlbumsStrategy mo1272onInit(Object obj) {
            ServerUnModifyAlbumsStrategy serverUnModifyAlbumsStrategy = (ServerUnModifyAlbumsStrategy) CloudControlManager.getInstance().queryFeatureStrategy("server-unmodify-albums");
            return (serverUnModifyAlbumsStrategy == null || serverUnModifyAlbumsStrategy.getAlbums() == null) ? ServerUnModifyAlbumsStrategy.createDefault() : serverUnModifyAlbumsStrategy;
        }
    };
    public static LazyValue<Object, ServerReservedAlbumNamesStrategy> sServerReservedAlbumNamesStrategy = new LazyValue<Object, ServerReservedAlbumNamesStrategy>() { // from class: com.miui.gallery.cloudcontrol.CloudControlStrategyHelper.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public ServerReservedAlbumNamesStrategy mo1272onInit(Object obj) {
            ServerReservedAlbumNamesStrategy serverReservedAlbumNamesStrategy = (ServerReservedAlbumNamesStrategy) CloudControlManager.getInstance().queryFeatureStrategy("server-reserved-album-names");
            return serverReservedAlbumNamesStrategy == null ? ServerReservedAlbumNamesStrategy.createDefault() : serverReservedAlbumNamesStrategy;
        }
    };

    public static ArrayList<String> getAlbumsInWhiteList() {
        AlbumsStrategy albumsStrategy = getAlbumsStrategy();
        if (albumsStrategy != null) {
            return albumsStrategy.getAlbumsInWhiteList();
        }
        return null;
    }

    public static AlbumsStrategy getAlbumsStrategy() {
        return (AlbumsStrategy) CloudControlManager.getInstance().queryFeatureStrategy("album-list");
    }

    public static AlbumTabToolsStrategy getAlbumTabToolsStrategy() {
        return (AlbumTabToolsStrategy) CloudControlManager.getInstance().queryFeatureStrategy("album-tab-tools");
    }

    public static WhiteAlbumsStrategy getWhiteAlbumsStrategy() {
        return (WhiteAlbumsStrategy) CloudControlManager.getInstance().queryFeatureStrategy("white-album-list");
    }

    public static ThirdAlbumsSortStrategy getThirdAlbumSorts() {
        return (ThirdAlbumsSortStrategy) CloudControlManager.getInstance().queryFeatureStrategy("third-album-sorts");
    }

    public static AlbumsStrategy.Album getAlbumByPath(String str) {
        AlbumsStrategy albumsStrategy = getAlbumsStrategy();
        if (albumsStrategy != null) {
            return albumsStrategy.getAlbumByPath(str);
        }
        return null;
    }

    public static String getAlbumName(String str) {
        AlbumsStrategy.Album albumByPath;
        AlbumsStrategy albumsStrategy = getAlbumsStrategy();
        if (albumsStrategy == null || (albumByPath = albumsStrategy.getAlbumByPath(str)) == null) {
            return null;
        }
        return albumByPath.getBestName();
    }

    public static AlbumsStrategy.Attributes getAlbumAttributesByPath(String str) {
        AlbumsStrategy albumsStrategy = getAlbumsStrategy();
        if (albumsStrategy != null) {
            return albumsStrategy.getAlbumAttributesByPath(str);
        }
        return null;
    }

    public static ArrayList<Pattern> getAlbumWhiteListPatterns() {
        AlbumsStrategy albumsStrategy = getAlbumsStrategy();
        if (albumsStrategy != null) {
            return albumsStrategy.getWhiteListPatterns();
        }
        return null;
    }

    public static boolean isInHideList(String str) {
        HiddenAlbumsStrategy hiddenAlbums = getHiddenAlbums();
        return hiddenAlbums != null && hiddenAlbums.isInHideList(str);
    }

    public static ServerUnModifyAlbumsStrategy getServerUnModifyAlbumsStrategy() {
        return sServerUnModifyAlbumsStrategy.get(null);
    }

    public static List<ComponentsStrategy.Component> getShareComponents() {
        ComponentsStrategy componentsStrategy = (ComponentsStrategy) CloudControlManager.getInstance().queryFeatureStrategy("components-list", ComponentsStrategy.CLOUD_FIRST_MERGER);
        if (componentsStrategy == null) {
            componentsStrategy = ComponentsStrategy.createDefault();
        }
        return componentsStrategy.getShareComponents();
    }

    public static List<ComponentsStrategy.Priority> getComponentPriority() {
        ComponentsStrategy componentsStrategy = (ComponentsStrategy) CloudControlManager.getInstance().queryFeatureStrategy("components-list", ComponentsStrategy.CLOUD_FIRST_MERGER);
        if (componentsStrategy == null) {
            componentsStrategy = ComponentsStrategy.createDefault();
        }
        return componentsStrategy.getComponentPriority();
    }

    public static SyncStrategy getSyncStrategy() {
        SyncStrategy syncStrategy = (SyncStrategy) CloudControlManager.getInstance().queryFeatureStrategy("sync");
        return syncStrategy == null ? SyncStrategy.createDefault() : syncStrategy;
    }

    public static HiddenAlbumsStrategy getHiddenAlbums() {
        return (HiddenAlbumsStrategy) CloudControlManager.getInstance().queryFeatureStrategy("hidden-albums");
    }

    public static MigrateStrategy getMigrateStrategy() {
        return (MigrateStrategy) CloudControlManager.getInstance().queryFeatureStrategy("migrate-list");
    }

    public static long getMigrateStrategyVersion() {
        MigrateStrategy migrateStrategy = getMigrateStrategy();
        if (migrateStrategy == null) {
            return 0L;
        }
        return migrateStrategy.getState();
    }

    public static List<MigrateStrategy.Album> getMigrateStrategyAlbums() {
        MigrateStrategy migrateStrategy = getMigrateStrategy();
        if (migrateStrategy == null) {
            return Collections.emptyList();
        }
        return migrateStrategy.getAlbums();
    }

    public static LocationStrategy getLocationStrategy() {
        LocationStrategy locationStrategy = (LocationStrategy) CloudControlManager.getInstance().queryFeatureStrategy("location");
        return locationStrategy == null ? LocationStrategy.createDefault() : locationStrategy;
    }

    public static SearchStrategy getSearchStrategy() {
        SearchStrategy searchStrategy = (SearchStrategy) CloudControlManager.getInstance().queryFeatureStrategy(MiStat.Event.SEARCH);
        return searchStrategy == null ? SearchStrategy.createDefault() : searchStrategy;
    }

    public static PhotoPrintStrategy getPhotoPrintStrategy() {
        return (PhotoPrintStrategy) CloudControlManager.getInstance().queryFeatureStrategy("photo-print");
    }

    public static MacaronStrategy getMacaronStrategy() {
        return (MacaronStrategy) CloudControlManager.getInstance().queryFeatureStrategy("macaron");
    }

    public static SlimEntrancesStrategy getSlimEntrancesStrategy() {
        return (SlimEntrancesStrategy) CloudControlManager.getInstance().queryFeatureStrategy("slim-entrances");
    }

    public static CreationStrategy getCreationStrategy() {
        CreationStrategy creationStrategy = (CreationStrategy) CloudControlManager.getInstance().queryFeatureStrategy("creation");
        return creationStrategy != null ? creationStrategy : CreationStrategy.createDefault();
    }

    public static String getPrintPackageName() {
        PhotoPrintStrategy photoPrintStrategy = getPhotoPrintStrategy();
        if (photoPrintStrategy == null) {
            return null;
        }
        return photoPrintStrategy.getPhotoPrintPackageName();
    }

    public static ServerReservedAlbumNamesStrategy getServerReservedAlbumNamesStrategy() {
        return sServerReservedAlbumNamesStrategy.get(null);
    }

    public static ScannerStrategy.FileSizeLimitStrategy getFileSizeLimitStrategy() {
        ScannerStrategy scannerStrategy = (ScannerStrategy) CloudControlManager.getInstance().queryFeatureStrategy("media_scanner", ScannerStrategy.CLOUD_FIRST_MERGER);
        if (scannerStrategy == null) {
            scannerStrategy = ScannerStrategy.createDefault();
        }
        return scannerStrategy.getFileSizeStrategy();
    }

    public static ScannerStrategy.SpecialTypeMediaStrategy getSpecialTypeMediaStrategy() {
        ScannerStrategy scannerStrategy = (ScannerStrategy) CloudControlManager.getInstance().queryFeatureStrategy("media_scanner", ScannerStrategy.CLOUD_FIRST_MERGER);
        if (scannerStrategy == null) {
            scannerStrategy = ScannerStrategy.createDefault();
        }
        return scannerStrategy.getSpecialTypeMediaStrategy();
    }

    public static AssistantScenarioStrategy getAssistantScenarioStrategy() {
        return (AssistantScenarioStrategy) CloudControlManager.getInstance().queryFeatureStrategy("scenario-rules");
    }

    public static SceneTagStructureStrategy getSceneTagsStructureStrategy() {
        return (SceneTagStructureStrategy) CloudControlManager.getInstance().queryFeatureStrategy("scene_tags_structure");
    }

    public static RecommendStrategy getRecommendStrategy() {
        return (RecommendStrategy) CloudControlManager.getInstance().queryFeatureStrategy("recommendation");
    }

    public static DataLoadStrategy getDataLoadStrategy() {
        DataLoadStrategy dataLoadStrategy = (DataLoadStrategy) CloudControlManager.getInstance().queryFeatureStrategy("load_strategy");
        return dataLoadStrategy != null ? dataLoadStrategy : DataLoadStrategy.createDefault();
    }

    public static OnlineVideoStrategy getOnlineVideoStrategy() {
        OnlineVideoStrategy onlineVideoStrategy = (OnlineVideoStrategy) CloudControlManager.getInstance().queryFeatureStrategy("online-video");
        return onlineVideoStrategy != null ? onlineVideoStrategy : OnlineVideoStrategy.createDefault();
    }

    public static boolean getMediaFeatureCalculationDisable() {
        MediaFeatureCalculationStrategy.DeviceStrategy strategy;
        String str = LibraryStrategyUtils.isMtk() ? "mtk" : "snpe";
        if (Build.VERSION.SDK_INT >= 31) {
            str = str + "_s";
        }
        MediaFeatureCalculationStrategy mediaFeatureCalculationStrategy = (MediaFeatureCalculationStrategy) CloudControlManager.getInstance().queryFeatureStrategy("media-feature-calculation");
        return (mediaFeatureCalculationStrategy == null || (strategy = mediaFeatureCalculationStrategy.getStrategy(str)) == null || !strategy.isDisable()) ? false : true;
    }
}
