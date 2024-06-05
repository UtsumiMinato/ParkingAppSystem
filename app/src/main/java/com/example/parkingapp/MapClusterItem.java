package com.example.parkingapp;

import com.google.maps.android.clustering.ClusterItem;
import com.google.android.gms.maps.model.LatLng;

public class MapClusterItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final String address;
    private final int available;
    private final int total;
    private final String managerName;
    private final String managerPhone;
    private final String price;

    public MapClusterItem(LatLng position, String title, String snippet, String address, String price, int available,  int total, String managerName, String managerPhone) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.address = address;
        this.price = price;
        this.available = available;
        this.total = total;
        this.managerName = managerName;
        this.managerPhone = managerPhone;
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

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public int getAvailable() {
        return available;
    }

    public int getTotal() {
        return total;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getManagerPhone() {
        return managerPhone;
    }
}

