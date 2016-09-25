package bg.tilchev.hw04task02;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Observable;
import java.util.Observer;

public class BatteryService extends Service implements Observer {

    private BateryChangedBinder binder;
    private int oldBateryPercents;
    private int newBateryPercents;
    private BatteryReciever receiver;

    public class BateryChangedBinder extends Binder {
        public BatteryService getService() {
            return BatteryService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.binder = new BateryChangedBinder();
        BroadCastHandler.getInstance().addObserver(this);
        this.newBateryPercents = -1;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.oldBateryPercents = this.newBateryPercents;
        this.startReceiver();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (this.oldBateryPercents == -1) {
            this.oldBateryPercents = (int) arg;
        }
        this.newBateryPercents = (int) arg;
    }

    public int getPercentChanged() {
        return Math.abs(this.oldBateryPercents - this.newBateryPercents);
    }

    private void startReceiver() {
        this.receiver = new BatteryReciever();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        this.registerReceiver(this.receiver, filter);
    }

    @Override
    public void onDestroy() {
        this.unregisterReceiver(this.receiver);
        super.onDestroy();
    }
}
