package com.example.mybook.view

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.mybook.databinding.RowBookCardBinding
import com.example.mybook.model.Formats
import com.example.mybook.model.Results

/**
 * @author Azam Khan
 */
class BookShelvesAdapter(
    private val cellClickListener: onItemClickListener,
    private var bookList: List<Results>
) :
    RecyclerView.Adapter<BookShelvesAdapter.BookViewHolder>() {

    private val TAG: String = "BookShelvesAdapter"

    class BookViewHolder(val binding: RowBookCardBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowBookCardBinding.inflate(layoutInflater)

        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = bookList.get(position)
        holder.binding.tvBookName.text = currentBook.title.capitalize()

        when {
            currentBook.authors.size > 0 -> {
                holder.binding.textViewLink.text = currentBook.authors.get(0).name
            }
        }
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))

        Glide.with(holder.binding.ivBookCover.context).load(currentBook.formats.url)
            .apply(requestOptions).into(holder.binding.ivBookCover)

        holder.binding.ivBookCover.setOnClickListener {
            cellClickListener.onBookClick(position, getFormat(currentBook.formats))
        }
        holder.binding.executePendingBindings()
    }

    private fun getFormat(formats: Formats): String? {
        if (!TextUtils.isEmpty(formats.textHtml)) {
            return formats.textHtml
        } else if (!TextUtils.isEmpty(formats.pdf)) {
            return formats.pdf
        } else if (TextUtils.isEmpty(formats.textPlain))
            return formats.textPlain;

        return "";
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    fun update(data: List<Results>) {
        var tempList : ArrayList<Results> = arrayListOf()
        tempList.addAll(bookList);
        tempList.addAll(data)
        bookList = tempList
        notifyDataSetChanged()
    }
    fun clearList(){
        bookList = emptyList();
        notifyDataSetChanged()
    }

    interface onItemClickListener {
        fun onBookClick(position: Int, url: String?)
    }
}