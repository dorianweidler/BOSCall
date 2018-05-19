package de.boscall.dto

data class RegistrationRequest(var unitId: Long, var secret: String, var token: String, var userName: String)