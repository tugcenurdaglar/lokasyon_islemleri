package com.tugcenurdaglar.lokasyonislemleri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private int izinKontrol;
    private Button buttonKonumAl;
    private TextView textViewEnlem, textViewBoylam;

    private String konumSaglayici = "gps";

    private LocationManager locationManager; //bununla verileri alırım

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonKonumAl = findViewById(R.id.buttonKonumAl);
        textViewBoylam = findViewById(R.id.textViewBoylam);
        textViewEnlem = findViewById(R.id.textViewEnlem);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //Bağlandı, artık lokasyon işlemleri yapılabilir

        //butona tıklanıldığında izin kontrolü yapılsın
        buttonKonumAl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //manifest dosyasında izin verilmiş fakat arkaplanda kullanıcının bu izni verip vermediğni anlamak için

                izinKontrol = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

                //ifle kontrol sağlayalım

                if (izinKontrol != PackageManager.PERMISSION_GRANTED){ //Arkaplanda aktif hale getirilmiş mi?
                    //daha önce izin verilmemişse burası
                    //izin isteği gönderilir
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},100);
                }
                else {
                    //daha önce izin verilmişse  burası çalışır

                    Location konum = locationManager.getLastKnownLocation(konumSaglayici);
                    //en son bilinen lokasyon alındı

                    if (konum!=null){
                        onLocationChanged(konum);
                    }else {
                        textViewBoylam.setText("Konum Aktif Değil");
                        textViewEnlem.setText("Konum Aktif Değil");
                    }

                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100){

            izinKontrol = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplication(), "İZİN VERİLDİ", Toast.LENGTH_SHORT).show();

                Location konum = locationManager.getLastKnownLocation(konumSaglayici);
                //en son bilinen lokasyon alındı

                if (konum!=null){
                    onLocationChanged(konum);
                }else {
                    textViewBoylam.setText("Konum Aktif Değil");
                    textViewEnlem.setText("Konum Aktif Değil");
                }

            }else {
                Toast.makeText(getApplicationContext(),"İZİN VERİLMEDİ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //bu metod, location ile enlem ve boylamı verir

        double enlem = location.getLatitude();
        double boylam = location.getLongitude();

        textViewBoylam.setText("Boylam : "+boylam);
        textViewEnlem.setText("Enlem : " +enlem);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}