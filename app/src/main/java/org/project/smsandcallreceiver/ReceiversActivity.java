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

import org.json.JSONArray;
import org.project.smsandcallreceiver.helpers.InternalStorage;
import org.project.smsandcallreceiver.helpers.ServerHelper;
import org.project.smsandcallreceiver.services.BackgroundService;
import org.project.smsandcallreceiver.threads.InternetThread;
import org.project.smsandcallreceiver.threads.SingletonThreadStopper;

import java.io.IOException;

public class ReceiversActivity extends AppCompatActivity {
    private static final int APPS_PERMISSIONS_CALL = 10001;
    private static final int APPS_PERMISSIONS_CALL_NUMBER = 10002;
    private static final int APPS_PERMISSIONS_SMS = 10000;
    private static final int APPS_PERMISSIONS_READ_PHONE_NUMBERS = 10003;
    private static final int APPS_PERMISSIONS_READ_SMS = 10004;

    private static final String TAG = ReceiversActivity.class.getSimpleName();
    public static ReceiversActivity instance = null;
    private InternetThread internetThread = null;

    public void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServerHelper.init();
        checkForSmsPermission();
        Button btnStart = findViewById(R.id.btnStartService);
        btnStart.setOnClickListener(view -> {
            SingletonThreadStopper singleton = SingletonThreadStopper.INSTANCE;
            singleton.setStatusRun(true);
            internetThread = new InternetThread("thread_internet_connection");
            internetThread.start();
            startForegroundService(new Intent(this, BackgroundService.class));
        });
        Button btnStop = findViewById(R.id.btnStopService);
        btnStop.setOnClickListener(view -> {
            SingletonThreadStopper singleton = SingletonThreadStopper.INSTANCE;
            singleton.setStatusRun(false);
            stopService(new Intent(this, BackgroundService.class));
        });
        try {
            InternalStorage singleton = InternalStorage.INSTANCE;
            singleton.saveData(new JSONArray());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
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

    private void checkForCallReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != 0) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_NUMBERS}, APPS_PERMISSIONS_READ_PHONE_NUMBERS);
        }
    }

    private void checkForSmsReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != 0) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, APPS_PERMISSIONS_READ_SMS);
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
                if (permissions[0].equalsIgnoreCase(Manifest.permission.READ_PHONE_STATE) && grantResults[0] == 0) {
                    checkForCallReadPermission();
                } else {
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case APPS_PERMISSIONS_READ_PHONE_NUMBERS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.READ_PHONE_NUMBERS) && grantResults[0] == 0) {
                    checkForSmsReadPermission();
                } else {
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case APPS_PERMISSIONS_READ_SMS:{
                if (!permissions[0].equalsIgnoreCase(Manifest.permission.READ_SMS) || grantResults[0] != 0) {
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this, getString(R.string.failure_permission), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

// TODO возможны коллизии при одновременной записи в файл
// TODO возможны коллизии при одновременно обработке и отлове смс и звонков в другом потоке

