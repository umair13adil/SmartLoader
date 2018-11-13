package com.umairadil.smartdownloaderapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blackbox.apps.smartdownloader.SmartLoader
import com.umairadil.smartdownloaderapp.R
import com.umairadil.smartdownloaderapp.adapters.PinsAdapter
import com.umairadil.smartdownloaderapp.ui.base.BaseFragment
import com.umairadil.smartdownloaderapp.utils.isConnected
import com.umairadil.smartdownloaderapp.utils.recyclerViewUtils.EndlessScrollListener
import com.umairadil.smartdownloaderapp.viewmodels.PinsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pins_list.*

class PinsListFragment : BaseFragment() {

    private val TAG = "PinsListFragment"
    private lateinit var viewModel: PinsViewModel
    private lateinit var adapter: PinsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_pins_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(PinsViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //This will setup list adapter
        setUpListAdapter()

        //Check if device is connected to internet
        if (!isConnected(activity!!)) {
            showEmptyView()
            return
        }

        //Request images from server
        fetchBoard()

        swipeRefreshLayout.setOnRefreshListener {

            hideViewWithAnimation(fab_go_to_top)

            //Clear adapter list
            adapter.submitList(arrayListOf())

            //Clear Cached images
            SmartLoader().resetCache()

            //Re-Request images from server
            fetchBoard()
        }
    }

    /*
     * This will show empty view text.
     */
    private fun showEmptyView() {
        txt_no_internet.visibility = View.VISIBLE
    }

    /*
     * Setup RecyclerView list adapter.
     */
    private fun setUpListAdapter() {
        adapter = PinsAdapter()
        adapter.setHasStableIds(true)

        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter

        val loadMoreListener = object : EndlessScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                Log.i(TAG, "Page: $page, Total Items: $totalItemsCount")

                //Request images from server
                fetchBoard()
            }
        }

        //Set visible threshold size, minimum item before new loading starts
        loadMoreListener.visibleThreshold = 4

        //Add on Load More listener
        recycler_view?.addOnScrollListener(loadMoreListener)
    }

    /*
     * This will fetch list of images from server.
     */
    private fun fetchBoard() {

        viewModel.getBoardPins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    showLoading()
                }
                .subscribeBy(
                        onNext = {
                            val list = viewModel.mapPinsResponse(it)
                            adapter.submitList(list)

                            swipeRefreshLayout.isRefreshing = false
                            hideLoading()
                            showViewWithAnimation(fab_go_to_top)
                        },
                        onError = {
                            it.printStackTrace()
                            hideLoading()
                        }
                )
    }
}