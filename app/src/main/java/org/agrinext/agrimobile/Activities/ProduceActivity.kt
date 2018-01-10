package org.agrinext.agrimobile.Activities

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.support.v7.widget.SearchView
import com.mntechnique.otpmobileauth.auth.AuthReqCallback
import org.agrinext.agrimobile.Android.EndlessRecyclerViewScrollListener
import org.agrinext.agrimobile.BuildConfig
import org.agrinext.agrimobile.Android.FrappeClient
import org.agrinext.agrimobile.Android.ListViewAdapter
import org.agrinext.agrimobile.R
import org.json.JSONArray
import org.json.JSONObject
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ProgressBar
import org.jetbrains.anko.searchManager
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast

class ProduceActivity : Fragment() {
    var recyclerAdapter: ListViewAdapter? = null
    var recyclerModels = JSONArray()
    var searchView : SearchView? = null
    var mAccountManager: AccountManager? = null
    var accounts: Array<Account>? = null
    internal lateinit var mRecyclerView: RecyclerView
    var progressBar: ProgressBar? = null
    var filters: String? = null
    var user:String? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_listing, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAccountManager = AccountManager.get(activity)
        accounts = mAccountManager?.getAccountsByType(BuildConfig.APPLICATION_ID)
        user = accounts?.get(0)?.name

        filters = "[[\"owner\",\"=\",\"" + user + "\"]]"

        mRecyclerView = find(R.id.recycler_view)
        progressBar = find(R.id.edit_progress_bar)
        progressBar?.visibility = View.VISIBLE
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true)

        setRecycleViewScrollListener()
        loadData(filters = filters!!)
    }

    private fun setRecycleViewScrollListener() {
        // use a linear layout manager
        val mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView.setLayoutManager(mLayoutManager)

        mRecyclerView.addOnScrollListener(object: EndlessRecyclerViewScrollListener(mLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadData(page, filters!!)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) { // here <---------------
        super.onCreateOptionsMenu(menu, menuInflater) // <----------
        menuInflater!!.inflate(R.menu.list_view, menu) // <------------

        val searchManager = context.searchManager
        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName)) // here <---------
        searchView?.setMaxWidth(Integer.MAX_VALUE)
        // listening to search query text change
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                recyclerModels = JSONArray()
                filters = "[[\"owner\",\"=\",\"$user\"],[\"name\",\"like\",\"%$query%\"]]"
                loadData(filters=filters!!)
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                return false
            }
        })

        searchView?.setOnCloseListener(object :SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                recyclerModels = JSONArray()
                filters = "[[\"owner\",\"=\",\"" + user + "\"]]"
                loadData(filters=filters!!)
                setRecycleViewScrollListener()
                return false
            }
        })
//        return true // something here <-----------
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.getItemId()
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }
    fun loadData(page: Int? = null, filters: String, limit_page_length:String = "5") {
        val limit_start = ((page?.times(limit_page_length.toInt()))?:0).toString()

        Log.d("Filters", filters)
        Log.d("Limit_page_length", limit_page_length)
        Log.d("limit_start", limit_start)

        val request = FrappeClient(activity).get_all(
                doctype = "Add Produce",
                filters = filters,
                limit_page_length = limit_page_length,
                limit_start = limit_start
        )

        val responseCallback = object : AuthReqCallback {
            override fun onSuccessResponse(s: String) {
                val response = JSONObject(s)
                // JSON Array from frappe's listing
                for (i in 0 until response.getJSONArray("data").length()) {
                    recyclerModels.put(response.getJSONArray("data").get(i))
                }
                if (page != null){
                   // Notify an adapter
                    recyclerAdapter!!.notifyDataSetChanged()
                } else if (page == null) {
                    // specify and add an adapter
                    recyclerAdapter = ListViewAdapter(recyclerModels)
                    mRecyclerView.adapter = recyclerAdapter
                }

                progressBar?.visibility = View.GONE
            }

            override fun onErrorResponse(s: String) {
                progressBar?.visibility = View.VISIBLE
                toast(R.string.somethingWrong)
            }
        }

        FrappeClient(activity).executeRequest(request, responseCallback)
    }
}