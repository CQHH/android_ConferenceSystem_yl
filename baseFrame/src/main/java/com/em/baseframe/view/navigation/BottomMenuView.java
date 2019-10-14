package com.em.baseframe.view.navigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.em.baseframe.R;
import com.em.baseframe.base.BaseLazyFragment;
import com.em.baseframe.util.DensityUtils;
import com.em.baseframe.view.viewpager.CustomViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @title  底部导航切换布局
 * @date   2017/06/17
 * @author enmaoFu
 */
public class BottomMenuView extends LinearLayout {

    private CustomViewPager mViewPager;
    private RadioGroup mGroup;
    private List<RadioButton> mRadioButtons;
    private List<Integer> ids;
    private Context mContext;
    private List<BaseLazyFragment> mFgts;
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private int checkid;
    private CheckChangeListener mListener;

    public void setListener(CheckChangeListener listener) {
        mListener = listener;
    }

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF)
                newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public BottomMenuView(Context context) {
        this(context, null);
    }

    public BottomMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.frame_bottom_menu, this, true);
        mContext = context;
        initData();

    }

    public CustomViewPager getViewPager() {
        return mViewPager;
    }

    public RadioGroup getGroup() {
        return mGroup;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public List<BaseLazyFragment> getFgts() {
        return mFgts;
    }

    private void initData() {

        mViewPager = (CustomViewPager) findViewById(R.id.view_pager);
        mGroup = (RadioGroup) findViewById(R.id.rg_mian);
        mRadioButtons = new ArrayList<>();
        ids = new ArrayList<>();

        mGroup.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mListener != null) {
                    mListener.onCheckChanged(mViewPager, group, getCheckPostionById(checkedId));
                } else {
                    mViewPager.setCurrentItem(getCheckPostionById(checkedId));
                }

            }
        });


    }


    private int getCheckPostionById(int checkId) {

        for (int i = 0; i < ids.size(); i++) {
            if (checkId == ids.get(i)) {
                this.checkid = ids.get(i);
                return i;
            }
        }
        return 0;
    }


    public void init(List<Drawable> drawables, int color, List<BaseLazyFragment> fgts, List<String> menus, FragmentManager fm) {

        for (int i = 0; i < drawables.size(); i++) {
            RadioButton button = getRadioButton(drawables, color, menus, i);
            if (mRadioButtons.size() == 0) {
                button.setChecked(true);
            }
            int id = generateViewId();
            button.setId(id);
            ids.add(id);
            mRadioButtons.add(button);
            mGroup.addView(button);
        }


        mViewPager.setOffscreenPageLimit(mRadioButtons.size());
        this.mFgts = fgts;

        MainFragmentAdapter fragmentAdapter = new MainFragmentAdapter(fm);
        mViewPager.setAdapter(fragmentAdapter);

    }

    @NonNull
    private RadioButton getRadioButton(List<Drawable> drawables, int color, List<String> menus, int i) {
        RadioButton button = new RadioButton(mContext);
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        button.setTextColor(mContext.getResources().getColorStateList(color));
        button.setText(menus.get(i));
        button.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        Bitmap a = null;
        button.setButtonDrawable(new BitmapDrawable(a));
        button.setCompoundDrawablePadding(DensityUtils.dp2px(mContext,2));
        button.setCompoundDrawablesWithIntrinsicBounds(drawables.get(i), null, null, null);
        button.setGravity(Gravity.CENTER);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,0 );
        params.weight = 1;
        button.setLayoutParams(params);
        return button;
    }


    public void setRadioButtonDrawable(Drawable drawable, int position) {

        RadioButton button = getRadioButtons().get(position);
        button.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

    }

    public int getCheckid() {
        return checkid;
    }

    public void setCheckid(int checkid) {
        this.checkid = checkid;
    }

    public List<RadioButton> getRadioButtons() {
        return mRadioButtons;
    }

    public void setRadioButtons(List<RadioButton> radioButtons) {
        mRadioButtons = radioButtons;
    }

    class MainFragmentAdapter extends FragmentPagerAdapter {
        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFgts.get(position);
        }

        @Override
        public int getCount() {
            return mFgts.size();
        }
    }

    public interface CheckChangeListener {
        void onCheckChanged(ViewPager pager, RadioGroup group, int checkedPosition);
    }
}
