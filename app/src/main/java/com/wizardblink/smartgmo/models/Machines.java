package com.wizardblink.smartgmo.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Machines extends RealmObject {

    private int id;
    private String machines;
    private RealmList<OTs> ots;

    public Machines(){

    }

    public Machines(int id, String machines){
        this.id = id;
        this.machines = machines;
        this.ots = new RealmList<OTs>();
    }

    public int getId() {
        return id;
    }

    public String getMachines() {
        return machines;
    }

    public void setMachines(String machines) {
        this.machines = machines;
    }

    public RealmList<OTs> getOts() {
        return ots;
    }
}
