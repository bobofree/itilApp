package UserDefinedView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bobo.myapplication.R;

/**
 * Created by Bobo on 2016/8/6.
 */
public class CustomizeListView extends ListView implements AbsListView.OnScrollListener {

    private View header;// 顶部布局文件；
    private int headerHeight;// 顶部布局文件的高度；
    private int firstVisibleItem;// 当前第一个可见的item的位置；
    private int scrollState;// listview 当前滚动状态；
    private boolean isRemark;// 标记，当前是在listview最顶端摁下的；
    private int startY;// 摁下时的Y值；

    private int state;// 当前的状态；
    private final int NONE = 0;// 正常状态；
    private final int PULL = 1;// 提示下拉状态；
    private final int RELESE = 2;// 提示释放状态；
    private final int REFLASHING = 3;// 刷新状态；

    private IReflashListener iReflashListener;//刷新接口

    public CustomizeListView(Context context) {
        super(context);

        initView(context);
    }

    public CustomizeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomizeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        header = inflater.inflate(R.layout.listview_header, null, false);

        measureView(header);
        headerHeight = header.getMeasuredHeight();
        topPadding(-headerHeight);

        this.addHeaderView(header);
        this.setOnScrollListener(this);
    }

    /**
     * 通知父布局，占用的宽，高；
     *
     * @param view
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight,
                    MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    /**
     * 设置header 布局 上边距；
     *
     * @param topPadding
     */
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding,
                header.getPaddingRight(), header.getPaddingBottom());
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFLASHING;
                    // 加载最新数据；
                    reflashViewByState();
                    iReflashListener.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }


        return super.onTouchEvent(ev);
    }

    /**
     * 判断移动过程中的操作
     *
     * @param ev
     */
    private void onMove(MotionEvent ev) {
        if (!isRemark) return;
        int tempY = (int) ev.getY();
        int space = tempY - startY;
        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                topPadding(topPadding);
                if (space > headerHeight + 50 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reflashViewByState();
                }
                break;
            case RELESE:
                topPadding(topPadding);
                if (space <= headerHeight + 50) {
                    state = PULL;
                    reflashViewByState();
                } else if (space <= 0) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
    }

    /**
     * 根据状态改变界面显示
     */
    private void reflashViewByState() {
        TextView tv_refresh_tip = (TextView) header.findViewById(R.id.tv_refresh_tip);
        ImageView iv_refresh_arrow = (ImageView) header.findViewById(R.id.iv_refresh_arrow);
        ProgressBar pb_refresh = (ProgressBar) header.findViewById(R.id.pb_refresh);

        //设置动画
        RotateAnimation anim_1 = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim_1.setDuration(200);
        anim_1.setFillAfter(true);
        RotateAnimation anim_2 = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim_2.setDuration(200);
        anim_2.setFillAfter(true);

        switch (state) {
            case NONE:
                iv_refresh_arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL:
                iv_refresh_arrow.setVisibility(View.VISIBLE);
                pb_refresh.setVisibility(View.INVISIBLE);
                tv_refresh_tip.setText("下拉刷新！");
                iv_refresh_arrow.clearAnimation();
                iv_refresh_arrow.setAnimation(anim_2);
                break;
            case RELESE:
                iv_refresh_arrow.setVisibility(View.VISIBLE);
                pb_refresh.setVisibility(View.INVISIBLE);
                tv_refresh_tip.setText("释放刷新！");
                iv_refresh_arrow.clearAnimation();
                iv_refresh_arrow.setAnimation(anim_1);
                break;
            case REFLASHING:
                topPadding(50);
                iv_refresh_arrow.setVisibility(View.INVISIBLE);
                pb_refresh.setVisibility(View.VISIBLE);
                tv_refresh_tip.setText("正在刷新...");
                iv_refresh_arrow.clearAnimation();
                break;
        }

    }

    /**
     * 获取完数据；
     */
    public void reflashComplete() {
        state = NONE;
        isRemark = false;
        reflashViewByState();
    }

    public void setInterface(IReflashListener iReflashListener) {
        this.iReflashListener = iReflashListener;
    }

    /**
     * 刷新数据接口
     *
     * @author Administrator
     */
    public interface IReflashListener {
        public void onReflash();
    }
}
