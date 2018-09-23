package com.tbc.vasa.ipfscloud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;


import java.io.IOException;

public class Sync extends AppCompatActivity {

    private Button sync;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private RelativeLayout sync_layout;
    private TextView scan_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        sync = (Button) findViewById(R.id.sync);
        sync_layout = (RelativeLayout) findViewById(R.id.sync_layout);
        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        scan_label = (TextView) findViewById(R.id.scan_label);

        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //noinspection MissingPermission
                    if (ActivityCompat.checkSelfPermission(Sync.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialize();
                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {
                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                        if (barcodes.size() != 0) {
                            System.out.println("pubKey: "+barcodes.valueAt(0).displayValue);
                            //Saving pubKey to the app
                            SharedPreferences.Editor editor = getSharedPreferences("account", MODE_PRIVATE).edit();
                            editor.putString("pubKey1", barcodes.valueAt(0).displayValue);
                            editor.apply();

                            Intent i = new Intent(Sync.this, Documents.class);
                            startActivity(i);
                        }
                    }
                });
            }
        });
    }

    private void initialize() {

        sync_layout.setVisibility(View.INVISIBLE);
        cameraView.setVisibility(View.VISIBLE);
        scan_label.setVisibility(View.VISIBLE);
    }
}
