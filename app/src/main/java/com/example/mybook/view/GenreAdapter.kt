package com.example.mybook.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mybook.databinding.GenreRowBinding
import com.example.mybook.model.GModel

class GenreAdapter(
    private val cellClickListener: RecyclerViewCallback,

):
    RecyclerView.Adapter<GenreAdapter.GenereViewHolder>() {

    private var listOfMovies = mutableListOf<GModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenereViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GenreRowBinding.inflate(layoutInflater)

        return GenereViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenereViewHolder, position: Int) {
        val currentBook = listOfMovies.get(position)
        holder.binding.textViewLink.text = currentBook.title.capitalize()
        holder.binding.imageView.setBackgroundResource(currentBook.genreImage)
        Glide.with(holder.binding.imageView.context).load(currentBook.genreImage!!).into(holder.binding.imageView)
        holder.binding.card.setOnClickListener(View.OnClickListener {
            cellClickListener.onRecycleViewItemClick(currentBook.title, position);
        })
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = listOfMovies.size

    class GenereViewHolder(val binding: GenreRowBinding) : RecyclerView.ViewHolder(binding.root)

    fun setMovieList(listOfMovies: List<GModel>) {
        this.listOfMovies = listOfMovies.toMutableList()
        notifyDataSetChanged()
    }

    interface RecyclerViewCallback {
        fun onRecycleViewItemClick(gModel: String, position: Int)
    }

}