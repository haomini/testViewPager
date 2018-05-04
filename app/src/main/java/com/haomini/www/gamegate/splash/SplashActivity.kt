package com.haomini.www.gamegate.splash

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.ImageViewTarget
import com.haomini.www.gamegate.R
import com.haomini.www.gamegate.base.GlideApp
import com.haomini.www.gamegate.utils.StatusBarUtils
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_spalsh.*


class SplashActivity : AppCompatActivity() {

    val map = mutableMapOf("http://brr.zhibocloud.cn/storage/2018/05/03/0612/1W3JxJLOpor50ljed0mGaXIsQokCjGMvVbBh4DpO/9446e2f678aeb9fd27eb0645cf0f6c5d.png" to false
            , "http://brr.zhibocloud.cn/storage/2018/05/02/1015/fLK7LjKk0tw2E5xBJnIY7EsKgvT9AwvgEfLGW5uL/b7f2c3b1e29086ddb2455346e63bf627.jpeg" to false
            , "http://brr.zhibocloud.cn/storage/2018/05/02/1015/dCDliFdwcjowoMNhgaByyQISp3xZz72Va6DDktZd/b7f2c3b1e29086ddb2455346e63bf627.jpeg" to false)

    private var adapter = object : ViewPager.SimpleOnPageChangeListener() {
        override fun onPageSelected(position: Int) {
            StatusBarUtils.setStatusTheme(window, map[map.keys.toList()[position]] ?: false)
        }
    }

    private var isFirst = true

    /**
     * 灰阶小于 < 142时认为应该使用白色状态栏文字
     */
    private fun isLightStatus(bitmap: Bitmap?): Boolean {
        val color = bitmap?.getPixel(bitmap.width * 2 / 3, 40)
        return (Color.red(color ?: 0) * 0.299 + Color.green(color ?: 0) * 0.587 + Color.blue(color
                ?: 0) * 0.114) < 142
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.activity_spalsh)

        //设置图片加载器
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                GlideApp.with(this@SplashActivity)
                        .asBitmap()
                        .load(path)
                        .transforms(CenterCrop(), RoundedCorners((4 * applicationContext.resources.displayMetrics.density).toInt()))
                        .into(object : ImageViewTarget<Bitmap>(imageView) {
                            override fun setResource(resource: Bitmap?) {
                                imageView?.setImageBitmap(resource)
                                if (resource != null) {
                                    map[path as String] = isLightStatus(resource)
                                    if (isFirst) {
                                        adapter.onPageSelected(0)
                                        isFirst = false
                                    }
                                }
                            }
                        })
            }

        })

        banner.mViewPager.apply {
            clipToPadding = false
            pageMargin = (8 * applicationContext.resources.displayMetrics.density).toInt()
            setPadding((24 * applicationContext.resources.displayMetrics.density).toInt(), 0,
                    (24 * applicationContext.resources.displayMetrics.density).toInt(), 0)
        }

        banner.setOnPageChangeListener(adapter)
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
