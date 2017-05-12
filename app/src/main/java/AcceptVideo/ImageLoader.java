package AcceptVideo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import JavaBeans.URLStore;

/**
 * Created by Bobo on 2016/8/4.
 */
public class ImageLoader {
    private static LruCache<String, Bitmap> lCache;

    public ImageLoader() {
        //防止多次创建
        if (lCache == null) {
            lCache = new LruCache<String, Bitmap>((int) Runtime.getRuntime().maxMemory() / 8) {
                @Override
                protected int sizeOf(String id, Bitmap bitmap) {
                    return bitmap.getByteCount();
                }
            };
        }
    }

    public void setImageFromId(ImageView imageView, String id) {
        new LoaderAsyncTask(imageView, id).execute(id);
    }

    private class LoaderAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        private String id;

        public LoaderAsyncTask(ImageView imageView, String id) {
            this.imageView = imageView;
            this.id = id;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmap = lCache.get(params[0]);
            if (bmap != null)
                return bmap;
            return getBitmapById(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (lCache.get(id) == null && bitmap != null)
                lCache.put(id, bitmap);
            if (imageView.getTag().equals(id))
                imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap getBitmapById(String id) {
        Bitmap bitmap = null;

        String urlStr = /*URLStore.VIDEO_IMAGE_URL +*/ id + ".jpg";

        try {
            InputStream is = new URL(urlStr).openStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}
