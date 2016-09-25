package bg.tilchev.hw04task02;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;


public class MainActivity extends AppCompatActivity implements Observer {

    private BatteryService bateryChangedService;
    private ServiceConnection connection;
    private boolean mIsBinded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_layout);

        this.connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                BatteryService.BateryChangedBinder binder = (BatteryService.BateryChangedBinder) service;
                MainActivity.this.bateryChangedService = binder.getService();
                MainActivity.this.mIsBinded = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                MainActivity.this.mIsBinded = false;
            }
        };
        BroadCastHandler.getInstance().addObserver(this);
        this.setService();
    }

    private TextView getBateryInfoTextView() {
        View view = this.findViewById(R.id.batary_info);
        if (view instanceof TextView) {
            return (TextView) view;
        }
        return null;
    }

    private void setService() {
        if (this.isMyServiceRunning(BatteryService.class)) {
            return;
        }
        Intent serciveIntent = new Intent(this, BatteryService.class);
        this.startService(serciveIntent);
        this.bindService(serciveIntent, this.connection, Context.BIND_AUTO_CREATE);
        PendingIntent pendIntent = PendingIntent.getService(this, 0, serciveIntent, 0);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // Start service every minute so to avoid waiting an hour
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendIntent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!this.isMyServiceRunning(BatteryService.class)) {
            return;
        }
        if (this.bateryChangedService == null) {
            return;
        }
        int percent = this.bateryChangedService.getPercentChanged();
        String text = String.format("Batery changed with %d %%", percent);
        TextView batteryInfo = this.getBateryInfoTextView();
        if (batteryInfo == null) {
            return;
        }
        batteryInfo.setText(text);
    }
}