package com.example.mybook.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybook.R
import com.example.mybook.model.GModel
import kotlinx.android.synthetic.main.activity_genere.*

class GenereActivity : AppCompatActivity(), GenreAdapter.RecyclerViewCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genere)
        initView()
    }

     fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
         //add ItemDecoration
        val movieListAdapter = GenreAdapter(this)
        recyclerView.adapter = movieListAdapter
        movieListAdapter.setMovieList(generateDummyData())
    }

    private fun generateDummyData(): List<GModel> {
        val listOfGenre = mutableListOf<GModel>()

        var genreModel = GModel( "Fiction",  R.drawable.ic_fiction)
        listOfGenre.add(genreModel)

        genreModel = GModel( "Drama",  R.drawable.ic_drama)
        listOfGenre.add(genreModel)

        genreModel = GModel( "Humor", R.drawable.ic_humour)
        listOfGenre.add(genreModel)

        genreModel = GModel( "Politics",  R.drawable.ic_politics)
        listOfGenre.add(genreModel)

        genreModel = GModel( "Philosophy", R.drawable.ic_philosophy)
        listOfGenre.add(genreModel)

        genreModel = GModel( "History",  R.drawable.ic_history)
        listOfGenre.add(genreModel)

        genreModel = GModel( "Adventure",  R.drawable.ic_adventure)
        listOfGenre.add(genreModel)

        return listOfGenre
    }

    override fun onRecycleViewItemClick(gModel: String, position: Int) {
        val intent = Intent(this, BookShelveActivity::class.java)
        intent.putExtra("topic", gModel)
        startActivity(intent)
    }
}