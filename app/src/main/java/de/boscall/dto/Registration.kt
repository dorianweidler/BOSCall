package de.boscall.dto

data class Registration(val userId: Long, val unitName: String, val apiKey: String, var secret: String, var unitId: Long)