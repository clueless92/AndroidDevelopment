package bg.tilchev.hw04task1;

import android.content.Context;
import android.widget.MediaController;

public class MusicController extends MediaController {

    public MusicController(Context context) {
        super(context);
    }

    @Override
    public void show(int timeout) {
        super.show(0);
    }
}
