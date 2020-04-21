package com.margdarshak.ui.data.model;

public class BusData {

        /**"stopid":"2",
            "displaystopid":"2",
            "shortname":"Parnell Square","
        shortnamelocalized":"Cearnóg Parnell",
        "fullname":"Parnell Square",
            "fullnamelocalized":"",
            "latitude":"53.35224111",
            "longitude":"-6.263695",
            "lastupdated":"20\/04\/2020 09:44:13",
            "operators":[{"name":"bac","operatortype":1,"routes":["38","38A","38B","38D","46A","46E"]}]}
         */

        String stopid;
        String displaystopid;
        String shortname;
        String shortnamelocalized;
        String fullname;
        String fullnamelocalized;
        double latitude;
        double longitude;
        long lastupdated;
        Operators operators;

    public BusData() {

    }

    // operators routes;

       public class Operators {
        String name;
        int operatortype;
        //String routes;

        Operators(String name ,int operatortype) {
            this.name = name;
            this.operatortype = operatortype;

        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int  getoperatortype() {
            return operatortype;
        }

        public void setoperatortype(int operatortype) {
            this.operatortype = operatortype;
        }
    }

    public BusData(String stopid,String displaystopid,String shortname,String shortnamelocalized,String fullname,String fullnamelocalized,double  latitude,double longitude,long lastupdated,Operators operators) {
        this.stopid = stopid;
        this.displaystopid = displaystopid;
        this.shortname = shortname;
        this.shortnamelocalized = shortnamelocalized;
        this.fullname = fullname;
        this.fullnamelocalized = fullnamelocalized;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operators = operators;
        this.lastupdated = lastupdated;

    }

    public String getStopid() {
        return stopid;
    }

    public void setStopid(String stopid) {
        this.stopid = stopid;
    }

    public String getDisplaystopid() {
        return displaystopid;
    }

    public void setDisplaystopid(String displaystopid) {
        this.displaystopid = displaystopid;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getShortnamelocalized() { return shortnamelocalized;
    }

    public void setShortnamelocalized(String shortnamelocalized) {this.shortnamelocalized = shortnamelocalized;
    }
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long Lastupdated() {
        return lastupdated;
    }

    public void setLastupdated(long lastupdated) {
        this.lastupdated = lastupdated;
    }

    public Operators getOperators() {
        return operators;
    }

    public void setOperators(Operators operators) {
        this.operators = operators;
    }



   }
