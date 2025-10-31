package com.practicum.playlistmaker.search.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: Int,            // Track ID
    val trackName: String,       // Название композиции
    val artistName: String,      // Имя исполнителя
    val trackDuration: String,   // Продолжительность трека
    val artworkUrl100: String?,   // Ссылка на изображение обложки
    val collectionName: String,  // Альбом
    val releaseDate: String,     // Год выхода
    val primaryGenreName: String,// Жанр
    val country: String,          // Страна
    val previewUrl: String,      // 30 seconds audio preview
): Parcelable
