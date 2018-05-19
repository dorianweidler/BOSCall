package de.boscall.dto

data class TokenUpdateRequest(val userId: Long, val token: String, val apiKey: String, val userName: String)