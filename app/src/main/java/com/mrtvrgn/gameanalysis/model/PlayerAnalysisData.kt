package com.mrtvrgn.gameanalysis.model

data class PlayerAnalysisData(
    val playerName: String,
    val successRate: Float,
    val heatMapGraphics: List<HexTileGraphics>
)