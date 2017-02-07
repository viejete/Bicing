package com.example.a53639858v.bicing;

import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashMap;


public class MapFragment extends Fragment implements AsyncResponse {

    View view;
    MapView map;
    GeoPoint startPoint;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ArrayList<Station> stations;
    public static DownloadTask download = new DownloadTask();
    private static final String BASE_URL = "http://wservice.viabicing.cat/v2/stations";


    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        map = (MapView) view.findViewById(R.id.map);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext() , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        download.delegate = this;
        download.execute(BASE_URL);


        //startPoint = new GeoPoint(48.13 , -1.63);

        //fillMap();

        //mapMarker();

        map.invalidate();


        return view;
    }

    private void fillMap() {

        mapIntro();

        for (Station station : stations) {
            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(Double.parseDouble(station.getLatitude()) , Double.parseDouble(station.getLongitude())));
            marker.setAnchor(Marker.ANCHOR_CENTER , Marker.ANCHOR_BOTTOM);
            marker.setTitle(station.getStreetName() + "\n" + station.getStreetNumber());
            map.getOverlays().add(marker);

        }
    }

    private void mapIntro() {

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(17);
        startPoint = new GeoPoint(Double.parseDouble(stations.get(1).getLatitude()) , Double.parseDouble(stations.get(1).getLongitude()));
        mapController.setCenter(startPoint);

    }

    private void mapMarker() {

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        //startMarker.setIcon(getResources().    }
        startMarker.setTitle("Start point");
    }

    @Override
    public void processFinish(String output) {
        stations = ProcessTask.processJson(output);
        fillMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {

                HashMap<String,Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                Log.i("permissions" , "hasta aqui bien?");
                // If request is cancelled, the result arrays are empty.
                if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Snackbar.make(view , "holita" , Snackbar.LENGTH_LONG).show();

                    Log.i("permissions" , "Que pollarda....");



                } else {


                    Snackbar.make(view , "holitamalo" , Snackbar.LENGTH_LONG).show();



                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
