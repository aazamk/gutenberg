package com.example.mybook.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.R
import com.example.mybook.databinding.ActivityBookshelvesBinding
import com.example.mybook.di.Injection
import com.example.mybook.model.Results
import com.example.mybook.viewmodel.BookShelvesViewModel
import kotlinx.android.synthetic.main.activity_bookshelves.*
import kotlinx.android.synthetic.main.layout_error.*


/**
 * @author Azam Khan
 */
class BookShelveActivity : AppCompatActivity(), FlSearchView.OnQueryTextListener,
    BookShelvesAdapter.onItemClickListener {

    var topic: String? = null
    lateinit var mlayoutManager: GridLayoutManager
    private lateinit var viewModel: BookShelvesViewModel
    private lateinit var adapter: BookShelvesAdapter
    lateinit var binding: ActivityBookshelvesBinding
    var pastVisiblesItems = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private var loading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookshelves)

        setupViewModel()
        val intent = intent
        topic = intent.getStringExtra("topic")
        tv_heading.text = topic
        binding.searchView.addQueryTextListener(this)
        setupUI()
        viewModel.loadBookData(topic.toString(), "")
        binding.tvHeading.setOnClickListener { v ->
           finish()
        }
    }

    //ui
    private fun setupUI() {


        adapter = BookShelvesAdapter(this, viewModel.books.value ?: emptyList())
        mlayoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = mlayoutManager
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mlayoutManager.getChildCount()
                    totalItemCount = mlayoutManager.getItemCount()
                    pastVisiblesItems = mlayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            viewModel.onLoadMore()
                        }
                    }
                }
            }
        })
    }

    //view model
    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory()
        ).get(BookShelvesViewModel::class.java)

        viewModel.books.observe(this, renderBookData)
        viewModel.isViewLoading.observe(this, isViewLoadingObserver)
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.isEmptyList.observe(this, emptyListObserver)
    }

    //observers
    private val renderBookData = Observer<List<Results>> {
        Log.v(TAG, "data updated $it")
        layoutError.visibility = View.GONE
        layoutEmpty.visibility = View.GONE
        adapter.update(it)
        loading = true
    }

    private val isViewLoadingObserver = Observer<Boolean> {
        Log.v(TAG, "isViewLoading $it")
        val visibility = if (it) View.VISIBLE else View.GONE
        progressBar.visibility = visibility
    }

    private val onMessageErrorObserver = Observer<Any> {
        Log.v(TAG, "onMessageError $it")
        layoutError.visibility = View.VISIBLE
        layoutEmpty.visibility = View.GONE
        textViewError.text = "Error $it"
    }

    private val emptyListObserver = Observer<Boolean> {
        Log.v(TAG, "emptyListObserver $it")
        layoutEmpty.visibility = View.VISIBLE
        layoutError.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val TAG = "CONSOLE"
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.clearList()
        viewModel.resetNextPageUrl()
        viewModel.loadBookData(topic.toString(), query.toString())
        return true
    }

    override fun onSearchBackClick() {
        adapter.clearList()
        viewModel.resetNextPageUrl()
        viewModel.loadBookData(topic.toString(), "")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(TAG, "onQueryTextChange: " + newText)
        return true;
    }

    override fun onBookClick(position: Int, url: String?) {
        if (TextUtils.isEmpty(url)) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(R.string.no_viewable_format)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            builder.setPositiveButton(R.string.ok) { dialogInterface, which ->
                dialogInterface.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        } else {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }

}
