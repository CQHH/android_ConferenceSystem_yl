package com.hhxk.app.ui;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.em.baseframe.adapter.recyclerview.BaseQuickAdapter;
import com.em.baseframe.adapter.recyclerview.BaseViewHolder;
import com.em.baseframe.util.AppJsonUtil;
import com.em.baseframe.util.RetrofitUtils;
import com.em.baseframe.view.dialog.FormBotomDefaultDialogBuilder;
import com.hhxk.app.R;
import com.hhxk.app.adapter.TopicsFileAdapter;
import com.hhxk.app.base.BasePhotoAty;
import com.hhxk.app.interfaces.BaseHttp;
import com.hhxk.app.interfaces.Http;
import com.hhxk.app.pojo.TopicsFilePojo;
import com.hhxk.app.view.UploadFileDialog;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TResult;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  会议议题添加附件aty
 * @date   2019/03/06
 * @author enmaoFu
 */
public class FileListActivity extends BasePhotoAty implements BaseHttp {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyvlerview)
    RecyclerView recyclerView;

    /**
     * recyclerview布局管理器
     */
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * 适配器
     */
    private TopicsFileAdapter topicsFileAdapter;

    /**
     * 数据源
     */
    private List<TopicsFilePojo> topicsFilePojoList;

    /**
     * 弹出相机和相册选择框
     */
    private FormBotomDefaultDialogBuilder mDefaultDialogBuilder;

    /**
     * 上传文件弹出框
     */
    private UploadFileDialog hpUploadFileImgDialog;
    private UploadFileDialog hpUploadFileFileDialog;

    /**
     * 设置压缩的最大大小，上传照片的最大值
     */
    public static final int MAXSIZE = 100 * 1024;

    /**
     * 传过来的会议议题id
     */
    private String lssusId;

    /**
     * 传过来的权限id
     */
    private String roleId;

    /**
     * 图片路径
     */
    private String mImagePath;

    /**
     * 文件路径
     */
    private String mFilePath;

    /**
     * 选择文件的返回码
     */
    private static final int FILE_SELECT_CODE = 0;

    /**
     * 可上传的文件后缀
     */
    private List<String> fileSuffixList;

    private int po;

    private int poItem;

    private MMKV kv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topics_file;
    }

    @Override
    protected void initData() {

        initToolbar(toolbar,"添加会议材料");

        kv = MMKV.defaultMMKV();

        lssusId = getIntent().getStringExtra("lssusId");
        roleId = getIntent().getStringExtra("roleId");

        fileSuffixList = new ArrayList<>();
        fileSuffixList.add("doc");
        fileSuffixList.add("docx");
        fileSuffixList.add("ppt");
        fileSuffixList.add("pptx");
        fileSuffixList.add("xls");
        fileSuffixList.add("xlsx");
        fileSuffixList.add("pdf");
        fileSuffixList.add("mp4");

        //实例化布局管理器
        mLayoutManager = new GridLayoutManager(this,3);
        //实例化适配器
        topicsFileAdapter = new TopicsFileAdapter(R.layout.item_topics_file, new ArrayList<TopicsFilePojo>());
        //设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        //大小不受适配器影响
        recyclerView.setHasFixedSize(true);
        //设置空数据页面
        setEmptyView(topicsFileAdapter);
        //设置间隔样式
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.parseColor("#E8E8E8"))
                        .sizeResId(R.dimen.recyclerview_item_hr)
                        .build());
        //设置adapter
        recyclerView.setAdapter(topicsFileAdapter);

        topicsFileAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<TopicsFilePojo>() {
            @Override
            public void onItemClick(BaseQuickAdapter<TopicsFilePojo, ? extends BaseViewHolder> adapter, View view, int position) {
                poItem = position;
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Http.class).findFileRecord(String.valueOf(topicsFileAdapter.getItem(position).getLssusefile_id()),
                        topicsFileAdapter.getItem(position).getLssue_id(),Integer.parseInt(kv.decodeString("user_id"))),5);
            }
        });

        topicsFileAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener<TopicsFilePojo>() {
            @Override
            public void onItemChildClick(BaseQuickAdapter<TopicsFilePojo, ? extends BaseViewHolder> adapter, View view, final int position) {
                switch (view.getId()){
                    case R.id.delete:
                        po = position;
                        //点击弹出对话框，选择拍照或者系统相册
                        new AlertDialog.Builder(FileListActivity.this).setTitle("确定删除该文件?")//设置对话框标题
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        showLoadingDialog(null);
                                        doHttp(RetrofitUtils.createApi(Http.class).deleteLssueFile(topicsFileAdapter.getItem(position).getLssusefile_id()),4);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        doHttp(RetrofitUtils.createApi(Http.class).selectLssueFileById(Integer.parseInt(kv.decodeString("user_id")),lssusId),3);
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    protected void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Http.class).selectLssueFileById(Integer.parseInt(kv.decodeString("user_id")),lssusId),3);
    }

    @Override
    public void btnClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_public, menu);
        if(roleId.equals("1")){
            SpannableString spannableString = new SpannableString("添加议题材料");
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), 0);
            menu.getItem(0).setTitle(spannableString);
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (mDefaultDialogBuilder == null) {
                        mDefaultDialogBuilder = new FormBotomDefaultDialogBuilder(FileListActivity.this);
                        mDefaultDialogBuilder.setFBFirstBtnText("选择图片");
                        mDefaultDialogBuilder.setFBLastBtnText("选择文件");
                        //点击选择图片
                        mDefaultDialogBuilder.setFBFirstBtnClick(new FormBotomDefaultDialogBuilder.DialogBtnCallBack() {
                            @Override
                            public void dialogBtnOnClick() {
                                selectFileUpload("image");
                            }
                        });
                        //点击选择文件
                        mDefaultDialogBuilder.setFBLastBtnClick(new FormBotomDefaultDialogBuilder.DialogBtnCallBack() {
                            @Override
                            public void dialogBtnOnClick() {
                                selectFileUpload("file");
                            }
                        });
                    }
                    mDefaultDialogBuilder.show();
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void selectFileUpload(String type){
        switch (type){
            case "image":
                hpUploadFileImgDialog = new UploadFileDialog(FileListActivity.this);
                hpUploadFileImgDialog.show();
                hpUploadFileImgDialog.setDialogAnimation();
                hpUploadFileImgDialog.setTitle("添加附件");
                hpUploadFileImgDialog.setHint("请输入附件标题");
                hpUploadFileImgDialog.setSelectBtn("选择附件");
                hpUploadFileImgDialog.setSelectOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        configCompress();
                        getTakePhoto().onPickFromGalleryWithCrop(getImageUri(), getCropOptions());
                    }
                });
                hpUploadFileImgDialog.setBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = hpUploadFileImgDialog.getInput();
                        String filePath = hpUploadFileImgDialog.getFilePath();
                        if(title.length() == 0){
                            showErrorToast("请输入附件标题");
                        }else if(filePath.length() == 0){
                            showErrorToast("请选择附件");
                        }else{
                            File file = new File(
                                    mImagePath.substring(0,mImagePath.lastIndexOf("/") + 1)
                                    ,mImagePath.substring(mImagePath.lastIndexOf("/") + 1,mImagePath.length()));
                            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("fileupload",
                                    mImagePath.substring(mImagePath.lastIndexOf("/") + 1,mImagePath.length()), requestFile);
                            showLoadingDialog(null);
                            doHttp(RetrofitUtils.createApi(Http.class).upload(body,lssusId,hpUploadFileImgDialog.getInput()),1);
                        }
                    }
                });
                break;
            case "file":
                hpUploadFileFileDialog = new UploadFileDialog(FileListActivity.this);
                hpUploadFileFileDialog.show();
                hpUploadFileFileDialog.setDialogAnimation();
                hpUploadFileFileDialog.setTitle("添加附件");
                hpUploadFileFileDialog.setHint("请输入标题");
                hpUploadFileFileDialog.setSelectBtn("选择文件");
                hpUploadFileFileDialog.setSelectOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        try {
                            startActivityForResult( Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
                        } catch (android.content.ActivityNotFoundException ex) {
                            showErrorToast("该系统没有文件选择器");
                        }
                    }
                });
                hpUploadFileFileDialog.setBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = hpUploadFileFileDialog.getInput();
                        String filePath = hpUploadFileFileDialog.getFilePath();
                        if(title.length() == 0){
                            showErrorToast("请输入附件标题");
                        }else if(filePath.length() == 0){
                            showErrorToast("请选择附件");
                        }else{
                            String suffixStr =  mFilePath.substring(mFilePath.lastIndexOf(".") + 1,mFilePath.length());
                            Logger.v(suffixStr + "---------------------------------");
                            if(fileSuffixList.contains(suffixStr) == true){
                                File file = new File(
                                        mFilePath.substring(0,mFilePath.lastIndexOf("/") + 1)
                                        ,mFilePath.substring(mFilePath.lastIndexOf("/") + 1,mFilePath.length()));
                                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                MultipartBody.Part body = MultipartBody.Part.createFormData("fileupload",
                                        mFilePath.substring(mFilePath.lastIndexOf("/") + 1,mFilePath.length()), requestFile);
                                showLoadingDialog(null);
                                doHttp(RetrofitUtils.createApi(Http.class).upload(body,lssusId,hpUploadFileFileDialog.getInput()),2);
                            }else{
                                showErrorToast("暂不支持上传" + suffixStr + "类型的文件");
                            }
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void takeSuccess(final TResult result) {
        FileListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Logger.v("图片路径" + "file://" + result.getImage().getCompressPath());
                mImagePath = result.getImage().getCompressPath();
                hpUploadFileImgDialog.setFilePath("");
                hpUploadFileImgDialog.setFilePath(result.getImage().getCompressPath());
            }
        });
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = null;
                    try {
                        path = getPath(FileListActivity.this, uri);
                        mFilePath = path;
                        hpUploadFileFileDialog.setFilePath("");
                        hpUploadFileFileDialog.setFilePath(mFilePath);
                        Logger.v("选中的路径为" + path);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                TopicsFilePojo topicsFileImgPojo = new TopicsFilePojo();
                topicsFileImgPojo.setIsSee("0");
                topicsFileImgPojo.setLssue_id(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"lssue_id"));
                topicsFileImgPojo.setLssusefile_name(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"lssusefile_name"));
                topicsFileImgPojo.setLssusefile_path(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"lssusefile_path"));
                topicsFileImgPojo.setLssusefile_date(AppJsonUtil.getLong(AppJsonUtil.getString(result, "content"),"lssusefile_date"));
                topicsFileImgPojo.setLssusefile_id(AppJsonUtil.getInt(AppJsonUtil.getString(result, "content"),"lssusefile_id"));
                topicsFileImgPojo.setLssusefile_status(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"lssusefile_status"));
                topicsFileImgPojo.setType(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"type"));
                topicsFileAdapter.addData(topicsFileImgPojo);
                showToast("添加图片成功");
                hpUploadFileImgDialog.dismiss();
                break;
            case 2:
                TopicsFilePojo topicsFileFilePojo = new TopicsFilePojo();
                topicsFileFilePojo.setIsSee("0");
                topicsFileFilePojo.setLssue_id(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"lssue_id"));
                topicsFileFilePojo.setLssusefile_name(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"lssusefile_name"));
                topicsFileFilePojo.setLssusefile_path(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"lssusefile_path"));
                topicsFileFilePojo.setLssusefile_date(AppJsonUtil.getLong(AppJsonUtil.getString(result, "content"),"lssusefile_date"));
                topicsFileFilePojo.setLssusefile_id(AppJsonUtil.getInt(AppJsonUtil.getString(result, "content"),"lssusefile_id"));
                topicsFileFilePojo.setLssusefile_status(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"lssusefile_status"));
                topicsFileFilePojo.setType(AppJsonUtil.getString(AppJsonUtil.getString(result, "content"),"type"));
                topicsFileAdapter.addData(topicsFileFilePojo);
                hpUploadFileFileDialog.dismiss();
                showToast("添加文件成功");
                hpUploadFileImgDialog.dismiss();
                break;
            case 3:
                topicsFilePojoList = AppJsonUtil.getArrayList(result,TopicsFilePojo.class);
                topicsFileAdapter.setNewData(topicsFilePojoList);
                break;
            case 4:
                topicsFileAdapter.remove(po);
                break;
            case 5:
                Bundle bundle = new Bundle();
                Bundle bundle1 = new Bundle();
                String type = topicsFileAdapter.getItem(poItem).getType();
                if(type.equals("doc") || type.equals("docx") || type.equals("ppt") || type.equals("pptx") || type.equals("xls") || type.equals("xlsx") || type.equals("pdf")){
                    openWps(BASEHTTP + topicsFileAdapter.getItem(poItem).getLssusefile_path());
                }else if(type.equals("jpg") || type.equals("png")){
                    List<String> imagePath = new ArrayList<>();
                    imagePath.add(BASEHTTP + topicsFileAdapter.getItem(poItem).getLssusefile_path());
                    bundle.putStringArrayList("imagePath", (ArrayList<String>) imagePath);
                    bundle.putInt("mIndex",poItem);
                    bundle.putString("title",topicsFileAdapter.getItem(poItem).getLssusefile_name());
                    startActivity(ZoomableActivity.class,bundle);
                }else if(type.equals("mp4")){
                    bundle1.putString("url",BASEHTTP + topicsFileAdapter.getItem(poItem).getLssusefile_path());
                    bundle1.putString("title",topicsFileAdapter.getItem(poItem).getLssusefile_name());
                    startActivity(VideoActivity.class,bundle1);
                }else{
                    showErrorToast("暂不支持打开此类型文件");
                }
                break;
        }
    }

    /**
     * 配置裁剪（方形，可拉动）
     * @return
     */
    private CropOptions getCropOptions() {
        int height = 800;
        int width = 800;
        boolean withWonCrop = false;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    /**
     * 配置压缩
     */
    private void configCompress() {
        //压缩
        LubanOptions option = new LubanOptions.Builder()
                .setMaxSize(MAXSIZE)//设置压缩的最大大小，上传照片的最大值
                .create();
        //进行压缩配置
        CompressConfig config = CompressConfig.ofLuban(option);
        config.enableReserveRaw(true);
        //启用图片压缩,设置上面的压缩配置，不显示进度对话框
        getTakePhoto().onEnableCompress(config, false);
    }

    /**
     * 转换路径
     * @param context
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it  Or Log it.
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 检查是否已安装WPS
     * @return
     */
    private boolean checkWps(){
        Intent intent = getPackageManager().getLaunchIntentForPackage("cn.wps.moffice_eng");//WPS个人版的包名
        if (intent == null) {
            return false;
        } else {
            return true;
        }
    }

    public void openWps(String str){
        if(checkWps() == false){
            showErrorToast("请安装wps手机APP");
        }else{
            Intent intent = getPackageManager().getLaunchIntentForPackage( "cn.wps.moffice_eng");
            Bundle bundle = new Bundle();
            bundle.putString("OpenMode", "ReadOnly");// 只读模式
            bundle.putBoolean("SendSaveBroad", true);// 关闭保存时是否发送广播
            bundle.putString("ThirdPackage", getPackageName()); // 第三方应用的包名，用于对改应用合法性的验证
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setData(Uri.parse(str));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 设置RecyclerView空数据页面
     * @param quickAdapter
     */
    public void setEmptyView(BaseQuickAdapter quickAdapter) {
        View view = this.getLayoutInflater().inflate(R.layout.fragment_assets_null, null, false);
        quickAdapter.setEmptyView(view);
    }

}
