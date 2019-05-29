package com.wizardblink.smartgmo.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ProductiveArea extends RealmObject {
    private int id;
    private String productiveArea;
    private RealmList<Machines> machines;

    public ProductiveArea(){

    }

    public ProductiveArea(int id, String productiveArea){
        this.id = id;
        this.productiveArea = productiveArea;
        this.machines = new RealmList<Machines>();
    }

    public int getId() {
        return id;
    }

    public String getProductiveArea() {
        return productiveArea;
    }

    public void setProductiveArea(String productiveArea) {
        this.productiveArea = productiveArea;
    }

    public RealmList<Machines> getMachines() {
        return machines;
    }
}
