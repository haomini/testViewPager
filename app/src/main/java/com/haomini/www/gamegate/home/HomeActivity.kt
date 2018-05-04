package com.haomini.www.gamegate.home

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.haomini.www.gamegate.R
import com.haomini.www.gamegate.base.GlideApp
import com.haomini.www.gamegate.utils.StatusBarUtils
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val map = mutableMapOf("http://brr.zhibocloud.cn/storage/2018/05/03/0612/1W3JxJLOpor50ljed0mGaXIsQokCjGMvVbBh4DpO/9446e2f678aeb9fd27eb0645cf0f6c5d.png" to false
            , "http://brr.zhibocloud.cn/storage/2018/05/02/1015/fLK7LjKk0tw2E5xBJnIY7EsKgvT9AwvgEfLGW5uL/b7f2c3b1e29086ddb2455346e63bf627.jpeg" to false
            , "http://brr.zhibocloud.cn/storage/2018/05/02/1015/dCDliFdwcjowoMNhgaByyQISp3xZz72Va6DDktZd/b7f2c3b1e29086ddb2455346e63bf627.jpeg" to false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.TRANSPARENT
            StatusBarUtils.setStatusTheme(window, true, false)
        }
        setContentView(R.layout.activity_home)

        //设置图片加载器
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                GlideApp.with(this@HomeActivity)
                        .asBitmap()
                        .load(path)
                        .transforms(CenterCrop(), RoundedCorners((4 * applicationContext.resources.displayMetrics.density).toInt()))
                        .into(imageView!!)
            }

        })

        banner.mViewPager.apply {
            clipToPadding = false
            pageMargin = (8 * applicationContext.resources.displayMetrics.density).toInt()
            setPadding((24 * applicationContext.resources.displayMetrics.density).toInt(), 0,
                    (24 * applicationContext.resources.displayMetrics.density).toInt(), 0)
        }

        /**
         * 与window最顶端有一段间隙, 属于viewpager - pageMargin换算后的图片平移系数,
         */
        banner.setPageTransformer(false) { page, position -> page.scaleY = 1 - 0.1F * Math.abs(position) }
        //设置图片集合
        banner.setImages(map.keys.toList())
        //banner设置方法全部调用完毕时最后调用
        banner.start()
    }
}
