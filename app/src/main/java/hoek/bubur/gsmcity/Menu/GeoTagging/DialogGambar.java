package hoek.bubur.gsmcity.Menu.GeoTagging;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import hoek.bubur.gsmcity.BaseDialog;
import hoek.bubur.gsmcity.Conf;
import hoek.bubur.gsmcity.Model.RTH;
import hoek.bubur.gsmcity.R;

/**
 * Created by dalbo on 5/23/2017.
 */

public class DialogGambar extends BaseDialog {


    @BindView(R.id.cobaa)
    ImageView cobaa;
    Context context;
    RTH rth;
    String wsAddr;
    public DialogGambar(@NonNull Context context, RTH rth) {
        super(context);
        this.context = context;
        this.rth = rth;
        this.wsAddr = new Conf(context).getConf(Conf.CONF_WSADDR);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_preview_gambar);
        ButterKnife.bind(this);
        cobaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
//
//        Glide.with(getContext()).load("http://192.168.137.1/gsmcity/foto/159239584e0aa8.jpg").listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                Log.i("GLIDE", "Fail");
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                Log.i("GLIDE", "SUCCESS");
//                return false;
//            }
//        }).into(cobaa);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(wsAddr+rth.getUrlGambar());
                    final Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cobaa.setImageBitmap(bitmap);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
