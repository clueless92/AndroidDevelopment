package bg.tilchev.imageencoder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements
        AsyncImageEncoder.Listener,
        AsyncImageDecoder.Listener {

    private static final int CAMERA_REQUEST = 0;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 2;

    private ProgressBar getProgressBar() {
        ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
        progressBar.setMax(100);
        progressBar.setVisibility(View.INVISIBLE);
        return progressBar;
    }

    private TextView getTextView() {
        TextView textView = (TextView) this.findViewById(R.id.text_view);
        textView.setVisibility(View.INVISIBLE);
        return textView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.requestPermissions();
    }

    public void onBrowseClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onCameraClicked(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != AppCompatActivity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            this.onBrowseResult(data);
        } else if (requestCode == CAMERA_REQUEST) {
            this.onCameraResult(data);
        }
    }

    private void onCameraResult(Intent data) {
        Bitmap bitmap = null;
        if (data.getData() == null) {
            bitmap = (Bitmap) data.getExtras().get("data");
        } else {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AsyncImageEncoder encoder = new AsyncImageEncoder(this);
        encoder.setProgressBar(this.getProgressBar());
        encoder.setTextView(this.getTextView());
        encoder.setAppContext(this.getApplicationContext());
        encoder.execute(bitmap);
    }

    private void onBrowseResult(Intent data) {
        Uri uri = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            AsyncImageEncoder encoder = new AsyncImageEncoder(this);
            encoder.setProgressBar(this.getProgressBar());
            encoder.setTextView(this.getTextView());
            encoder.setAppContext(this.getApplicationContext());
            encoder.execute(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageEncoded(String base64str) {
        Toast.makeText(this, "Image encoded", Toast.LENGTH_SHORT).show();
        AsyncImageDecoder decoder = new AsyncImageDecoder(this);
        decoder.setProgressBar(this.getProgressBar());
        decoder.setTextView(this.getTextView());
        decoder.execute(base64str);
    }

    @Override
    public void onImageDecoded(Bitmap bitmap) {
        Toast.makeText(this, "Image decoded", Toast.LENGTH_SHORT).show();
        ImageView imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int desiredBound) {
        // Get current dimensions AND the desired bounding box
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int bounding = this.dpToPx(desiredBound);

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format more attune to the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return scaledBitmap;
    }

    private int dpToPx(int dp) {
        float density = this.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Button cameraButton = (Button) this.findViewById(R.id.camera_button);
        Button browseButton = (Button) this.findViewById(R.id.browse_button);
        cameraButton.setEnabled(false);
        browseButton.setEnabled(false);
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE }, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Button cameraButton = (Button) this.findViewById(R.id.camera_button);
        Button browseButton = (Button) this.findViewById(R.id.browse_button);
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            cameraButton.setEnabled(true);
            browseButton.setEnabled(true);
        }
    }
}
