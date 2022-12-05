package com.example.arborparker.network

data class Spot(
    var id: Int,
    var isOpen: Boolean,
    var timeLastOccupied: String?,
    var vanAccessible: Boolean
)
