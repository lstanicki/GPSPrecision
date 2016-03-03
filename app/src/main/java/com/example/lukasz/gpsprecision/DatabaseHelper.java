package com.example.lukasz.gpsprecision;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database.db";
    public static final String TABLE_NAME = "GPS_MARKERS";
    public static final String MARKER_ID = "ID";
    public static final String MARKER_NAME = "NAZWA";
    public static final String MARKER_LONGITUDE = "DLUGOSC";
    public static final String MARKER_LATITUDE = "SZEROKOSC";
    public static final String DISTANCE_FROM  = "DLUGOSC_OD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + MARKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MARKER_NAME + " TEXT, " + MARKER_LATITUDE + " NUMERIC NOT NULL, " + MARKER_LONGITUDE + " NUMERIC NOT NULL, "
                + DISTANCE_FROM + " INTEGER" +
                " )");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Krzemieniecka 1', '51.254458', '22.579823', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'KFC', '51.251711', '22.577230', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Lidl', '51.251402', '22.581424', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Biedronka', '51.255670', '22.580135', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Betacom', '51.270444', '22.565671', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Tarasy Zamkowe', '51.249830', '22.576297', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Zamek Lubelski', '51.250515', '22.572421', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Politechnika Lubelska Rektorat', '51.235650', '22.550631', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Biblioteka Główna UMCS', '51.246484', '22.541105', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Uniwersytet Medyczny', '51.248369', '22.549185', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'McDonalds', '51.247688', '22.558750', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Teatr Stary w Lublinie', '51.247289', '22.569281', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Targ pod Zamkiem', '51.246618', '22.572994', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'WSEI Lublin', '51.245304', '22.610828', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Decathlon', '51.263423', '22.572191', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'WSPiA', '51.270206', '22.569153', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'BP', '51.253763', '22.556198', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Urząd Pocztowy Lublin 3', '51.256474', '22.583501', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Lubelski Rower Miejski 6920', '51.258212', '22.584171', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Niepubliczny Zakład Opieki Zdrowotnej Farmed Stanisław Podgórski', '51.259268', '22.584627', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Kaprys s.c. Bar mleczny', '51.254875', '22.577811', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Brama Grodzka', '51.249603', '22.569845', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Czarcia Łapa', '51.247835', '22.567652', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Urząd Miasta Lublin', '51.247643', '22.565944', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Hotel Europa', '51.248152', '22.561239', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Red Rock City', '51.246140', '22.549955', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Chatka Żaka', '51.245484', '22.538999', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Kino Bajka', '51.246465', '22.544485', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Paco. Klub Sportowy', '51.235040', '22.542761', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Statoil', '51.250546', '22.524138', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Sklep Biegacza Lublin', '51.255288', '22.562971', NULL)");
        db.execSQL("INSERT INTO GPS_MARKERS VALUES (NULL, 'Shell', '51.253525', '22.562052', NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
        onCreate(db);
    }

    void addMarker(MyMarker myMarker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MARKER_NAME, myMarker.getName());
        values.put(MARKER_LONGITUDE, myMarker.getLongitude());
        values.put(MARKER_LATITUDE, myMarker.getLatitude());
        values.put(DISTANCE_FROM, myMarker.getDistanceFrom());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Markers
    public List<MyMarker> getAllMarkers() {
        List<MyMarker> markerList = new ArrayList<MyMarker>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MyMarker myMarker = new MyMarker("Krzemieniecka 1", 51.254458, 22.579823, 1);
                myMarker.setID(Integer.parseInt(cursor.getString(0)));
                myMarker.setName(cursor.getString(1));
                myMarker.setLatitude(cursor.getDouble(2));
                myMarker.setLongitude(cursor.getDouble(3));
                markerList.add(myMarker);
            } while (cursor.moveToNext());
        }

        // return contact list
        return markerList;
    }

    public List<MyMarker> getMarkersForTest() {
        List<MyMarker> markerList = new ArrayList<MyMarker>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY DLUGOSC_OD LIMIT 5";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MyMarker myMarker = new MyMarker("Krzemieniecka 1", 51.254458, 22.579823, 1);
                myMarker.setID(Integer.parseInt(cursor.getString(0)));
                myMarker.setName(cursor.getString(1));
                myMarker.setLatitude(cursor.getDouble(2));
                myMarker.setLongitude(cursor.getDouble(3));
                myMarker.setDistanceFrom(Integer.parseInt(cursor.getString(4)));
                markerList.add(myMarker);
            } while (cursor.moveToNext());
        }

        // return contact list
        return markerList;
    }

    public float updateMarker(MyMarker myMarker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DISTANCE_FROM, myMarker.getDistanceFrom());

        // updating row
        return db.update(TABLE_NAME, values, MARKER_ID + " = ?",
                new String[] { String.valueOf(myMarker.getID())});
    }


}
