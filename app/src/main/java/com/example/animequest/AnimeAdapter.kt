package com.example.animequest

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AnimeAdapter(private var animeList: List<Anime>) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animeImage: ImageView = itemView.findViewById(R.id.animeImage)
        val animeTitle: TextView = itemView.findViewById(R.id.animeTitle)
        val animeDescription: TextView = itemView.findViewById(R.id.animeDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_anime, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.animeTitle.text = anime.original_name
        holder.animeDescription.text = truncateString(anime.overview, 75)

        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original/" + anime.poster_path)
            .into(holder.animeImage)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AnimeDetailActivity::class.java).apply {
                putExtra("animeTitle", anime.original_name)
                putExtra("animeDescription", anime.overview)
                putExtra("animeLanguage", anime.original_language)
                putExtra("animeCountry", anime.origin_country.joinToString(", "))
                putExtra("animeFirstAirDate", anime.first_air_date)
                putExtra("animeVoteAverage", anime.vote_average)
                putExtra("animeVoteCount", anime.vote_count)
                putExtra("animePopularity", anime.popularity.toString())
                putExtra("animeIsAdult", anime.adult.toString())
                putExtra("animeBackdropPath", anime.backdrop_path)
                putExtra("animePosterPath", anime.poster_path)
            }
            context.startActivity(intent)
        }
    }

    private fun truncateString(input: String, maxLength: Int): String {
        return if (input.length > maxLength) {
            input.substring(0, maxLength) + "..."
        } else {
            input
        }
    }


    fun updateAnimeList(newAnimeList: List<Anime>) {
        animeList = newAnimeList
        notifyDataSetChanged() // Informer l'adaptateur que les données ont changé
    }

    override fun getItemCount() = animeList.size
}