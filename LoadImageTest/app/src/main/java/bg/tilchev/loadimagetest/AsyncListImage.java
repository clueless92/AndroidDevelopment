package bg.tilchev.loadimagetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AsyncListImage extends AsyncTask<Void, Void, Bitmap> {

    private String imgString;
    private ImageView imageView;
    private Context context;
    private static final Map<String, Bitmap> hashMap = new HashMap<>();

    public AsyncListImage(String imgString, ImageView imageView, Context context) {
        this.imgString = imgString;
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap bitmap = null;
        if (hashMap.containsKey(this.imgString)) {
            bitmap = hashMap.get(this.imgString);
        }
        else {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(this.imgString).getContent());
                hashMap.put(this.imgString, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap resultBitmat) {
        double density = this.context.getResources().getDisplayMetrics().density;
        if (resultBitmat != null) {
            this.imageView.setImageBitmap(
                    RoundImageCornersUtil.getRoundedCornerImage(
                            resultBitmat,
                            (int) (100*density),
                            (int) (70*density),
                            (int) (2*density),
                            (int) (10*density)
                    )
            );
        }
    }
}
