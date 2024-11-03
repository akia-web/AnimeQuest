package com.example.animequest

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AnimeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)

        // Récupérer les données transmises via l'intent
        val animeTitle = intent.getStringExtra("animeTitle") ?: "Titre non disponible"
        val animeDescription = intent.getStringExtra("animeDescription") ?: "Description non disponible"
        val animeBackdropPath = intent.getStringExtra("animeBackdropPath") ?: ""
        val animePosterPath = intent.getStringExtra("animePosterPath") ?: ""
        val animeLanguage = intent.getStringExtra("animeLanguage") ?: "Langue non disponible"
        val animeCountry = intent.getStringExtra("animeCountry") ?: "Pays non disponible"
        val animeFirstAirDate = intent.getStringExtra("animeFirstAirDate") ?: "Date non disponible"
        val animeVoteAverage = intent.getDoubleExtra("animeVoteAverage", 0.0)
        val animeVoteCount = intent.getIntExtra("animeVoteCount", 0)

        // Mettre à jour l'UI avec les informations
        findViewById<TextView>(R.id.animeTitle).text = animeTitle
        findViewById<TextView>(R.id.animeDescription).text = animeDescription
        findViewById<TextView>(R.id.animeLanguage).text = "Langue : $animeLanguage"
        findViewById<TextView>(R.id.animeCountry).text = "Pays d'origine : $animeCountry"
        findViewById<TextView>(R.id.animeFirstAirDate).text = "Première diffusion : $animeFirstAirDate"
        findViewById<TextView>(R.id.animeVoteAverage).text = "Note moyenne : $animeVoteAverage / 10"
        findViewById<TextView>(R.id.animeVoteCount).text = "$animeVoteCount votes"

        val animePosterImageView = findViewById<ImageView>(R.id.animePosterImage)
        val animeBackdropImageView = findViewById<ImageView>(R.id.animeBackdropImage)

        // Charger l'image du poster avec Glide si l'URL n'est pas vide
        if (animePosterPath.isNotEmpty()) {
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original/$animePosterPath")
                .into(animePosterImageView)
        }

        // Charger l'image du backdrop avec Glide si l'URL n'est pas vide
        if (animeBackdropPath.isNotEmpty()) {
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original/$animeBackdropPath")
                .into(animeBackdropImageView)
        }
    }
}
