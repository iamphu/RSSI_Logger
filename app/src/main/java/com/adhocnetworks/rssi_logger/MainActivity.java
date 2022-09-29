package com.adhocnetworks.rssi_logger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Request code to create RSSI files
    private static final int CREATE_FILE = 1;
    private final ArrayList<Integer> RSSI_list = new ArrayList<Integer>(); // Keeps track of the RSSI values
    // Measurement period in milliseconds
    private final int measurePeriod = 500;
    private final Handler handler = new Handler();    // Handler and runnable to create recurring measuring
    private Runnable rssiRunnable;
    // Keeps track of the amount of samples
    private int numSamples = 0;
    private boolean measureIsOn = false; // Measurement has started or not
    // WiFi variables
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    // Component variables
    private TextView sampleCount, previewValue;
    private ImageView rssiArrow;
    private Button buttonStart, buttonClear, buttonSave;
    private final float[] rssiIndicatorRange = {1.0f, 226.0f};

    private int getWiFiRSSI() {
        wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getRssi();
    }

    private void addRSSIMeasurement(int rssi) {
        RSSI_list.add(rssi);
        numSamples++;

        sampleCount.setText("Amount of samples: " + numSamples);
    }

    // NEW FILE STORAGE (API level 19+): https://developer.android.com/training/data-storage/shared/documents-files
    // Opens dialog window for the user to pick a location
    private void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        DateFormat df = new SimpleDateFormat("yyMMdd-HHmm");
        intent.putExtra(Intent.EXTRA_TITLE, "rssi_data_" + df.format(new Date()) + ".txt");
        startActivityForResult(intent, CREATE_FILE);
    }

    // Retrieves code based on what the user did during the dialog window
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
                createTextFile((uri));
            }
        }
    }

    // Stores the RSSI values to the user selected folder
    private void createTextFile(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream = new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write((RSSI_list.toString()).getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The RSSI value which is not logged, only serves as preview
     * @param rssi the integer value received from the WiFi manager
     */
    private void updateRSSIPreview(int rssi) {
        previewValue.setText(String.valueOf(rssi));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create textviews
        sampleCount = (TextView) findViewById(R.id.textview_samplecount);
        previewValue = (TextView) findViewById(R.id.textView_RSSI_preview);

        // Create imageview
        rssiArrow = (ImageView) findViewById(R.id.imageview_arrow);

        // Create buttons
        buttonStart = (Button) findViewById(R.id.button_start);
        buttonClear = (Button) findViewById(R.id.button_clear);
        buttonSave = (Button) findViewById(R.id.button_save);

        // Set WiFi manager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Create click listeners
        buttonStart.setOnClickListener(this);
        buttonClear.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

        // Create recurring measurement + update
        // Preview + measurement same runnable as RSSI gets updated roughly 2-3 sec (see below)
        // https://stackoverflow.com/questions/27339503/get-signal-strength-of-wifi-connection-from-android-drivers-in-terminal/63316932#63316932
        rssiRunnable = () -> {
            int newRSSI = getWiFiRSSI();

            if (this.measureIsOn) {
                this.addRSSIMeasurement(newRSSI);
            }

            // Update the preview RSSI number
            this.updateRSSIPreview(newRSSI);
            float ZRotationArrow = newRSSI*(this.rssiIndicatorRange[1] - this.rssiIndicatorRange[0])/(-100); // -100: max scale of the indicator
            ZRotationArrow = ZRotationArrow < this.rssiIndicatorRange[0] ? this.rssiIndicatorRange[0] : ZRotationArrow;
            ZRotationArrow = ZRotationArrow > this.rssiIndicatorRange[1] ? this.rssiIndicatorRange[1] : ZRotationArrow;
            rssiArrow.setRotation(ZRotationArrow);

            // Update the RSSI indicator
            handler.postDelayed(rssiRunnable, measurePeriod);

            Log.d("Runnable", "RSSI measured: " + newRSSI);
        };

        // Needs WiFi access which is given before app boots
        handler.postDelayed(rssiRunnable, measurePeriod);
    }

    // onClick events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start: { // Turn measuring on or off
                if (!measureIsOn) {
                    Log.d("Runnable", "Runnable started");
                    buttonStart.setText("Stop Measuring");
                    buttonSave.setEnabled(false);
                    buttonClear.setEnabled(false);
                    handler.postDelayed(rssiRunnable, measurePeriod);
                    measureIsOn = true;
                } else {
                    Log.d("Runnable", "Runnable stopped");
                    buttonStart.setText("Start Measuring");
                    handler.removeCallbacks(rssiRunnable);
                    buttonSave.setEnabled(true);
                    buttonClear.setEnabled(true);
                    measureIsOn = false;
                }
                break;
            }
            case R.id.button_clear: { // Clear data
                Log.d("Button", "Data cleared");
                RSSI_list.clear();
                numSamples = 0;
                sampleCount.setText("Amount of samples: " + numSamples);
//                values.setText(String.valueOf(0));
                break;
            }
            case R.id.button_save: { // Save data
                Log.d("File Storage", "Save button clicked");

                // Uses Android Storage Access Framework now
                createFile();

                break;
            }
            default:
                break;
        }
    }
}
