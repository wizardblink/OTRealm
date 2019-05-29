package com.wizardblink.smartgmo.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class OTs extends RealmObject {

    //Definimos los campos de nuestra tabla OTs de la base de datos Realm.
    @PrimaryKey
    private long id;
    @Required
    private String priority;
    @Required
    private String type;
    private String state;
    @Required
    private String description;
    @Required
    private String usuario;
    @Required
    private Date createdAt;
    private Date realInit;
    private Date finishedAt;
    @Required
    private String equipo;
    @Required
    private String generatedBy;

    //Definimos un constructor vacío por defecto porque lo neceista Realm.
    public OTs() {

    }

    //Definimos nuestros constructores.
    /*public OTs(String state , Date realInit, Date finishedAt){
        this.state = state;
        this.realInit = realInit;
        this.finishedAt = finishedAt;
    }*/

    public OTs(long id, String priority, String type, String description, String equipo, String generatedBy){
        this.id = id;
        this.priority = priority;
        this.state = "Sin asignar";
        this.type = type;
        this.description = description;
        this.usuario = "";
        this.createdAt = new Date();
        this.equipo = equipo;
        this.generatedBy = generatedBy;

    }

    //Generamos los métodos getters y setters

    public long getId() {
        return id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getRealInit() {
        return realInit;
    }

    public void setRealInit(Date realInit) {
        this.realInit = realInit;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getEquipo() {return equipo;}

    public void setEquipo(String equipo) {this.equipo = equipo;}

    public String getGeneratedBy() {return generatedBy;}

    public void setGeneratedBy(String generatedBy) {this.generatedBy = generatedBy;}
}
