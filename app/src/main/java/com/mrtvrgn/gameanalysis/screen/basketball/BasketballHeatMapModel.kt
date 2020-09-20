package com.mrtvrgn.gameanalysis.screen.basketball

import com.mrtvrgn.gameanalysis.net.GameService

class BasketballHeatMapModel {

    private val service = GameService.Creator.create()

    suspend fun getGameRecords() = service.getGameRecord()
}