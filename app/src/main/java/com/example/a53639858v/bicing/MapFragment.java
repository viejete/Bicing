package com.example.a53639858v.bicing;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;


public class MapFragment extends Fragment implements AsyncResponse {

    View view;
    MapView map;
    GeoPoint startPoint;
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

        download.delegate = this;
        download.execute(BASE_URL);

        return view;
    }


    @Override
    public void processFinish(String output) {
        stations = ProcessTask.processJson(output);
        fillMap();
    }


    private void fillMap() {

        float percent;

        mapIntro();

        // Creamos el cluster
        RadiusMarkerClusterer poiMarkers = new RadiusMarkerClusterer(getContext());
        poiMarkers.setRadius(110);

        // Le asignamos un icono
        Drawable clusterIconD = ContextCompat.getDrawable(getContext(), R.drawable.ic_cluster);
        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
        poiMarkers.setIcon(clusterIcon);

        // Custom design of clusters
        poiMarkers.getTextPaint().setColor(Color.CYAN);
        poiMarkers.getTextPaint().setTextSize(12 * getResources().getDisplayMetrics().density);
        poiMarkers.mAnchorU = Marker.ANCHOR_RIGHT;
        poiMarkers.mAnchorV = Marker.ANCHOR_BOTTOM;
        poiMarkers.mTextAnchorV = 0.40f;

        for (Station station : stations) {
            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(Double.parseDouble(station.getLatitude()), Double.parseDouble(station.getLongitude())));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(station.getStreetName() + "\n" + station.getStreetNumber() + "\nCapacidad: " + station.getTotalBikes() + "\nDisponibles: " + station.getBikeAvailables());

            if (station.getTotalBikes() != 0) {
                percent = station.getBikeAvailables() * 100 / station.getTotalBikes();
            } else {
                percent = 0;
            }

            if (station.getType().equalsIgnoreCase("BIKE")) {
                if (percent == 0) {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_100));
                } else if (percent < 37.5) {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_75));
                } else if (percent < 62.5) {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_50));
                } else if (percent < 87.5) {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_25));
                } else {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_0));
                }
            } else if (station.getType().equalsIgnoreCase("BIKE-ELECTRIC")) {
                if (percent == 0) {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_100e));
                } else if (percent < 37.5) {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_75e));
                } else if (percent < 62.5) {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_50e));
                } else if (percent < 87.5) {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_25e));
                } else {
                    marker.setIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_0e));
                }
            }

            poiMarkers.add(marker);
        }

        map.getOverlays().add(poiMarkers);

        map.invalidate();
    }


    private void mapIntro() {

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(17);
        startPoint = new GeoPoint(Double.parseDouble(stations.get(1).getLatitude()), Double.parseDouble(stations.get(1).getLongitude()));
        mapController.setCenter(startPoint);
    }
}
