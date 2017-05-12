package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import JavaBeans.CommentBean;
import JavaBeans.PictureTrim;
import JavaBeans.VideoBean;

public class CommentItemAdapter extends BaseAdapter {
    private Context context;
    private List<CommentBean> list;
    private LayoutInflater inflater;

    public CommentItemAdapter(Context context, List<CommentBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CommentBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder vh = null;
        int type = getItemViewType(position);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.comment_item, parent, false);
            vh = new viewHolder();
            vh.iv_comment_icon = (ImageView) convertView.findViewById(R.id.iv_comment_icon);
            vh.tv_comment_username = (TextView) convertView.findViewById(R.id.tv_comment_username);
            vh.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
            vh.tv_comment_date = (TextView) convertView.findViewById(R.id.tv_comment_date);
            convertView.setTag(vh);

        } else {
            vh = (viewHolder) convertView.getTag();
        }

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.login);
        vh.iv_comment_icon.setImageBitmap(PictureTrim.toRoundCorner(bitmap, 400));
        vh.tv_comment_username.setText(getItem(position).getUserName());
        vh.tv_comment_content.setText(getItem(position).getContent());
        vh.tv_comment_date.setText(getItem(position).getCreateTime());

        return convertView;
    }

    //各个布局的控件资源
    class viewHolder {
        ImageView iv_comment_icon;
        TextView tv_comment_username;
        TextView tv_comment_date;
        TextView tv_comment_content;
    }
}
