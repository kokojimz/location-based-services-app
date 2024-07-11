package com.example.location_based_services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSION_REQUEST_CODE = 1;
    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.mapview);
        String[] permissionsString = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        requestPermissionIfNecessary(permissionsString);
        map.setTileSource(TileSourceFactory.MAPNIK);

        addPOIMarkers();

        recenterMap(new GeoPoint(-6.9153653, 107.5886954)); // Kampus Binus Bandung

        addRecenterButtons();
    }

    void requestPermissionIfNecessary(String[] permissions){
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission: permissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionsToRequest.add(permission);
            };
        }
        if(permissionsToRequest.size() > 0){
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSION_REQUEST_CODE);
        }
    }

    private void addPOIMarkers() {
        addMarker(new GeoPoint(-6.9153653, 107.5886954), "Kampus Binus Bandung");
        addMarker(new GeoPoint(-6.9178283, 107.6045685), "Braga");
        addMarker(new GeoPoint(-6.9218295, 107.6021967), "Alun-Alun Kota Bandung");
        addMarker(new GeoPoint(-6.9002779, 107.6161296), "Lapangan Gazibu");
    }

    private void addMarker(GeoPoint point, String title) {
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setTitle(title);

        Bitmap bitmap = getBitmapFromVectorDrawable(this, R.drawable.baseline_location_pin);
        if (bitmap != null) {
            marker.setIcon(new BitmapDrawable(getResources(), bitmap));
        }

        map.getOverlays().add(marker);
    }

    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, drawableId);
        if (vectorDrawable == null) {
            return null;
        }
        vectorDrawable = DrawableCompat.wrap(vectorDrawable).mutate();
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private void addRecenterButtons() {
        Button btnBinus = findViewById(R.id.btnBinus);
        Button btnBraga = findViewById(R.id.btnBraga);
        Button btnAlunAlun = findViewById(R.id.btnAlunAlun);
        Button btnGazibu = findViewById(R.id.btnGazibu);

        btnBinus.setOnClickListener(v -> recenterMap(new GeoPoint(-6.9153653, 107.5886954)));
        btnBraga.setOnClickListener(v -> recenterMap(new GeoPoint(-6.9178283, 107.6045685)));
        btnAlunAlun.setOnClickListener(v -> recenterMap(new GeoPoint(-6.9218295, 107.6021967)));
        btnGazibu.setOnClickListener(v -> recenterMap(new GeoPoint(-6.9002779, 107.6161296)));
    }

    private void recenterMap(GeoPoint point) {
        map.getController().setCenter(point);
        map.getController().setZoom(18.0);
    }
}
