package itis.ru.scivi.model

data class RemoteUser(val email: String, val name: String) {
    constructor() : this("", "")
}