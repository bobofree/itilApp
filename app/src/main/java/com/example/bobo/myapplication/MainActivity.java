package com.example.bobo.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import DataBaseHelper.DBHelper;
import fragment.ListFragment;
import fragment.MapFragment;
import fragment.FoundFragment;
import fragment.UserFragment;

public class MainActivity extends Activity
        implements View.OnClickListener {

    private LinearLayout drawer = null;
    private View contentView = null;
    private ImageView imageView = null;
    private LinearLayout layout_tab_course = null;
    private LinearLayout layout_tab_download = null;
    private LinearLayout layout_tab_found = null;
    private LinearLayout layout_tab_user = null;
    private ImageView iv_tab_course_img = null;
    private ImageView iv_tab_download_img = null;
    private ImageView iv_tab_found_img = null;
    private ImageView iv_tab_user_img = null;
    private TextView tv_user = null;
    private TextView tv_course = null;
    private TextView tv_download = null;
    private TextView tv_found = null;
    private Toolbar toolbar = null;

    private DBHelper dbHelper = null;

    public static Context mainContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainContext = getApplicationContext();

        initData();

        //initDrawerAction(drawer);

        initFragment();

        //initUserData();

        //百度地图的初始化
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        Cursor cursor = dbHelper.getAll("", "");
        if (null != cursor && cursor.moveToFirst()) {
            String dateStr = cursor.getString(cursor.getColumnIndex("logintime"));
            Date lointime = null;
            try {
                lointime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long intervals = new Date().getTime() - lointime.getTime();
            //超过3天:3 * 24 * 60 * 60 * 1000
            if (intervals > 3 * 24 * 60 * 60 * 1000) {
                dbHelper.update(cursor.getInt(cursor.getColumnIndex("id")) + "", "state", "used");
            }
        }
        super.onDestroy();
    }

    /*private void initUserData() {
        userBean = (UserBean) getIntent().getSerializableExtra("userBean");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        leftNameItem = (MenuItem) menu.findItem(R.id.left_name);
        leftAgeItem = (MenuItem) menu.findItem(R.id.left_age);
        leftGradeItem = (MenuItem) menu.findItem(R.id.left_grade);
        leftSeeLogItem = (MenuItem) menu.findItem(R.id.left_see_log);

        if (null != userBean) {
            leftNameItem.setTitle("昵称：" + userBean.getUserName());
            leftAgeItem.setTitle("生日：" + userBean.getCreateDate());
            leftGradeItem.setTitle("等级：4");
        }
    }*/

    private void initFragment() {
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment cFragment = new ListFragment();
        fTransaction.add(R.id.frame_layout, cFragment, "course");
        fTransaction.show(cFragment);
        fTransaction.commit();
        selectCourseTab();
    }


    private void initData() {
        dbHelper = new DBHelper(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //界面控制
        drawer = (LinearLayout) findViewById(R.id.drawer_layout);
        //主界面
        contentView = drawer.findViewById(R.id.content_view);

        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        /*NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        /*imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.login);
        imageView.setImageBitmap(PictureTrim.toRoundCorner(bitmap, 400));*/

        layout_tab_course = (LinearLayout) findViewById(R.id.id_tab_course);
        layout_tab_course.setOnClickListener(this);
        layout_tab_download = (LinearLayout) findViewById(R.id.id_tab_download);
        layout_tab_download.setOnClickListener(this);
        layout_tab_found = (LinearLayout) findViewById(R.id.id_tab_found);
        layout_tab_found.setOnClickListener(this);
        layout_tab_user = (LinearLayout) findViewById(R.id.id_tab_user);
        layout_tab_user.setOnClickListener(this);

        iv_tab_course_img = (ImageView) findViewById(R.id.id_tab_course_img);
        iv_tab_download_img = (ImageView) findViewById(R.id.id_tab_download_img);
        iv_tab_found_img = (ImageView) findViewById(R.id.id_tab_found_img);
        iv_tab_user_img = (ImageView) findViewById(R.id.id_tab_user_img);

        tv_course = (TextView) findViewById(R.id.textView1);
        tv_download = (TextView) findViewById(R.id.textView2);
        tv_found = (TextView) findViewById(R.id.textView3);
        tv_user = (TextView) findViewById(R.id.textView4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView:
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.putExtra("modify", true);
                startActivity(i);
                break;
            case R.id.id_tab_course:
                switchoverCourseFragment();
                selectCourseTab();
                break;
            case R.id.id_tab_download:
                switchoverDownloadFragment();
                selectDownloadTab();
                break;
            case R.id.id_tab_found:
                switchoverFounddFragment();
                selectFoundTab();
                break;
            case R.id.id_tab_user:
                switchoverUserFragment();
                selectUserTab();
                break;
            default:
                break;
        }
    }

    private void switchoverUserFragment() {
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment cFragment = fManager.findFragmentByTag("course");
        Fragment dFragment = fManager.findFragmentByTag("download");
        Fragment fFragment = fManager.findFragmentByTag("found");
        Fragment uFragment = fManager.findFragmentByTag("user");
        if (uFragment == null) {
            uFragment = new UserFragment();
            fTransaction.add(R.id.frame_layout, uFragment, "user");
        }

        if (cFragment != null)
            fTransaction.hide(cFragment);
        if (dFragment != null)
            fTransaction.hide(dFragment);
        if (fFragment != null)
            fTransaction.hide(fFragment);

        fTransaction.show(uFragment);
        fTransaction.commit();
    }

    private void switchoverFounddFragment() {
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment cFragment = fManager.findFragmentByTag("course");
        Fragment dFragment = fManager.findFragmentByTag("download");
        Fragment fFragment = fManager.findFragmentByTag("found");
        Fragment uFragment = fManager.findFragmentByTag("user");
        if (fFragment == null) {
            fFragment = new FoundFragment();
            fTransaction.add(R.id.frame_layout, fFragment, "found");
        }

        if (cFragment != null)
            fTransaction.hide(cFragment);
        if (dFragment != null)
            fTransaction.hide(dFragment);
        if (uFragment != null)
            fTransaction.hide(uFragment);

        fTransaction.show(fFragment);
        fTransaction.commit();
    }

    private void switchoverDownloadFragment() {
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment cFragment = fManager.findFragmentByTag("course");
        Fragment dFragment = fManager.findFragmentByTag("download");
        Fragment fFragment = fManager.findFragmentByTag("found");
        Fragment uFragment = fManager.findFragmentByTag("user");
        if (dFragment == null) {
            dFragment = new MapFragment();
            fTransaction.add(R.id.frame_layout, dFragment, "download");
        }

        if (cFragment != null)
            fTransaction.hide(cFragment);
        if (fFragment != null)
            fTransaction.hide(fFragment);
        if (uFragment != null)
            fTransaction.hide(uFragment);

        fTransaction.show(dFragment);
        fTransaction.commit();
    }

    private void switchoverCourseFragment() {
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        Fragment cFragment = fManager.findFragmentByTag("course");
        Fragment dFragment = fManager.findFragmentByTag("download");
        Fragment fFragment = fManager.findFragmentByTag("found");
        Fragment uFragment = fManager.findFragmentByTag("user");
        if (cFragment == null) {
            cFragment = new ListFragment();
            fTransaction.add(R.id.frame_layout, cFragment, "course");
        }
        if (dFragment != null)
            fTransaction.hide(dFragment);
        if (fFragment != null)
            fTransaction.hide(fFragment);
        if (uFragment != null)
            fTransaction.hide(uFragment);

        fTransaction.show(cFragment);
        fTransaction.commit();
    }

    private void selectFoundTab() {
        toolbar.setTitle("数据分析");

        iv_tab_course_img.setImageResource(R.mipmap.icon_main_list);
        tv_course.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_download_img.setImageResource(R.mipmap.icon_main_map);
        tv_download.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_user_img.setImageResource(R.mipmap.icon_main_user);
        tv_user.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_found_img.setImageResource(R.mipmap.icon_main_found_0);
        tv_found.setTextColor(Color.rgb(7, 28, 183));
    }

    private void selectDownloadTab() {
        toolbar.setTitle("机器分布地图");

        iv_tab_course_img.setImageResource(R.mipmap.icon_main_list);
        tv_course.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_download_img.setImageResource(R.mipmap.icon_main_map_0);
        tv_download.setTextColor(Color.rgb(7, 28, 183));

        iv_tab_found_img.setImageResource(R.mipmap.icon_main_found);
        tv_found.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_user_img.setImageResource(R.mipmap.icon_main_user);
        tv_user.setTextColor(Color.rgb(0, 0, 0));
    }

    private void selectCourseTab() {
        toolbar.setTitle("监控列表");

        iv_tab_course_img.setImageResource(R.mipmap.icon_main_list_0);
        tv_course.setTextColor(Color.rgb(7, 28, 183));

        iv_tab_download_img.setImageResource(R.mipmap.icon_main_map);
        tv_download.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_found_img.setImageResource(R.mipmap.icon_main_found);
        tv_found.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_user_img.setImageResource(R.mipmap.icon_main_user);
        tv_user.setTextColor(Color.rgb(0, 0, 0));
    }

    private void selectUserTab() {
        toolbar.setTitle("个人中心");

        iv_tab_course_img.setImageResource(R.mipmap.icon_main_list);
        tv_course.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_download_img.setImageResource(R.mipmap.icon_main_map);
        tv_download.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_found_img.setImageResource(R.mipmap.icon_main_found);
        tv_found.setTextColor(Color.rgb(0, 0, 0));

        iv_tab_user_img.setImageResource(R.mipmap.icon_main_user_0);
        tv_user.setTextColor(Color.rgb(7, 28, 183));
    }
}
