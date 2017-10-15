package bg.tilchev.imageencoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AsyncImageDecoder extends AsyncTask<String, Integer, Bitmap> {

    public interface Listener {

        void onImageDecoded(Bitmap bitmap);
    }

    private Listener mListener;
    private ProgressBar progressBar;
    private TextView textView;

    public AsyncImageDecoder(Listener listener) {
        super();
        this.mListener = listener;
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
        this.textView.setText(String.format("Decoding... %d", values[0]));
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        this.publishProgress(20);
        if (params == null) {
            return null;
        }
        if (params.length < 1) {
            return null;
        }
        this.publishProgress(40);
        String base64ImgStr = params[0];
        if (base64ImgStr == null) {
            return null;
        }
        this.publishProgress(60);
        byte[] decodedString = Base64.decode(base64ImgStr, Base64.DEFAULT);
        this.publishProgress(80);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        bitmap = Bitmap.createScaledBitmap(bitmap, 512, 512, false);
        this.publishProgress(100);
        return bitmap;
    }

    protected void onPostExecute(Bitmap resultBitmap) {
        this.progressBar.setVisibility(View.INVISIBLE);
        this.textView.setVisibility(View.INVISIBLE);
        if (this.mListener != null) {
            this.mListener.onImageDecoded(resultBitmap);
        }
    }
}
