package com.example.animequest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type
import java.util.concurrent.Executors

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: SearchView
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: AnimeAdapter // Adapter pour le RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Initialisation des éléments UI
        searchView = findViewById(R.id.searchView)
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView)

        // Initialiser le RecyclerView
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        searchResultsAdapter = AnimeAdapter(mutableListOf()) // Adapter avec une liste mutable
        searchResultsRecyclerView.adapter = searchResultsAdapter

        // Configurer la recherche en temps réel
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    searchResultsAdapter.updateAnimeList(mutableListOf()) // Réinitialiser la liste
                    return true
                }
                performSearch(newText)
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val url = "https://akiagaming.fr/api/movies/search?title=$query"
                val response = getRequest(url)
                response?.let {
                    val searchResults = parseAnimeList(it)
                    runOnUiThread {
                        searchResultsAdapter.updateAnimeList(searchResults) // Mettre à jour la liste de l'adaptateur
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseAnimeList(jsonResponse: String): List<Anime> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<Anime>>() {}.type
        return gson.fromJson(jsonResponse, listType)
    }

    private fun getRequest(sUrl: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder().url(sUrl).build()
        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) response.body?.string() else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
