package com.haomini.www.gamegate.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import com.youth.banner.Banner

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/5/4
 * @Contact 605626708@qq.com
 */
class CustomBanner : Banner {

    val mViewPager: ViewPager = findViewById(com.youth.banner.R.id.bannerViewPager)

    constructor (context: Context) : this(context, null)

    constructor (context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor (context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)
}