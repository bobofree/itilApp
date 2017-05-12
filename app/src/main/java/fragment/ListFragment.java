package fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.bobo.myapplication.LoginActivity;
import com.example.bobo.myapplication.MainActivity;
import com.example.bobo.myapplication.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import UserDefinedView.CustomizeListView;
import JavaBeans.URLStore;
import JavaBeans.VideoBean;

public class ListFragment extends Fragment implements CustomizeListView.IReflashListener {

    private CustomizeListView listView;
    private List<VideoBean> list = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View courseView = inflater.inflate(R.layout.list_fragment, container, false);

        initListView(courseView);

        //initData();

        return courseView;
    }

    private void initListView(View courseView) {
        listView = (CustomizeListView) courseView.findViewById(R.id.lv_course_main);
        listView.setInterface(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("position:",position+"");
//                Log.d("id:",id+"");
                if (list != null && list.size() >= id) {
                    VideoBean vbean = list.get((int) id - 1);
//                    Log.d("bean name  position", vbean.getName() + "===" + id);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("VideoBean", vbean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if(null==list){
                    Toast.makeText(MainActivity.mainContext, "获取数据失败。。。", Toast.LENGTH_LONG).show();
                    return;
                }
                /*CourseItemAdapter adapter = new CourseItemAdapter(getActivity(), list);
                listView.setAdapter(adapter);*/
            } else if (msg.what == 2) {
                Toast.makeText(MainActivity.mainContext, "网络连接失败。。。", Toast.LENGTH_LONG).show();
            }
        }
    };

    /*private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(URLStore.VIDEO_INFO_URL);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setDoInput(true);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        list = getListData(conn.getInputStream());
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                } finally {
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }*/


    private List<VideoBean> getListData(InputStream inputStream) {
        List<VideoBean> list_temp = new ArrayList<>();

        String jsonStr = "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = inputStream.read(buffer, 0, buffer.length)) > 0) {
                baos.write(buffer, 0, length);
                baos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] jsonStrByte = baos.toByteArray();
        try {
            jsonStr = new String(jsonStrByte, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jArray = new JSONArray(jsonStr);
            VideoBean tempBean = null;
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObj = jArray.getJSONObject(i);
                tempBean = new VideoBean();
                tempBean.setId(jsonObj.getInt("id"));
                tempBean.setName(jsonObj.getString("name"));
                tempBean.setSize(jsonObj.getString("size"));
                tempBean.setViewers(jsonObj.getInt("viewers"));
                tempBean.setDatetime(jsonObj.getString("datetime"));
                tempBean.setImg_name(jsonObj.getString("img_name"));
                list_temp.add(tempBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list_temp;
    }

    @Override
    public void onReflash() {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取最新数据
                //setReflashData();
                //通知界面显示
                //showList(apk_list);
                //通知listview 刷新数据完毕；
                listView.reflashComplete();
            }
        }, 2000);

    }
}
