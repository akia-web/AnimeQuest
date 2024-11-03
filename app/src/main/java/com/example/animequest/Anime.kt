package com.example.animequest

data class Anime(
    val id: Int,
    val original_name: String,
    val overview: String,
    val original_language: String,
    val origin_country: List<String>,
    val backdrop_path: String,
    val first_air_date: String,
    val vote_average: Double,
    val vote_count: Int,
    val popularity: Double,
    val adult: Boolean,
    val poster_path: String,
    val categories: List<Category>
)

data class Category(
    val id: Int,
    val name: String
)
