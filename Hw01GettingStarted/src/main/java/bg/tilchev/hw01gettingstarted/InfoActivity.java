package bg.tilchev.hw01gettingstarted;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Todor Ilchev on 2016-09-04.
 */
public class InfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_info);
        Intent intent = this.getIntent();
        String info = intent.getStringExtra("info");
        TextView infoTextView = (TextView) this.findViewById(R.id.textView2);
        infoTextView.setText(info);
    }
}
