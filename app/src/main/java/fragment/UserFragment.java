package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bobo.myapplication.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import DataBaseHelper.DBHelper;
import JavaBeans.PictureTrim;

/**
 * Created by Bobofree on 2017/5/12.
 */
public class UserFragment extends Fragment implements View.OnClickListener {
    private LinearLayout about_app = null;
    private LinearLayout share_app = null;
    private LinearLayout exit_app = null;
    private DBHelper dbHelper = null;
    private ImageView user_img = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View userView = inflater.inflate(R.layout.user_fragment, container, false);
        initView(userView);
        initData();
        return userView;
    }

    private void initData() {
        dbHelper = new DBHelper(getContext());
    }

    private void initView(View userView) {
        about_app = (LinearLayout) userView.findViewById(R.id.about_app);
        about_app.setOnClickListener(this);

        share_app = (LinearLayout) userView.findViewById(R.id.share_app);
        share_app.setOnClickListener(this);

        exit_app = (LinearLayout) userView.findViewById(R.id.exit_app);
        exit_app.setOnClickListener(this);

        user_img = (ImageView) userView.findViewById(R.id.user_img);
        user_img.setOnClickListener(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.login);
        user_img.setImageBitmap(PictureTrim.toRoundCorner(bitmap, 400));
    }

    //分享文字
    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String imgPath = "";
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, "title");
        intent.putExtra(Intent.EXTRA_TEXT, "污水监控APP");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    //APP信息
    private void showAboutApp(View view) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.left_about_app, (ViewGroup) view.findViewById(R.id.left_about_app));

        new AlertDialog.Builder(getContext()).setView(layout).setTitle("关于")
                .setPositiveButton("确定", null).show();
    }

    private void exitApp() {
        Cursor cursor = dbHelper.getAll("", "");
        if (null != cursor && cursor.moveToFirst()) {
            dbHelper.update(cursor.getInt(cursor.getColumnIndex("id")) + "", "state", "used");
        }
        onDestroy();
        System.exit(0);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.about_app:
                showAboutApp(view);
                break;
            case R.id.share_app:
                shareApp();
                break;
            case R.id.exit_app:
                exitApp();
                break;
            case R.id.user_img:
                gotoUserCentre();
                break;
            default:
                break;
        }
    }

    private void gotoUserCentre() {
        Toast.makeText(getContext(), "user", Toast.LENGTH_SHORT).show();
    }

}
