package com.haomini.www.gamegate.base

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/5/3
 * @Contact 605626708@qq.com
 */
@GlideModule
class MyAppGlideModule: AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}