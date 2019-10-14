package com.hhxk.app.ui;

import android.support.v7.widget.Toolbar;

import com.hhxk.app.R;
import com.hhxk.app.base.BaseAty;
import com.orhanobut.logger.Logger;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import butterknife.BindView;

/**
 * @title  视频播放
 * @date   2019/06/14
 * @author enmaoFu
 */
public class VideoActivity extends BaseAty {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nice_video_player)
    NiceVideoPlayer niceVideoPlayer;

    private String title;
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    protected void initData() {
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        url.replace("\\","/");
        initToolbar(mToolbar,title);
        //String videoUrl = "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4";
        //String videoUrl1 = "http://192.168.0.105//meetingfile//2019-06//8//8//VID_20190614_090659.mp4";
        Logger.v("----" + url.replace("\\","/"));
        niceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // or NiceVideoPlayer.TYPE_NATIVE
        niceVideoPlayer.setUp(url.replace("\\","/"), null);
        TxVideoPlayerController controller = new TxVideoPlayerController(this);
        controller.setTitle("正在播放" + title);
        //controller.setImage(R.drawable.bg);
        niceVideoPlayer.setController(controller);
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
}
