package com.android.purebilibili.feature.settings

import kotlin.test.Test
import kotlin.test.assertEquals

class MobileSettingsRootSectionOrderTest {

    @Test
    fun shouldUseSceneBasedOrderForSettingsHome() {
        assertEquals(
            resolveSettingsRootCategoryOrder(),
            resolveTabletSettingsRootCategoryOrder()
        )
    }

    @Test
    fun rootSections_shouldUseSceneTitles() {
        assertEquals(
            listOf(
                "界面与首页",
                "动态与推荐",
                "播放与互动",
                "导航与手势",
                "数据与隐私",
                "扩展与关于"
            ),
            resolveSettingsRootCategoryOrder().map { it.title }
        )
    }
}
