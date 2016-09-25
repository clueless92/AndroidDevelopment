package bg.tilchev.hw04task02;

import java.util.Observable;

public class BroadCastHandler extends Observable {

    private static BroadCastHandler handler;

    public static BroadCastHandler getInstance() {
        if(handler == null) {
            handler = new BroadCastHandler();
        }
        return handler;
    }

    public synchronized void updateValue(Object data) {
        this.setChanged();
        this.notifyObservers(data);
    }
}
