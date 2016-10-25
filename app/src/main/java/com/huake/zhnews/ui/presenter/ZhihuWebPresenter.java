package com.huake.zhnews.ui.presenter;

/*
 * @创建者     兰昱
 * @创建时间  2016/10/24 19:39
 * @描述	      
 */

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huake.zhnews.bean.zhihu.News;
import com.huake.zhnews.ui.base.BasePresenter;
import com.huake.zhnews.ui.view.IZhihuWebView;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ZhihuWebPresenter extends BasePresenter<IZhihuWebView> {

    private Context mContext;
    private WebView mWebView;
    private ImageView mWebImage;

    public ZhihuWebPresenter(Context context) {
        mContext = context;
    }
    //得到数据
    public void getDetailNews(String id){
        zhihuApi.getDetailNews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<News>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "erro", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(News news) {
                        setWebViews(news);
                    }
                });
    }

    private void setWebViews(News news) {
        //值得注意我们一般用webview载入数据，这个真搞笑，直接把数据转换下拉，自己载入，服啊
        TextView imgSource = getView().getImgSource();
        TextView imgTitle = getView().getImgTitle();
        mWebImage = getView().getWebImage();
        mWebView = getView().getWebView();

        imgSource.setText(news.getImage_source());
        imgTitle.setText(news.getTitle());
        Glide.with(mContext).load(news.getImage()).centerCrop().into(mWebImage);

        //到重头戏webview了，比较麻烦，因为要设置一些参数
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //反斜杠是为了保证不冲突，后面发蓝是请求头的内容
        String head = "<head>\n" +
                "\t<link rel=\"stylesheet\" href=\""+news.getCss()[0]+"\"/>\n" +
                "</head>";

        //这种标语全不要，替换为空
        String img = "<div class=\"headline\">";
        String html =head + news.getBody().replace(img," ");
        Logger.e(html);
        //载入数据吧
//        mWebView.loadData(html,"text/html","utf-8");  bug,这个方法会出现乱码！！
        mWebView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);

    }

    public void destoryImg(){
        if (mWebView != null) {
            Glide.clear(mWebView);
        }
    }
}
