package com.mrtvrgn.gameanalysis.helper

import android.graphics.PointF
import kotlin.math.sqrt

class HexTile(
    side: Float,
    val centerX: Float,
    val centerY: Float,
    val col: Int,
    val row: Int
) {
    private val height: Float = sqrt(3F) * side
    private val width: Float = 2 * side
    private val singleUnitHorizontal = side / 2
    private val singleUnitVertical = height / 2

    val point0 = PointF(centerX - singleUnitHorizontal, centerY - singleUnitVertical)
    val point1 = PointF(centerX + singleUnitHorizontal, centerY - singleUnitVertical)
    val point2 = PointF(centerX + side, centerY)
    val point3 = PointF(centerX + singleUnitHorizontal, centerY + singleUnitVertical)
    val point4 = PointF(centerX - singleUnitHorizontal, centerY + singleUnitVertical)
    val point5 = PointF(centerX - side, centerY)

    //todo: move shot related objects out
    var shotCount: Int = 0
        private set
    var successCount: Int = 0
    val successRate: Float
        get() {
            return (successCount.toFloat() / shotCount) * 100F
        }

    fun addShot(isSuccess: Boolean) {
        shotCount++
        if (isSuccess) successCount++
    }

    fun isPointInsideHex(testX: Float, testY: Float): Boolean {
        val vertexCount = 6
        val vertexX = arrayOf(point0.x, point1.x, point2.x, point3.x, point4.x, point5.x)
        val vertexY = arrayOf(point0.y, point1.y, point2.y, point3.y, point4.y, point5.y)

        var c = false
        var i = 0
        var j = vertexCount - 1
        while (i < vertexCount) {
            if (vertexY[i] > testY != vertexY[j] > testY
                && testX < (vertexX[j] - vertexX[i]) * (testY - vertexY[i]) / (vertexY[j] - vertexY[i]) + vertexX[i]
            ) c = !c
            j = i++
        }
        return c
    }
}


class HexTileMap(
    startX: Float,
    startY: Float,
    private val endX: Float,
    private val endY: Float,
    apxCount: Int //Approximate number of tiles on the map
) {

    val tiles = mutableListOf<HexTile>()
    val widthTotalPx = endX - startX
    val heightTotalPx = endY - startY

    //Approximated pixel area of a single tile
    private val singleTileArea = (heightTotalPx * widthTotalPx) / apxCount

    private val tileSide: Float = sqrt(2 * singleTileArea / (3 * sqrt(3F)))
    private val tileHeight: Float = sqrt(3F) * tileSide
    private val adjCenterDistX = (2 * tileSide) * 3 / 4 //Adjacent hex center distance = width * 3/4
    private val adjCenterDistY = tileHeight / 2

    private fun generateHexTile(centerX: Float, centerY: Float, col: Int, row: Int): HexTile {
        return HexTile(tileSide, centerX, centerY, col, row)
    }

    init {
        generateHexagonTiles()
    }

    private fun generateHexagonTiles() {

        var offsetTile = generateHexTile(0F, 0F, 0, 0)

        while (true) {

            if (offsetTile.centerY + tileHeight > endY) { //out of Y
                break
            }

            if (tiles.isNotEmpty()) {
                offsetTile = generateHexTile(
                    offsetTile.centerX,
                    offsetTile.centerY + tileHeight,
                    0,
                    offsetTile.row + 1
                )
            }

            generateTileHorizontally(offsetTile)
        }
    }

    //starts moving down first then moves up (odd column vertical)
    private fun generateTileHorizontally(offsetTile: HexTile): List<HexTile> {

        tiles.add(offsetTile)
        var isDownward = false

        while (true) {

            if (tiles.last().centerX + adjCenterDistX > endX) { //out of X
                break
            }

            tiles.last().let { prevTile ->

                tiles.add(
                    generateHexTile(
                        prevTile.centerX + adjCenterDistX,
                        prevTile.centerY + if (isDownward) adjCenterDistY else -adjCenterDistY,
                        prevTile.col + 1, prevTile.row
                    )
                )
            }
            isDownward = !isDownward
        }

        return tiles
    }
}