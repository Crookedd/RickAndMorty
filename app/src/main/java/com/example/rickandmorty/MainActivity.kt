package com.example.rickandmorty

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var charactersData: Results
    private lateinit var adapter: CharacterAdapter
    private val retrofitService: RickAndMortyApiService = RetrofitClient.getClient("https://rickandmortyapi.com/api/").create(
        RickAndMortyApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.r_view)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val characterRetrofit = retrofitService.getCharacters()
                charactersData = characterRetrofit
                withContext(Dispatchers.Main) {
                    adapter = CharacterAdapter(this@MainActivity, charactersData)
                    recyclerView.adapter = adapter
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }
}

data class Character(val id: Int, val name: String, val species: String, val image: String) {
    fun getType(): Int {
        if (species.lowercase() == "human") {
            return 1
        } else if (species.lowercase() == "alien") {
            return 2
        }

        return 0
    }
}
interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(): Results
}
object RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

data class Results(val results: List<Character>)
class CharacterAdapter(private val context: Context, private val characters: Results) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        if (characters.results[position].getType() == 1) {
            return R.layout.image
        } else if (characters.results[position].getType() == 2) {
            return R.layout.name
        }

        return R.layout.species
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        if (viewType == R.layout.image) {
            return ImageViewHolder(view)
        } else if (viewType == R.layout.name) {
            return NameViewHolder(view)
        }

        return SpeciesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bind(characters.results[position])
        } else if (holder is NameViewHolder) {
            holder.bind(characters.results[position])
        } else {
            (holder as SpeciesViewHolder).bind(characters.results[position])
        }
    }

    override fun getItemCount(): Int {
        return characters.results.size
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imageView: ImageView = view.findViewById(R.id.imageView)

        fun bind(character: Character) {
            Picasso.get().load(character.image).placeholder(R.drawable.ic_launcher_background).into(imageView)
        }
    }

    class NameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.textViewName)

        fun bind(character: Character) {
            textView.text = character.name
        }
    }

    class SpeciesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.textViewSpecies)

        fun bind(character: Character) {
            textView.text = character.species
        }
    }
}