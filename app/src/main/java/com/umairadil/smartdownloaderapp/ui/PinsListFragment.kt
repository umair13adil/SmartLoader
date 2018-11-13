package com.umairadil.smartdownloaderapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.umairadil.smartdownloaderapp.R
import com.umairadil.smartdownloaderapp.adapters.PinsAdapter
import com.umairadil.smartdownloaderapp.ui.base.BaseFragment
import com.umairadil.smartdownloaderapp.utils.isConnected
import com.umairadil.smartdownloaderapp.utils.recyclerViewUtils.EndlessScrollListener
import com.umairadil.smartdownloaderapp.utils.recyclerViewUtils.SpacesItemDecoration
import com.umairadil.smartdownloaderapp.utils.recyclerViewUtils.smoothScroll
import com.umairadil.smartdownloaderapp.viewmodels.PinsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_pins_list.*

class PinsListFragment : BaseFragment() {

    private val TAG = "PinsListFragment"
    private lateinit var viewModel: PinsViewModel
    private lateinit var adapter: PinsAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

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
        setUpSwipeRefreshListener()

        //Check if device is connected to internet
        if (!isConnected(activity!!)) {
            showEmptyView()
            return
        }

        //Request images from server
        fetchBoard()

        //If clicked, scroll to top
        fab_go_to_top.setOnClickListener {
            smoothScroll(recycler_view, 0)
        }
    }

    /**
     * This will setup SwipeRefresh layout.
     */
    private fun setUpSwipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener {

            //Hide FAB
            hideViewWithAnimation(fab_go_to_top)

            //Clear adapter list
            adapter.submitList(arrayListOf())

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

        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = adapter

        val decoration = SpacesItemDecoration(16)
        recycler_view.addItemDecoration(decoration)

        //Add on LoadMore listener
        setupOnLoadMoreListener()
    }

    /**
     * This will add 'EndlessScroll' listener.
     */
    private fun setupOnLoadMoreListener(){
        val loadMoreListener = object : EndlessScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int) {

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
                    showLoading(layout_progress)
                }
                .subscribeBy(
                        onNext = {
                            val list = viewModel.mapPinsResponse(it)
                            adapter.submitList(list)

                            swipeRefreshLayout.isRefreshing = false
                            hideLoading(layout_progress)
                            showViewWithAnimation(fab_go_to_top)
                        },
                        onError = {
                            it.printStackTrace()
                            hideLoading(layout_progress)
                        }
                )
    }
}