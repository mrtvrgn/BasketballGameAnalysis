package com.mrtvrgn.gameanalysis.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.mrtvrgn.gameanalysis.model.HexTileGraphics

class TileMapView(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private var tiles: List<HexTileGraphics>? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        tiles?.forEach {
            canvas.drawPath(it.path, it.paint)
        }
    }

    fun drawTiles(tiles: List<HexTileGraphics>) {
        this.tiles = tiles
        invalidate()
    }
}