package com.practicum.playlistmaker

data class Track(
    val trackId: Int,            // Track ID
    val trackName: String,       // Название композиции
    val artistName: String,      // Имя исполнителя
    val trackTimeMillis: Long,   // Продолжительность трека
    val artworkUrl100: String    // Ссылка на изображение обложки
)
