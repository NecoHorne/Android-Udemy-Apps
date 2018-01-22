package com.necohorne.earthquakewatcher.Model;

/**
 * Created by necoh on 2018/01/18.
 */

public class EarthQuake {

    //Earthquake details from https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php
    private double mag;
    private String place;
    private long time;
    private long updated  ;
    private int tz;
    private String url;
    private String detail;
    private int felt;
    private double cdi;
    private double mmi;
    private String alert;
    private String status;
    private int tsunami;
    private int sig;
    private String net;
    private String code;
    private String ids;
    private String sources;
    private String types;
    private int nst;
    private double dmin;
    private double rms;
    private double gap;
    private String magType;
    private String type;

    //Location Details also from USGS site.
    private double longitude;
    private double latitude;

    //Constructors

    public EarthQuake() {
    }

    public EarthQuake(double mag, String place, long time, long updated, int tz, String url, String detail, int felt, double cdi, double mmi, String alert, String status, int tsunami, int sig, String net, String code, String ids, String sources, String types, int nst, double dmin, double rms, double gap, String magType, String type, double longitude, double latitude) {
        this.mag = mag;
        this.place = place;
        this.time = time;
        this.updated = updated;
        this.tz = tz;
        this.url = url;
        this.detail = detail;
        this.felt = felt;
        this.cdi = cdi;
        this.mmi = mmi;
        this.alert = alert;
        this.status = status;
        this.tsunami = tsunami;
        this.sig = sig;
        this.net = net;
        this.code = code;
        this.ids = ids;
        this.sources = sources;
        this.types = types;
        this.nst = nst;
        this.dmin = dmin;
        this.rms = rms;
        this.gap = gap;
        this.magType = magType;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public EarthQuake(double mag, String place, long time, String detail, String type, double longitude, double latitude) {
        this.mag = mag;
        this.place = place;
        this.time = time;
        this.detail = detail;
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    //Getters

    public double getMag() {
        return mag;
    }

    public String getPlace() {
        return place;
    }

    public long getTime() {
        return time;
    }

    public long getUpdated() {
        return updated;
    }

    public int getTz() {
        return tz;
    }

    public String getUrl() {
        return url;
    }

    public String getDetail() {
        return detail;
    }

    public int getFelt() {
        return felt;
    }

    public double getCdi() {
        return cdi;
    }

    public double getMmi() {
        return mmi;
    }

    public String getAlert() {
        return alert;
    }

    public String getStatus() {
        return status;
    }

    public int getTsunami() {
        return tsunami;
    }

    public int getSig() {
        return sig;
    }

    public String getNet() {
        return net;
    }

    public String getCode() {
        return code;
    }

    public String getIds() {
        return ids;
    }

    public String getSources() {
        return sources;
    }

    public String getTypes() {
        return types;
    }

    public int getNst() {
        return nst;
    }

    public double getDmin() {
        return dmin;
    }

    public double getRms() {
        return rms;
    }

    public double getGap() {
        return gap;
    }

    public String getMagType() {
        return magType;
    }

    public String getType() {
        return type;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    //Setters

    public void setMag(double mag) {
        this.mag = mag;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public void setTz(int tz) {
        this.tz = tz;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setFelt(int felt) {
        this.felt = felt;
    }

    public void setCdi(double cdi) {
        this.cdi = cdi;
    }

    public void setMmi(double mmi) {
        this.mmi = mmi;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTsunami(int tsunami) {
        this.tsunami = tsunami;
    }

    public void setSig(int sig) {
        this.sig = sig;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public void setNst(int nst) {
        this.nst = nst;
    }

    public void setDmin(double dmin) {
        this.dmin = dmin;
    }

    public void setRms(double rms) {
        this.rms = rms;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }

    public void setMagType(String magType) {
        this.magType = magType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
