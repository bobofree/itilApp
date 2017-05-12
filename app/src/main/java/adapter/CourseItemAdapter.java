package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bobo.myapplication.R;

import java.util.List;

import AcceptVideo.ImageLoader;
import JavaBeans.VideoBean;

public class CourseItemAdapter extends BaseAdapter {
    private Context context;
    private List<VideoBean> list;
    private LayoutInflater inflater;

    public CourseItemAdapter(Context context, List<VideoBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public VideoBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder_2 vh_2 = null;

        if (convertView == null) {
                convertView = inflater.inflate(R.layout.course_item, parent, false);
                vh_2 = new viewHolder_2();
                vh_2.iv_course_item_img = (ImageView) convertView.findViewById(R.id.iv_course_item_img);
                vh_2.tv_course_item_name = (TextView) convertView.findViewById(R.id.tv_course_item_name);
                vh_2.tv_course_item_time = (TextView) convertView.findViewById(R.id.tv_course_item_time);
                vh_2.tv_viewers = (TextView) convertView.findViewById(R.id.tv_viewers);
                convertView.setTag(vh_2);
        } else {
                vh_2 = (viewHolder_2) convertView.getTag();
        }

            String name = getItem(position).getName();
            vh_2.tv_course_item_name.setText(name.substring(0, name.lastIndexOf(".")));
            vh_2.tv_course_item_time.setText(getItem(position).getDatetime());
            vh_2.tv_viewers.setText(getItem(position).getViewers() + "");

            ImageLoader loader = new ImageLoader();
            vh_2.iv_course_item_img.setTag(getItem(position).getId() + "");
            loader.setImageFromId(vh_2.iv_course_item_img, getItem(position).getId() + "");

        return convertView;
    }

    class viewHolder_2 {
        ImageView iv_course_item_img;
        TextView tv_course_item_name;
        TextView tv_viewers;
        TextView tv_course_item_time;
    }
}
