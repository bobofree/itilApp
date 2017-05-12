package fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.bobo.myapplication.R;

import java.util.ArrayList;

import UserDefinedView.CurveGraphicView;

public class FoundFragment extends Fragment {
    //private WebView wv_found;
    private CurveGraphicView curveGraphicView = null;
    private ArrayList<Double> yList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View foundView = inflater.inflate(R.layout.found_fragment, container, false);
        curveGraphicView = (CurveGraphicView) foundView.findViewById(R.id.curve_graphic_view);
        initData();
        return foundView;
    }

    private void initData() {
        yList = new ArrayList<Double>();
        yList.add(2.12);
        yList.add(4.05);
        yList.add(6.60);
        yList.add(3.08);
        yList.add(4.32);
        yList.add(2.0);
        yList.add(5.0);

        ArrayList<String> xRawDatas = new ArrayList<String>();
        xRawDatas.add("05-20");
        xRawDatas.add("05-21");
        xRawDatas.add("05-22");
        xRawDatas.add("05-23");
        xRawDatas.add("05-24");
        xRawDatas.add("05-25");
        xRawDatas.add("05-26");
        curveGraphicView.setData(yList, xRawDatas, 8, 1);
    }

}
