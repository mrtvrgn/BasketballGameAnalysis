package com.mrtvrgn.gameanalysis.screen.basketball

import android.graphics.PointF
import com.mrtvrgn.gameanalysis.helper.HexTileMap
import com.mrtvrgn.gameanalysis.model.HexTileGraphics
import com.mrtvrgn.gameanalysis.model.PlayerAnalysisData
import com.mrtvrgn.gameanalysis.model.toGraphics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HeatMapRecordsPresenter(private val model: BasketballHeatMapModel) : CoroutineScope {

    private lateinit var view: HeatMapRecordsView
    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private lateinit var playerResults: List<PlayerAnalysisData>
    private var selectedPlayerPosition = 0

    fun attachView(view: HeatMapRecordsView) {
        this.view = view
    }

    fun generateMap(startX: Float, startY: Float, endX: Float, endY: Float) {

        launch {

            val defaultTileMap = HexTileMap(startX, startY, endX, endY, approximatedTileCount)

            val pixelsMeterX = defaultTileMap.widthTotalPx / horizontalCourtLength
            val pixelsMeterY = defaultTileMap.heightTotalPx / verticalCourtLength
            val singleTileAreaMeters = //Approximated area meters of a single tile
                (horizontalCourtLength * verticalCourtLength) / approximatedTileCount

            //Find relative points from meters placed in data to canvas coordinate system
            fun relativePoint(x: Float, y: Float): PointF = PointF(
                (pixelsMeterX * (horizontalCourtLength / 2F)) + (x * pixelsMeterX),
                (pixelsMeterY * verticalBucketLength) + (-1 * y * pixelsMeterY)
            )

            val records = model.getGameRecords()

            playerResults = records.data.map { playerRecord ->

                val graphics = playerRecord.shots.mapNotNull { shot ->
                    val relativePoint = relativePoint(shot.ShotPosX, shot.ShotPosY)

                    defaultTileMap.tiles.firstOrNull {
                        val isInside = it.isPointInsideHex(relativePoint.x, relativePoint.y)
                        if (isInside) {
                            it.addShot(shot.InOut)
                        }
                        isInside
                    }
                }.map { it.toGraphics(singleTileAreaMeters) }

                PlayerAnalysisData(
                    playerName = "${playerRecord.user.name} ${playerRecord.user.surname}",
                    successRate = 100F * playerRecord.shots.count { it.InOut } / playerRecord.shots.size,
                    heatMapGraphics = graphics
                )
            }
            launch(Dispatchers.Main) {
                view.addPlayersData(playerResults.map { it.playerName })
                view.setSelectedPlayerPosition(selectedPlayerPosition)
                view.setOnPlayersDropdownItemSelectedBehavior { position ->
                    selectedPlayerPosition = position
                    present()
                }
                present()
            }
        }
    }

    private fun present() {

        launch(Dispatchers.Main) {
            view.drawMap(playerResults[selectedPlayerPosition].heatMapGraphics)
            val successRate = playerResults[selectedPlayerPosition].successRate.toInt()
            view.setSuccessRate(successRate, "%${successRate}")
        }
    }

    companion object {
        const val horizontalCourtLength = 15F //15m horizontal length of a basketball court
        const val verticalCourtLength = 14F //14m vertical length of a basketball court
        const val verticalBucketLength = 1.2F //1.2m from baseline
        const val approximatedTileCount = 600
    }
}

interface HeatMapRecordsView {
    fun drawMap(tiles: List<HexTileGraphics>)
    fun addPlayersData(data: List<String>)
    fun setSelectedPlayerPosition(index: Int)
    fun setOnPlayersDropdownItemSelectedBehavior(behavior: (selectedIndex: Int) -> Unit)
    fun setSuccessRate(progress: Int, rate: String)
}