package ru.tecforce.worktime.exceptions

class NotFoundException(override val message: String): Exception()
class ConflictException(override val message: String): Exception()