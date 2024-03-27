package cn.xdf.ucan.troy.lib.permission.rationale;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ScrollView;

import cn.xdf.ucan.troy.lib.permission.utils.ContextUtil;

/**
 * @Description: 最大高度为屏幕一半的ScrollView
 * @author: jingzhenglun@xdf.cn
 * @date: 5/10/21
 */
public class MaxHeightScrollView extends ScrollView {

    private Context mContext;

    public MaxHeightScrollView(Context context) {
        this(context, null);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            Activity activity = ContextUtil.getActivity(mContext);
            if (activity != null) {
                Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                defaultDisplay.getMetrics(displayMetrics);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels / 2, MeasureSpec.AT_MOST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
