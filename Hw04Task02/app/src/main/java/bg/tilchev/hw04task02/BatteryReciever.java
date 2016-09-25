package bg.tilchev.hw04task02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class BatteryReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        BroadCastHandler.getInstance().updateValue(level);
    }
}
