package com.mrtvrgn.gameanalysis.helper

import android.graphics.Color
import android.graphics.Matrix

fun getColorBySuccessRate(rate: Float) = when {
    rate > 12.5F && rate <= 25F -> Color.rgb(244, 109, 67)
    rate > 25F && rate <= 37.5F -> Color.rgb(253, 174, 97)
    rate > 37.5F && rate <= 50F -> Color.rgb(254, 224, 139)
    rate > 50F && rate <= 62.5F -> Color.rgb(230, 245, 152)
    rate > 62.5F && rate <= 75F -> Color.rgb(171, 221, 164)
    rate > 75F && rate <= 87.5F -> Color.rgb(102, 194, 165)
    rate > 87.5F && rate <= 100F -> Color.rgb(102, 194, 165)
    else -> Color.rgb(178, 24, 43) // 0~12.5
}

fun getScaleByDensityMatrix(density: Int, centerX: Float, centerY: Float) = when (density) {
    4 -> Matrix().apply { setScale(1F, 1F, centerX, centerY) }
    3 -> Matrix().apply { setScale(0.8F, 0.8F, centerX, centerY) }
    2 -> Matrix().apply { setScale(0.6F, 0.6F, centerX, centerY) }
    1 -> Matrix().apply { setScale(0.4F, 0.4F, centerX, centerY) }
    else -> Matrix().apply { setScale(0F, 0F, centerX, centerY) }
}