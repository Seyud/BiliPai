package com.android.purebilibili.feature.video.screen

import org.junit.Assert.assertEquals
import org.junit.Test

class VideoDetailEntryVisualPolicyTest {

    @Test
    fun `entry frame should stay fully opaque and unblurred when transition is disabled`() {
        val frame = resolveVideoDetailEntryVisualFrame()

        assertEquals(1f, frame.contentAlpha)
        assertEquals(0f, frame.scrimAlpha)
        assertEquals(0f, frame.blurRadiusPx)
    }

    @Test
    fun `entry frame should stay fully opaque and unblurred in shared transition mode`() {
        val start = resolveVideoDetailEntryVisualFrame()
        val mid = resolveVideoDetailEntryVisualFrame()
        val end = resolveVideoDetailEntryVisualFrame()

        assertEquals(0f, start.blurRadiusPx)
        assertEquals(0f, mid.blurRadiusPx)
        assertEquals(0f, end.blurRadiusPx)
        assertEquals(0f, start.scrimAlpha)
        assertEquals(0f, mid.scrimAlpha)
        assertEquals(0f, end.scrimAlpha)
        assertEquals(1f, start.contentAlpha)
        assertEquals(1f, mid.contentAlpha)
        assertEquals(1f, end.contentAlpha)
    }
}
