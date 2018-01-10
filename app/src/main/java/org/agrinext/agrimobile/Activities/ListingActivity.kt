package org.agrinext.agrimobile.Activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.agrinext.agrimobile.Android.ListViewAdapter
import org.agrinext.agrimobile.R
import org.jetbrains.anko.support.v4.find
import org.json.JSONArray
import org.json.JSONObject

class ListingActivity : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_listing, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mRecyclerView: RecyclerView = find(R.id.recycler_view)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        val mLayoutManager = LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // JSON Array from frappe's listing
        var jsonArray = JSONArray()
        for (i in 0..100) {
            val jsonObject = JSONObject()
            jsonObject.put("name","List Item " + i.toString())
            jsonArray.put(jsonObject)
        }

        // specify an adapter
        val mAdapter = ListViewAdapter(jsonArray);
        mRecyclerView.setAdapter(mAdapter);
    }
}
