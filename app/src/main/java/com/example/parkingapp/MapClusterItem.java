package com.example.parkingapp;

import com.google.maps.android.clustering.ClusterItem;
import com.google.android.gms.maps.model.LatLng;

public class MapClusterItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;

    public MapClusterItem(LatLng position, String title, String snippet) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }
}
