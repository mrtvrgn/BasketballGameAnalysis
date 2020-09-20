package com.mrtvrgn.gameanalysis.model

data class Shot(
    val _id: String,
    val point: Int,
    val segment: Int,
    val InOut: Boolean,
    val ShotPosX: Float,
    val ShotPosY: Float
)

data class Player(val name: String, val surname: String)

data class PlayerRecord(val user: Player, val shots: List<Shot>)

data class GameRecord(val data: List<PlayerRecord>)