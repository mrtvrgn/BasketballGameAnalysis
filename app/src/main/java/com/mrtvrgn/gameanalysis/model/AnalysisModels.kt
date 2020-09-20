package com.mrtvrgn.gameanalysis.model

import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import com.mrtvrgn.gameanalysis.helper.HexTile
import com.mrtvrgn.gameanalysis.helper.getColorBySuccessRate
import com.mrtvrgn.gameanalysis.helper.getScaleByDensityMatrix

data class HexTileGraphics(val path: Path, val paint: Paint)

fun HexTile.toGraphics(singleTileArea: Float): HexTileGraphics {

    val densityRate = (shotCount / singleTileArea) * 0.25

    val scaleMatrix = when {
        densityRate >= 3F -> getScaleByDensityMatrix(4, centerX, centerY)
        densityRate >= 2F -> getScaleByDensityMatrix(3, centerX, centerY)
        densityRate >= 1F -> getScaleByDensityMatrix(2, centerX, centerY)
        densityRate > 0 -> getScaleByDensityMatrix(1, centerX, centerY)
        else -> getScaleByDensityMatrix(0, centerX, centerY)
    }

    return HexTileGraphics(Path().apply {
        moveTo(point0.x, point0.y)
        lineTo(point1.x, point1.y)
        lineTo(point2.x, point2.y)
        lineTo(point3.x, point3.y)
        lineTo(point4.x, point4.y)
        lineTo(point5.x, point5.y)
        transform(scaleMatrix)
    }, Paint().apply {
        color = getColorBySuccessRate(successRate)
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DARKEN)
    })
}