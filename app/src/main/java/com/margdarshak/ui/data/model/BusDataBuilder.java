package com.margdarshak.ui.data.model;
import com.margdarshak.ui.data.model.BusData.Operators;
public class BusDataBuilder {

    private String stopid;
    private String displaystopid;
    private String shortname;
    private String shortnamelocalized;
    private String fullname;
    private String fullnamelocalized;
    private double latitude;
    private double longitude;
    private long lastupdated;
    private Operators operators;


    public BusDataBuilder setStopid(String stopid) {
        this.stopid = stopid;
        return this;
    }

    public BusDataBuilder setDisplaystopid(String displaystopid) {
        this.displaystopid = displaystopid;
        return this;
    }

    public BusDataBuilder setShortname(String shortname) {
        this.shortname = shortname;
        return this;
    }

    public BusDataBuilder Shortnamelocalized(String shortnamelocalized) {
        this.shortnamelocalized = shortnamelocalized;
        return this;
    }
    public BusDataBuilder Fullname(String fullname) {
        this.fullname = fullname;
        return this;
    }
    public BusDataBuilder Fullnamelocalized(String fullnamelocalized) {
        this.fullnamelocalized = fullnamelocalized;
        return this;
    }

    public BusDataBuilder latitude(double latitude) {
        this.latitude = latitude;
        return this;
    }
    public BusDataBuilder longitude(double longitude) {
        this.longitude = longitude;
        return this;
    }


    public BusDataBuilder setLastupdated(long lastupdated) {
        this.lastupdated = lastupdated;
        return this;
    }
    public BusDataBuilder setOperators(String name ,int operatortype) {
        this.operators = new BusData().new Operators(name,operatortype);
        return this;
    }


    public BusData createBusData() {
        return new BusData(stopid,displaystopid,shortname, shortnamelocalized,fullname,fullnamelocalized,latitude,longitude,lastupdated,operators);
    }
}
