package pl.pollub.gpsprecision;

public class MyMarker {
    int _id;
    String _name;
    double _latitude;
    double _longitude;
    float _distance_from;

    //pusty konstruktor
    public MyMarker(String s, double v1, double v, int i) {

    }

    //konstruktor
    public MyMarker(int id, String name, double latitude, double longitude,  float distance_from) {
        this._id = id;
        this._name = name;
        this._latitude =  latitude;
        this._longitude = longitude;
        this._distance_from = distance_from;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public double getLatitude() {
        return _latitude;
    }

    public void setLatitude(double latitude) {
        this._latitude = latitude;
    }

    public double getLongitude() {
        return _longitude;
    }

    public void setLongitude(double longitude) {
        this._longitude = longitude;
    }

    public float getDistanceFrom() {
        return _distance_from;
    }

    public void setDistanceFrom(float distance_from) {
        this._distance_from = distance_from;
    }


}

