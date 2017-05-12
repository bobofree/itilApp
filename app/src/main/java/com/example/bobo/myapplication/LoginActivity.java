package com.example.bobo.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import DataBaseHelper.DBHelper;
import JavaBeans.JSONReturn;
import JavaBeans.PictureTrim;
import JavaBeans.URLStore;

public class LoginActivity extends Activity implements View.OnClickListener {

    private ImageView iv_login = null;
    private EditText et_username = null;
    private EditText et_password = null;
    private Button btn_login = null;
    private TextView tv_forget_password = null;
    private TextView tv_user_register = null;
    private CheckBox cb_remember_password = null;
    private ProgressBar pb_login_progerss = null;
    private DBHelper dbHelper = null;
    private TextView login_error_info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initData();
        checkExist();
    }

    /**
     * 检查用户是否在用以及是否保存密码
     */
    private void checkExist() {
        Cursor cursor = dbHelper.getAll(null, null);
        //检查是否记住密码
        if (null != cursor && cursor.moveToFirst()) {
            String remember = cursor.getString(cursor.getColumnIndex("remember"));
            //Toast.makeText(getApplicationContext(),"remember:"+remember,Toast.LENGTH_SHORT).show();
            if ("yes".equals(remember)) {
                cb_remember_password.setChecked(true);
                et_username.setText(cursor.getString(1));
                et_password.setText(cursor.getString(2));
            }
        }

        //检查是否从mainactivity来
        Intent i = getIntent();
        boolean modify = i.getBooleanExtra("modify", false);
        if (modify) {
            return;
        }

        //检查用户是否处于活跃状态
        if (null != cursor && cursor.moveToFirst()) {
            String state = cursor.getString(cursor.getColumnIndex("state"));
            if (state.equals("using")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //关闭当前activity并返回
                finish();
                return;
            }
        }
    }

    private void initData() {
        iv_login = (ImageView) findViewById(R.id.iv_login);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        //tv_forget_password = (TextView) findViewById(R.id.tv_forget_password);
        //tv_user_register = (TextView) findViewById(R.id.tv_register);
        cb_remember_password = (CheckBox) findViewById(R.id.cb_remember_password);
        pb_login_progerss = (ProgressBar) findViewById(R.id.pb_login_progress);
        login_error_info = (TextView) findViewById(R.id.login_error_info);

        btn_login.setOnClickListener(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.login);
        iv_login.setImageBitmap(PictureTrim.toRoundCorner(bitmap, 400));

        dbHelper = new DBHelper(getApplicationContext());
        //dbHelper.insert("admin", "123", "admin", "18814093554", "used","yes");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        final String username = et_username.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        if (username.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        pb_login_progerss.setVisibility(View.VISIBLE);
        loginByPost(username, password);
    }

    private void loginByPost(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* //测试代码
                handler.sendEmptyMessage(0x1);
                if (true)
                    return;*/

                try {
                    String result = "";
                    String dataStr = "username=" + URLEncoder.encode(username, "utf-8") + "&password=" + URLEncoder.encode(password, "utf-8");
                    System.out.println("post url:" + dataStr);
                    byte[] data = dataStr.getBytes();

                    URL url = new URL(URLStore.LOGIN_URL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    System.out.println("connecting ok!");
                    connection.setRequestMethod("POST");        //设置以Post方式提交数据
                    connection.setConnectTimeout(5000);        //设置连接超时时间
                    connection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
                    connection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
                    connection.setUseCaches(false);               //使用Post方式不能使用缓存
                    //设置请求体的类型是文本类型
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //设置请求体的长度
                    connection.setRequestProperty("Content-Length", String.valueOf(data.length));
                    connection.connect();
                    //获得输出流，向服务器写入数据
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(data);
                    outputStream.flush();

                    int response = connection.getResponseCode();            //获得服务器的响应码
                    System.out.println(response);
                    if (response == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
                        byte[] datas = new byte[1024];
                        int len = 0;
                        try {
                            while ((len = inputStream.read(datas)) != -1) {
                                byteout.write(datas, 0, len);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        result = new String(byteout.toByteArray());

                        Bundle bundle = new Bundle();
                        bundle.putString("result", result);
                        Message ms = new Message();
                        ms.what = 0x1;
                        ms.setData(bundle);
                        handler.sendMessage(ms);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                    if (connection != null)
                        connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
            }
        }).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                pb_login_progerss.setVisibility(View.GONE);
                Bundle bundle = msg.getData();
                String result = bundle.getString("result");
                Log.d("login", result);
                JSONObject jsonObject = null;
                int code = 404;
                try {
                    jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("errorcode");
                    if (202 != code) {
                        login_error_info.setText(jsonObject.getString("message"));
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    login_error_info.setText("数据格式错误：" + result);
                    return;
                }

                Toast.makeText(getApplicationContext(), "欢迎！", Toast.LENGTH_SHORT).show();

                boolean remember = cb_remember_password.isChecked();
                Cursor cursor = dbHelper.getAll("", "");
                if (null == cursor || !cursor.moveToFirst()) {
                    dbHelper.insert(et_username.getText().toString().trim(),
                            et_password.getText().toString().trim(),
                            "admin", "", "using", remember ? "yes" : "no");
                } else {
                    dbHelper.update("1", "name", et_username.getText().toString().trim());
                    dbHelper.update("1", "password", et_password.getText().toString().trim());
                    dbHelper.update("1", "state", "using".trim());
                    dbHelper.update("1", "remember", remember ? "yes" : "no");
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //关闭当前activity并返回
                finish();
            } else if (msg.what == 2) {
                //Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                pb_login_progerss.setVisibility(View.GONE);

                //测试代码
                Toast.makeText(getApplicationContext(), "网络错误！", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
