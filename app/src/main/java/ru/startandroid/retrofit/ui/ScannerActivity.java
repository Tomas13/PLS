package ru.startandroid.retrofit.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import ru.startandroid.retrofit.Const;
import ru.startandroid.retrofit.R;

public class ScannerActivity extends AppCompatActivity implements
        BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    private static BarcodeReader barcodeReader;
    private AidcManager manager;

    boolean useTrigger = true;
    boolean btnPressed = false;

    Button button1, btnSwitch;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        textView = (TextView) findViewById(R.id.textView);

        btnSwitch = (Button) findViewById(R.id.btn_switch);
        button1 = (Button) findViewById(R.id.button1);
        // create the AidcManager providing a Context and a
        // CreatedCallback implementation.
        AidcManager.create(this, new AidcManager.CreatedCallback() {
            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                barcodeReader = manager.createBarcodeReader();

                try {
                    if (barcodeReader != null) {
                        Log.d("honeywellscanner: ", "barcodereader not claimed in OnCreate()");
                        barcodeReader.claim();
                    }
                    // apply settings
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, false);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);

                    // set the trigger mode to automatic control
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                            BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                } catch (UnsupportedPropertyException e) {
                    Toast.makeText(ScannerActivity.this, "Failed to apply properties",
                            Toast.LENGTH_SHORT).show();
                } catch (ScannerUnavailableException e) {
                    Toast.makeText(ScannerActivity.this, "Failed to claim scanner",
                            Toast.LENGTH_SHORT).show();
                    //e.printStackTrace();
                }

                // register bar code event listener
                barcodeReader.addBarcodeListener(ScannerActivity.this);
            }
        });

        ActivitySetting();
    }

    @Override
    public void onResume() {  //will always? be called before app becomes visible?
        super.onResume();
        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
                Log.d("noneywellscanner: ", "scanner claimed");
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (barcodeReader != null)
            barcodeReader.release();
    }

    /**
     * Create buttons to launch demo activities.
     */
    public void ActivitySetting() {
        button1.setOnClickListener(v ->
        {

            if (barcodeReader != null) {
                try {
                    barcodeReader.softwareTrigger(true);
                } catch (ScannerNotClaimedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (ScannerUnavailableException e) {
                    e.printStackTrace();
                }

            } else {
                showToastMsg("Barcodereader not available");
            }
        });


        btnSwitch.setOnClickListener(view ->
                startActivityForResult(new Intent(ScannerActivity.this, ScannerSelectionBarcodeActivity.class),
                Const.ScannerSelectionBarcodeActivity));
    }

    private void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (barcodeReader != null) {
            // close BarcodeReader to clean up resources.
            barcodeReader.close();
            barcodeReader = null;
        }

        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager.close();
        }
    }

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent barcodeReadEvent) {
        // TODO Auto-generated method stub
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String barcodeData = barcodeReadEvent.getBarcodeData();
                String timestamp = barcodeReadEvent.getTimestamp();
                // update UI to reflect the data
                String s = (String) textView.getText();
                s+="\n"+barcodeData+"\n"+timestamp;
                textView.setText(s);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.ScannerSelectionBarcodeActivity){ //scannerSelectionBarcodeActivity
            if (data != null){
                final String scanner = data.getStringExtra("scanner");


                // create the AidcManager providing a Context and a
                // CreatedCallback implementation.
                AidcManager.create(this, new AidcManager.CreatedCallback() {
                    @Override
                    public void onCreated(AidcManager aidcManager) {
                        manager = aidcManager;
                        barcodeReader = manager.createBarcodeReader(scanner);

                        try {
                            if (barcodeReader != null) {
                                Log.d("honeywellscanner: ", "barcodereader not claimed in OnCreate()");
                                barcodeReader.claim();
                            }
                            // apply settings
                            barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, false);
                            barcodeReader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);

                            // set the trigger mode to automatic control
                            barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                                    BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                        } catch (UnsupportedPropertyException e) {
                            Toast.makeText(ScannerActivity.this, "Failed to apply properties",
                                    Toast.LENGTH_SHORT).show();
                        } catch (ScannerUnavailableException e) {
                            Toast.makeText(ScannerActivity.this, "Failed to claim scanner",
                                    Toast.LENGTH_SHORT).show();
                            //e.printStackTrace();
                        }

                        // register bar code event listener
                        barcodeReader.addBarcodeListener(ScannerActivity.this);
                    }});

                    }
        }


    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {

    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

    }
}
