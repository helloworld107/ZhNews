package com.huake.zhnews.ui.activity;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/24 20:58
 * @描述	      
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huake.zhnews.R;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.base.MVPBaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

public class PictureActivity extends MVPBaseActivity {

    public static final String IMG_URL = "img_url";
    public static final String IMG_DESC = "img_desc";
    public static final String TRANSIT_PIC = "picture";
    @Bind(R.id.iv_meizhi_pic)
    ImageView mIvMeizhiPic;
    @Bind(R.id.save_img)
    FloatingActionButton mSaveImg;
    private String mImgurl;
    private String mImgdec;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int provideContentViewId() {
        return R.layout.activity_pic;
    }

    public static Intent newIntent(Context context,String url,String desc){
        Intent intent=new Intent(context,PictureActivity.class);
        intent.putExtra(IMG_URL,url);
        intent.putExtra(IMG_DESC,desc);
        return intent;
    }

    @Override
    public void initData() {
        super.initData();
        parseIntent();
        //设置共享元素
        ViewCompat.setTransitionName(mIvMeizhiPic, TRANSIT_PIC);
        showImage();
    }

    private void showImage() {
        Glide.with(this).load(mImgurl).centerCrop().into(mIvMeizhiPic);
    }

    private void parseIntent() {
        mImgurl = getIntent().getStringExtra(IMG_URL);
        mImgdec = getIntent().getStringExtra(IMG_DESC);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(mIvMeizhiPic);
    }
    @OnClick(R.id.save_img)
    public void savaImg(){
        //先建立图片的缓存模板
        mIvMeizhiPic.buildDrawingCache();
        Bitmap bitmap = mIvMeizhiPic.getDrawingCache();
        //由于图片太大，压缩成一个流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bytes = stream.toByteArray();
        //存取图片-->确定路径
        File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/LY");
        if (!dir.exists()) {
            dir.mkdir();
        }
        //按流的形式读取文件
        File file=new File(dir,mImgdec.substring(0,10)+".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bytes,0,bytes.length);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
        //通过相册栏更新，感觉不用你管也会自己更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        PictureActivity.this.sendBroadcast(intent);


    }
}
