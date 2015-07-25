package oompa.loompa.blast;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Ethan on 7/24/2015.
 */
public class ImageHelper extends AsyncTask<ImageView, Void, Drawable> {

    private Exception exception;

    private ImageView image;

    //Retrieve an image from an online source, without downloading it.
    protected Drawable doInBackground(ImageView... view) {
        this.image = view[0];
        try {
            InputStream is = (InputStream) new URL((String) image.getTag()).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            this.exception = e;
            System.out.println(exception);
            return null;
        }
    }

    protected void onPostExecute(Drawable result) {
        image.setImageDrawable(result);
    }
}
