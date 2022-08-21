package com.miui.gallery.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import com.google.common.collect.Iterables;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.map.cluster.Cluster;
import com.miui.gallery.map.cluster.ClusterManager;
import com.miui.gallery.map.cluster.MapLatLng;
import com.miui.gallery.map.data.MapItem;
import com.miui.gallery.map.location.BDLocationClientImpl;
import com.miui.gallery.map.location.GalleryLocationClient;
import com.miui.gallery.map.location.ILocationClient;
import com.miui.gallery.map.location.ILocationListener;
import com.miui.gallery.map.utils.IMapStatus;
import com.miui.gallery.map.utils.MapConfig;
import com.miui.gallery.map.utils.MapStatHelper;
import com.miui.gallery.map.utils.OnMapLoadedCallback;
import com.miui.gallery.map.view.IMapContainer;
import com.miui.gallery.map.view.MapContainer;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.permission.core.PermissionInjector;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.provider.cache.IMedia;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.ui.MapFloatingMenuLayout;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MapFragment extends Fragment implements OnMapLoadedCallback {
    public List<IMedia> allMediasShowOnMap;
    public ClusterManager<MapItem> mClusterManager;
    public double[] mInitialLocation;
    public ILocationClient mLocationClient;
    public IMapContainer mMapContainer;
    public MapFloatingMenuLayout mMapFloatingMenu;
    public String mMediaTitle;
    public boolean mShowNearbyPhotos;
    public MapViewModel mViewModel;

    /* renamed from: $r8$lambda$-I5POvgdO5RhkIJr2OsfyhZoPXQ */
    public static /* synthetic */ void m1509$r8$lambda$I5POvgdO5RhkIJr2OsfyhZoPXQ(MapFragment mapFragment, IMapStatus iMapStatus) {
        mapFragment.lambda$initManager$8(iMapStatus);
    }

    /* renamed from: $r8$lambda$57Jr-AmwTeqcBTxLQ3Vk2AfVFpw */
    public static /* synthetic */ void m1510$r8$lambda$57JrAmwTeqcBTxLQ3Vk2AfVFpw(MapFragment mapFragment, DialogInterface dialogInterface, int i) {
        mapFragment.lambda$showOpenLocationPermissionDialog$4(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$KldtY99t_fdwQoNslVxN4dQaSL0(MapFragment mapFragment, List list) {
        mapFragment.lambda$onCreate$1(list);
    }

    /* renamed from: $r8$lambda$TjJYJZ-CM9penph8yjSWUjWAkHw */
    public static /* synthetic */ void m1511$r8$lambda$TjJYJZCM9penph8yjSWUjWAkHw(MapFragment mapFragment, DialogInterface dialogInterface, int i) {
        mapFragment.lambda$checkLocationServiceAvailable$2(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$fewKqSdAfhUuUbU35RTrSokO7mM(MapFragment mapFragment, Boolean bool) {
        mapFragment.lambda$onCreate$0(bool);
    }

    public static /* synthetic */ boolean $r8$lambda$l8PZbwiZ_wh1pC53cNoEcr1SbyQ(MapFragment mapFragment, Cluster cluster) {
        return mapFragment.lambda$initManager$7(cluster);
    }

    public static /* synthetic */ boolean lambda$initManager$6(MapItem mapItem) {
        return false;
    }

    public static MapFragment getInstance(String str, boolean z, double[] dArr) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("extra_show_nearby_photos", z);
        bundle.putDoubleArray("extra_initial_location", dArr);
        bundle.putString("extra_initial_media_title", str);
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mShowNearbyPhotos = getArguments().getBoolean("extra_show_nearby_photos", true);
            this.mInitialLocation = getArguments().getDoubleArray("extra_initial_location");
            this.mMediaTitle = getArguments().getString("extra_initial_media_title");
        }
        MapViewModel mapViewModel = (MapViewModel) new ViewModelProvider(requireActivity()).get(MapViewModel.class);
        this.mViewModel = mapViewModel;
        Transformations.distinctUntilChanged(mapViewModel.getShowAllPhotoLiveData()).observe(this, new Observer() { // from class: com.miui.gallery.ui.MapFragment$$ExternalSyntheticLambda4
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                MapFragment.$r8$lambda$fewKqSdAfhUuUbU35RTrSokO7mM(MapFragment.this, (Boolean) obj);
            }
        });
        this.mViewModel.getItemsShowOnMap().observe(this, new Observer() { // from class: com.miui.gallery.ui.MapFragment$$ExternalSyntheticLambda5
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                MapFragment.$r8$lambda$KldtY99t_fdwQoNslVxN4dQaSL0(MapFragment.this, (List) obj);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0(Boolean bool) {
        DefaultLogger.d("MapFragment", "status of display nearby photos change->%s", bool);
        updateDataSet();
    }

    public /* synthetic */ void lambda$onCreate$1(List list) {
        IMapContainer iMapContainer;
        DefaultLogger.d("MapFragment", "mapViewModel query finish: count->%s", Integer.valueOf(list == null ? 0 : list.size()));
        if (list == null || (iMapContainer = this.mMapContainer) == null || !iMapContainer.hasMapLoaded()) {
            return;
        }
        if (this.allMediasShowOnMap != null && list.size() == this.allMediasShowOnMap.size()) {
            return;
        }
        this.allMediasShowOnMap = list;
        markerGetAndSet();
    }

    public final void updateDataSet() {
        MapViewModel mapViewModel = this.mViewModel;
        if (mapViewModel != null) {
            mapViewModel.queryItemsShowOnMap();
        }
    }

    public final List<MapItem> buildMapData(List<IMedia> list) {
        ArrayList arrayList = new ArrayList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                IMedia iMedia = list.get(i);
                if (iMedia != null && ((!TextUtils.isEmpty(iMedia.getThumbnail()) || !TextUtils.isEmpty(iMedia.getFilePath())) && iMedia.hasValidLocationInfo())) {
                    MapItem mapItem = new MapItem();
                    mapItem.mapping(iMedia);
                    arrayList.add(mapItem);
                }
            }
        }
        DefaultLogger.d("MapFragment", "build map data finish: count->%s", Integer.valueOf(arrayList.size()));
        return arrayList;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.map_fragment, viewGroup, false);
        FrameLayout frameLayout = (FrameLayout) inflate.findViewById(R.id.map_fragment_root);
        try {
            this.mMapContainer = MapContainer.newInstance(requireContext());
            this.mMapFloatingMenu = (MapFloatingMenuLayout) inflate.findViewById(R.id.map_floating_menu);
            frameLayout.addView(this.mMapContainer.getView(), 0, new ViewGroup.LayoutParams(-1, -1));
            return inflate;
        } catch (UnsatisfiedLinkError e) {
            ToastUtils.makeText(requireContext(), getString(R.string.map_common_download_failed_msg));
            DefaultLogger.d("MapFragment", "finish map activity dur to java.lang.UnsatisfiedLinkError");
            DefaultLogger.e("MapFragment", e);
            requireActivity().finish();
            return null;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mMapContainer != null) {
            initManager();
            this.mMapContainer.setMapStatusChangeListener(this.mClusterManager);
            this.mMapContainer.setClusterClickListener(this.mClusterManager);
            this.mMapContainer.setMapLoadedCallback(this);
        }
        MapFloatingMenuLayout mapFloatingMenuLayout = this.mMapFloatingMenu;
        if (mapFloatingMenuLayout != null) {
            mapFloatingMenuLayout.setOnItemClickListener(new MapFloatingMenuLayout.OnMenuItemClickListener() { // from class: com.miui.gallery.ui.MapFragment.1
                {
                    MapFragment.this = this;
                }

                @Override // com.miui.gallery.ui.MapFloatingMenuLayout.OnMenuItemClickListener
                public void location() {
                    MapFragment.this.checkPermission();
                }

                @Override // com.miui.gallery.ui.MapFloatingMenuLayout.OnMenuItemClickListener
                public void showNearby() {
                    MapFragment.this.mViewModel.getShowAllPhotoLiveData().setValue(Boolean.TRUE);
                    MapStatHelper.trackShowOrHideNearby(true);
                }

                @Override // com.miui.gallery.ui.MapFloatingMenuLayout.OnMenuItemClickListener
                public void hideNearby() {
                    MapFragment.this.mViewModel.getShowAllPhotoLiveData().setValue(Boolean.FALSE);
                    MapStatHelper.trackShowOrHideNearby(false);
                }
            }, !this.mShowNearbyPhotos);
        }
    }

    public final void startLocation() {
        if (this.mLocationClient == null) {
            BDLocationClientImpl bDLocationClientImpl = new BDLocationClientImpl();
            this.mLocationClient = bDLocationClientImpl;
            bDLocationClientImpl.init(GalleryApp.sGetAndroidContext());
            this.mLocationClient.registerLocationListener(new ILocationListener() { // from class: com.miui.gallery.ui.MapFragment.2
                {
                    MapFragment.this = this;
                }

                @Override // com.miui.gallery.map.location.ILocationListener
                public void onReceiveLocationSuccess(MapLatLng mapLatLng) {
                    if (MapFragment.this.mMapContainer != null && MapFragment.this.mMapContainer.hasMapLoaded()) {
                        MapFragment.this.mMapContainer.showLocationIcon(mapLatLng);
                        MapStatHelper.trackPosition(true, Integer.MIN_VALUE);
                    }
                    MapFragment.this.mLocationClient.stop();
                }

                @Override // com.miui.gallery.map.location.ILocationListener
                public void onReceiveLocationFailed(int i) {
                    DefaultLogger.d("MapFragment", "bd location receiveLocationFailed: errorCode->%s", Integer.valueOf(i));
                    MapStatHelper.trackPosition(false, i);
                    MapFragment.this.mLocationClient.stop();
                    MapFragment.this.startLocationByGallery();
                }
            });
        }
        this.mLocationClient.start();
    }

    public final void startLocationByGallery() {
        MapLatLng lastKnownLocation = GalleryLocationClient.getLastKnownLocation(requireContext());
        if (lastKnownLocation != null) {
            this.mMapContainer.showLocationIcon(lastKnownLocation);
            return;
        }
        DefaultLogger.d("MapFragment", "LocationManage getLastKnownLocation failed");
        ToastUtils.makeText(getContext(), (int) R.string.map_positioning_failed_tip);
    }

    public final boolean checkPermission() {
        if (PermissionUtils.checkLocationPermissions(requireActivity())) {
            checkLocationServiceAvailable();
            return true;
        }
        PermissionInjector.injectIfNeededIn(getActivity(), new PermissionCheckCallback() { // from class: com.miui.gallery.ui.MapFragment.3
            {
                MapFragment.this = this;
            }

            @Override // com.miui.gallery.permission.core.PermissionCheckCallback
            public Permission[] getRuntimePermissions() {
                return new Permission[]{new Permission("android.permission.ACCESS_FINE_LOCATION", MapFragment.this.getString(R.string.map_grant_position_permission_subtitle), false), new Permission("android.permission.ACCESS_COARSE_LOCATION", MapFragment.this.getString(R.string.map_grant_position_permission_subtitle), false)};
            }

            @Override // com.miui.gallery.permission.core.PermissionCheckCallback
            public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
                boolean z = false;
                if (permissionArr != null && permissionArr.length > 0) {
                    boolean z2 = false;
                    for (int i = 0; i < permissionArr.length; i++) {
                        z2 = iArr[i] == 0;
                    }
                    z = z2;
                }
                if (z) {
                    MapFragment.this.checkLocationServiceAvailable();
                } else {
                    MapFragment.this.showOpenLocationPermissionDialog();
                }
            }
        }, null);
        return true;
    }

    public final void checkLocationServiceAvailable() {
        if (GalleryLocationClient.isLocationServiceAvailable(getContext())) {
            startLocation();
        } else {
            new AlertDialogFragment.Builder().setTitle(getString(R.string.map_enable_position_service_title)).setMessage(getString(R.string.map_enable_position_service_message)).setPositiveButton(getString(R.string.grant_permission_go_and_set_2), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.MapFragment$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    MapFragment.m1511$r8$lambda$TjJYJZCM9penph8yjSWUjWAkHw(MapFragment.this, dialogInterface, i);
                }
            }).setNegativeButton(getString(R.string.cancel), MapFragment$$ExternalSyntheticLambda2.INSTANCE).create().showAllowingStateLoss(getActivity().getSupportFragmentManager(), "LocationServiceSettingDialog");
        }
    }

    public /* synthetic */ void lambda$checkLocationServiceAvailable$2(DialogInterface dialogInterface, int i) {
        IntentUtil.goToLocationServivePage(getContext());
    }

    public final void showOpenLocationPermissionDialog() {
        new AlertDialogFragment.Builder().setTitle(getString(R.string.grant_permission_title)).setMessage(getString(R.string.map_grant_position_permission_after_declined_twice_message)).setPositiveButton(getString(R.string.grant_permission_go_and_set_2), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.MapFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MapFragment.m1510$r8$lambda$57JrAmwTeqcBTxLQ3Vk2AfVFpw(MapFragment.this, dialogInterface, i);
            }
        }).setNegativeButton(getString(R.string.cancel), MapFragment$$ExternalSyntheticLambda3.INSTANCE).create().showAllowingStateLoss(getActivity().getSupportFragmentManager(), "OpenLocationPermissionDialog");
    }

    public /* synthetic */ void lambda$showOpenLocationPermissionDialog$4(DialogInterface dialogInterface, int i) {
        IntentUtil.goToAppPermissionEditor(getContext());
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        IMapContainer iMapContainer = this.mMapContainer;
        if (iMapContainer != null) {
            iMapContainer.onResume();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        IMapContainer iMapContainer = this.mMapContainer;
        if (iMapContainer != null) {
            iMapContainer.onPause();
        }
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        IMapContainer iMapContainer = this.mMapContainer;
        if (iMapContainer != null) {
            iMapContainer.onDestroy();
        }
        release();
        super.onDestroy();
    }

    @Override // com.miui.gallery.map.utils.OnMapLoadedCallback
    public void onMapLoaded() {
        DefaultLogger.d("MapFragment", "onMapLoaded, zoom->%s", Float.valueOf(this.mMapContainer.getZoomLevel()));
        if (this.mInitialLocation != null && !TextUtils.isEmpty(this.mMediaTitle)) {
            IMapContainer iMapContainer = this.mMapContainer;
            double[] dArr = this.mInitialLocation;
            iMapContainer.moveTo(dArr[0], dArr[1], MapConfig.FOCUS_ZOOM_LEVEL.floatValue());
        }
        this.mViewModel.initMapStatus(this.mShowNearbyPhotos, this.mMediaTitle);
    }

    public void initManager() {
        ClusterManager<MapItem> clusterManager = new ClusterManager<>(GalleryApp.sGetAndroidContext(), this.mMapContainer);
        this.mClusterManager = clusterManager;
        clusterManager.setOnClusterItemClickListener(MapFragment$$ExternalSyntheticLambda7.INSTANCE);
        this.mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener() { // from class: com.miui.gallery.ui.MapFragment$$ExternalSyntheticLambda6
            @Override // com.miui.gallery.map.cluster.ClusterManager.OnClusterClickListener
            public final boolean onClusterClick(Cluster cluster) {
                return MapFragment.$r8$lambda$l8PZbwiZ_wh1pC53cNoEcr1SbyQ(MapFragment.this, cluster);
            }
        });
        this.mClusterManager.setOnMapStatusChangeFinishListener(new ClusterManager.OnMapStatusChangeFinish() { // from class: com.miui.gallery.ui.MapFragment$$ExternalSyntheticLambda8
            @Override // com.miui.gallery.map.cluster.ClusterManager.OnMapStatusChangeFinish
            public final void onMapStatusChangeFinish(IMapStatus iMapStatus) {
                MapFragment.m1509$r8$lambda$I5POvgdO5RhkIJr2OsfyhZoPXQ(MapFragment.this, iMapStatus);
            }
        });
    }

    public /* synthetic */ boolean lambda$initManager$7(Cluster cluster) {
        DefaultLogger.d("MapFragment", "cluster click, size->%s", String.valueOf(cluster.getSize()));
        if (cluster.getSize() == 1) {
            this.mViewModel.goToPhotoPage(this, (MapItem) Iterables.get(cluster.mo1081getItems(), 0));
        } else {
            this.mViewModel.goToPhotoListPage(this, cluster.mo1081getItems());
        }
        MapStatHelper.trackClickMarker(cluster.getSize());
        return false;
    }

    public /* synthetic */ void lambda$initManager$8(IMapStatus iMapStatus) {
        DefaultLogger.d("MapFragment", "onMapStatusChangeFinish: zoom->%s", Float.valueOf(iMapStatus.getZoomLevel()));
        if (this.mViewModel.needShowAllPhotosStatus()) {
            this.mViewModel.getItemsShowOnMap().invalidate();
        }
    }

    public final void markerGetAndSet() {
        List<IMedia> list;
        if (!this.mMapContainer.hasMapLoaded() || (list = this.allMediasShowOnMap) == null) {
            return;
        }
        DefaultLogger.d("MapFragment", "all medias in map->%s", Integer.valueOf(list.size()));
        this.mClusterManager.clearItems();
        if (this.allMediasShowOnMap.size() != 0) {
            AutoTracking.trackView("403.61.0.1.15327", AutoTracking.getRef(), this.allMediasShowOnMap.size());
            this.mClusterManager.addItems(buildMapData(this.allMediasShowOnMap));
        }
        this.mClusterManager.cluster(this.mMapContainer.getZoomLevel(), this.mMapContainer.getBound());
    }

    public void onBackPressed() {
        release();
    }

    public final void release() {
        IMapContainer iMapContainer = this.mMapContainer;
        if (iMapContainer != null) {
            iMapContainer.setMapStatusChangeListener(null);
            this.mMapContainer.setMapLoadedCallback(null);
        }
        ILocationClient iLocationClient = this.mLocationClient;
        if (iLocationClient != null) {
            iLocationClient.stop();
            this.mLocationClient.unregisterLocationListener();
        }
        ClusterManager<MapItem> clusterManager = this.mClusterManager;
        if (clusterManager != null) {
            clusterManager.release();
        }
    }
}
