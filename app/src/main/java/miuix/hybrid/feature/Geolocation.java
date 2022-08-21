package miuix.hybrid.feature;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import java.util.Map;
import miuix.hybrid.Callback;
import miuix.hybrid.HybridFeature;
import miuix.hybrid.Request;
import miuix.hybrid.Response;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class Geolocation implements HybridFeature {
    private static final String ACTION_DISABLE = "disableListener";
    private static final String ACTION_ENABLE = "enableListener";
    private static final String ACTION_GET = "get";
    private static final String PARAM_TYPE = "type";
    private static final String TAG = "HybridGeolocation";
    private Callback mCallback;
    private LocationListener mListener;
    private String mProvider = "network";

    @Override // miuix.hybrid.HybridFeature
    public void setParams(Map<String, String> map) {
        if ("gps".equals(map.get("type"))) {
            this.mProvider = "gps";
        }
    }

    @Override // miuix.hybrid.HybridFeature
    public Response invoke(Request request) {
        String action = request.getAction();
        Log.i(TAG, "invoke with action: " + action);
        LocationManager locationManager = (LocationManager) request.getNativeInterface().getActivity().getSystemService("location");
        if (ACTION_ENABLE.equals(action)) {
            return invokeUpdateLocation(locationManager, request);
        }
        if ("get".equals(action)) {
            return invokeGetLocation(locationManager, request);
        }
        if (ACTION_DISABLE.equals(action)) {
            return invokeRemove(locationManager, request);
        }
        return new Response(204, "no such action");
    }

    private Response invokeUpdateLocation(LocationManager locationManager, Request request) {
        this.mCallback = request.getCallback();
        if (this.mListener == null) {
            this.mListener = new NetworkLocationListener();
            Looper.prepare();
            locationManager.requestLocationUpdates(this.mProvider, 0L, 0.0f, this.mListener);
            Looper.loop();
        }
        this.mCallback.callback(response(locationManager.getLastKnownLocation(this.mProvider)));
        return null;
    }

    private Response invokeGetLocation(LocationManager locationManager, Request request) {
        return response(locationManager.getLastKnownLocation(this.mProvider));
    }

    private Response invokeRemove(LocationManager locationManager, Request request) {
        LocationListener locationListener = this.mListener;
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
            this.mListener = null;
        }
        this.mCallback = null;
        request.getCallback().callback(new Response(0, "remove"));
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Response response(Location location) {
        if (location != null) {
            Log.i(TAG, "response with valid location.");
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("latitude", location.getLatitude());
                jSONObject.put("longitude", location.getLongitude());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new Response(3, jSONObject.toString());
        }
        Log.i(TAG, "error: response location with null.");
        return new Response(200, "no location");
    }

    /* loaded from: classes3.dex */
    public class NetworkLocationListener implements LocationListener {
        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        private NetworkLocationListener() {
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (Geolocation.this.mCallback != null) {
                Geolocation.this.mCallback.callback(Geolocation.this.response(location));
            }
        }
    }

    @Override // miuix.hybrid.HybridFeature
    public HybridFeature.Mode getInvocationMode(Request request) {
        if ("get".equals(request.getAction())) {
            return HybridFeature.Mode.SYNC;
        }
        return HybridFeature.Mode.CALLBACK;
    }
}
