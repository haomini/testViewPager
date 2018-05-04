package com.haomini.www.gamegate.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import java.lang.reflect.Field

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/5/4
 * @Contact 605626708@qq.com
 */
object StatusBarUtils {

    /**
     * 设置状态栏
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun setStatusTheme(window: Window?, isLight: Boolean) {
        if (isLight) {
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            MIUISetStatusBarLightMode(window, false)
            FlymeSetStatusBarLightMode(window, false)
        } else {
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            MIUISetStatusBarLightMode(window)
            FlymeSetStatusBarLightMode(window)
        }
    }

    /**
     * 获取状态栏高度
     */
    @SuppressLint("PrivateApi")
    fun getStatusBarHeight(context: Context): Int {
        val c: Class<*>?
        val obj: Any?
        val field: Field?
        val x: Int
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c!!.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field!!.get(obj).toString())
            return context.resources
                    .getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 魅族设置黑白状态栏文字
     */
    private fun FlymeSetStatusBarLightMode(window: Window?, dark: Boolean = true): Boolean {
        var result = false
        if (window != null) {
            try {
                val lp = window.attributes
                val darkFlag = WindowManager.LayoutParams::class.java
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                val meizuFlags = WindowManager.LayoutParams::class.java
                        .getDeclaredField("meizuFlags")
                darkFlag.isAccessible = true
                meizuFlags.isAccessible = true
                val bit = darkFlag.getInt(null)
                var value = meizuFlags.getInt(lp)
                value = if (dark) {
                    value or bit
                } else {
                    value and bit.inv()
                }
                meizuFlags.setInt(lp, value)
                window.attributes = lp
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }

    /**
     * 小米设置黑白状态栏文字
     */
    @SuppressLint("PrivateApi")
    private fun MIUISetStatusBarLightMode(window: Window?, dark: Boolean = true): Boolean {
        var result = false
        if (window != null) {
            val clazz = window::class.java
            try {
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                val darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag)//清除黑色字体
                }
                result = true
            } catch (e: Exception) {

            }

        }
        return result
    }
}