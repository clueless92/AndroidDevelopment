package bg.tilchev.imageencoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class AsyncImageEncoder extends AsyncTask<Bitmap, Integer, String> {

    public interface Listener {

        void onImageEncoded(String encodedImage);
    }

    private Listener mListener;
    private ProgressBar progressBar;
    private TextView textView;
    private Context appContext;

    public AsyncImageEncoder(Listener listener) {
        super();
        this.mListener = listener;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressBar.setVisibility(View.VISIBLE);
        this.textView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        this.progressBar.setProgress(values[0]);
        this.textView.setText(String.format("Encoding... %d", values[0]));
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        if (params == null) {
            return null;
        }
        if (params.length < 1) {
            return null;
        }
        Bitmap bitmap = params[0];
        if (bitmap == null) {
            return null;
        }

        int bounding = 500;
        float screenDensity = this.appContext.getResources().getDisplayMetrics().density;
        bitmap = this.scaleBitmap(bitmap, bounding, screenDensity);

        this.publishProgress(20);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.publishProgress(40);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        this.publishProgress(60);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        this.publishProgress(80);
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        this.publishProgress(100);
        return result;
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int desiredBounding, float screenDensity) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = Math.round((float)desiredBounding * screenDensity);

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = xScale <= yScale ? xScale : yScale;

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format more attune to the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return scaledBitmap;
    }

    @Override
    protected void onPostExecute(String result) {
        this.progressBar.setVisibility(View.INVISIBLE);
        this.textView.setVisibility(View.INVISIBLE);
        if (result != null) {
            this.mListener.onImageEncoded(result);
        }
    }
}
