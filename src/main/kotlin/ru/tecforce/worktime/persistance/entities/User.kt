package ru.tecforce.worktime.persistance.entities

data class User(val id: Long, val login: String, val firstname: String, val lastname: String, val mail: String)
