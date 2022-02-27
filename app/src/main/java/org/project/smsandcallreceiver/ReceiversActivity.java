package org.project.smsandcallreceiver;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.project.smsandcallreceiver.services.BackgroundService;

public class ReceiversActivity extends AppCompatActivity {
    private static final int APPS_PERMISSIONS_CALL = 10001;
    private static final int APPS_PERMISSIONS_CALL_NUMBER = 10002;
    private static final int APPS_PERMISSIONS_SMS = 10000;

    private static final String TAG = ReceiversActivity.class.getSimpleName();
    public static ReceiversActivity instance = null;

    public void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForSmsPermission();
        Button btnStart = findViewById(R.id.btnStartService);
        btnStart.setOnClickListener((View.OnClickListener) view -> {
            startForegroundService(new Intent(this, BackgroundService.class));
        });
        Button btnStop = findViewById(R.id.btnStopService);
        btnStop.setOnClickListener((View.OnClickListener) view -> {
            stopService(new Intent(this, BackgroundService.class));
        });
    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != 0) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, APPS_PERMISSIONS_SMS);
            return;
        }
        checkForCallNumberPermission();
    }

    private void checkForCallNumberPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != 0) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, APPS_PERMISSIONS_CALL_NUMBER);
            return;
        }
        checkForCallPermission();
    }

    private void checkForCallPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != 0) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, APPS_PERMISSIONS_CALL);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case APPS_PERMISSIONS_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.RECEIVE_SMS) && grantResults[0] == 0) {
                    checkForCallNumberPermission();
                } else {
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case APPS_PERMISSIONS_CALL_NUMBER : {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.READ_CALL_LOG) && grantResults[0] == 0) {
                    checkForCallPermission();
                } else {
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case APPS_PERMISSIONS_CALL: {
                if (!permissions[0].equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE) || grantResults[0] != 0) {
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
