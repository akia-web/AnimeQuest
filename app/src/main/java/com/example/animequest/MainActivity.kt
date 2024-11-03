package com.example.animequest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Type
import java.util.concurrent.Executors
import androidx.appcompat.widget.SearchView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var animeList: List<Anime> // Liste des objets Anime pour le RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            enableEdgeToEdge()
            setContentView(R.layout.activity_main)

            // Initialisation des éléments UI
            recyclerView = findViewById(R.id.recyclerView)

            // Configuration du RecyclerView avec une disposition en grille
            recyclerView.layoutManager = GridLayoutManager(this, 2)

            // Ajuster le remplissage pour les barres système
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            loadAnimeList() // Chargement initial de la liste des animes pour le RecyclerView
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadAnimeList() {
        Executors.newSingleThreadExecutor().execute {
            try {
                val url = "https://akiagaming.fr/api/movies"
                val response = getRequest(url)
                response?.let {
                    animeList = parseAnimeList(it)
                    runOnUiThread {
                        val adapter = AnimeAdapter(animeList)
                        recyclerView.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Parser le JSON en une liste d'objets Anime
    private fun parseAnimeList(jsonResponse: String): List<Anime> {
        val gson = Gson()
        val listType: Type = object : TypeToken<List<Anime>>() {}.type
        return gson.fromJson(jsonResponse, listType)
    }

    // Exécuter la requête GET
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
