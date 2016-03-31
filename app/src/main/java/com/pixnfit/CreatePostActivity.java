package com.pixnfit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.pixnfit.common.Image;
import com.pixnfit.common.Post;
import com.pixnfit.ws.AddImageToPostAsyncTask;
import com.pixnfit.ws.CreateImageAsyncTask;
import com.pixnfit.ws.CreatePostAsyncTask;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CreatePostActivity.class.getSimpleName();

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imageView;
    private int imageViewWidth, imageViewHeight;
    private File imageFile;
    private boolean photoTaken = false;
    private EditText postTitleEditText;
    private EditText postDescriptionEditText;
    private FloatingActionButton fabCreatePost;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        imageView = (ImageView) findViewById(R.id.cameraImageView);
        postTitleEditText = (EditText) findViewById(R.id.postTitleEditText);
        postDescriptionEditText = (EditText) findViewById(R.id.postDescriptionEditText);
        fabCreatePost = (FloatingActionButton) findViewById(R.id.fabCreatePost);

        fabCreatePost.setOnClickListener(this);

        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                Log.e(TAG, "Impossible to create image file", e);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    // On va afficher l'image
                    Log.i(TAG, "Photo taken OK !");
                    photoTaken = true;
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && photoTaken) {
            AsyncSetImage asyncSetImage = new AsyncSetImage(imageView);
            asyncSetImage.execute(imageFile);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "PixnFit_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.fabCreatePost:
                if (imageFile != null) {
                    // On affiche un dialogue d'attente, le temps d'envoyer l'image sur le réseau...
                    progressDialog = ProgressDialog.show(this, "Please wait", "Uploading picture...", true);

                    Post post = new Post();
                    post.name = postTitleEditText.getText().toString();
                    post.description = postDescriptionEditText.getText().toString();
                    CreatePostAsyncTask createPostAsyncTask = new CreatePostAsyncTask(this) {
                        @Override
                        protected void onPostExecute(final Post post) {
                            super.onPostExecute(post);
                            if (!isCancelled()) {
                                if (post != null) {
                                    CreateImageAsyncTask createImageAsyncTask = new CreateImageAsyncTask(getApplication()) {
                                        @Override
                                        protected void onPostExecute(List<Image> images) {
                                            super.onPostExecute(images);
                                            if (!isCancelled()) {
                                                if (images != null && !images.isEmpty()) {
                                                    post.images = images;
                                                    AddImageToPostAsyncTask addImageToPostAsyncTask = new AddImageToPostAsyncTask(getContext(), post) {
                                                        @Override
                                                        protected void onPostExecute(List<Post> posts) {
                                                            super.onPostExecute(posts);
                                                            if (!isCancelled()) {
                                                                if (posts != null && !posts.isEmpty()) {
                                                                    Intent i = new Intent();
                                                                    i.putExtra("posts", (Serializable) posts);
                                                                    setResult(Activity.RESULT_OK, i);
                                                                    progressDialog.dismiss();
                                                                    finish();
                                                                } else {
                                                                    Snackbar.make(v, "Impossible to create post : addImageToPostAsyncTask failed", Snackbar.LENGTH_LONG);
                                                                    progressDialog.dismiss();
                                                                }
                                                            }
                                                        }
                                                    };
                                                    addImageToPostAsyncTask.execute();
                                                } else {
                                                    Snackbar.make(v, "Impossible to create post : createImageAsyncTask failed", Snackbar.LENGTH_LONG);
                                                    progressDialog.dismiss();
                                                }
                                            }
                                        }
                                    };
                                    createImageAsyncTask.execute(imageFile);
                                } else {
                                    Snackbar.make(v, "Impossible to create post : createPostAsyncTask failed", Snackbar.LENGTH_LONG);
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    };
                    createPostAsyncTask.execute(post);
                }
                break;
            default:
                break;
        }
    }
}


class AsyncSetImage extends AsyncTask<File, Void, Void> {

    private ImageView imageView;
    private final int imageViewWidth;
    private final int imageViewHeight;
    private Bitmap bitmap;

    public AsyncSetImage(ImageView imageView) {
        this.imageView = imageView;
        this.imageViewWidth = imageView.getWidth();
        this.imageViewHeight = imageView.getHeight();
    }

    @Override
    protected Void doInBackground(File... imageFiles) {
        File imageFile = imageFiles[0];
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / imageViewWidth, photoH / imageViewHeight);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (!isCancelled()) {
            imageView.setImageBitmap(bitmap);
        }
    }
}