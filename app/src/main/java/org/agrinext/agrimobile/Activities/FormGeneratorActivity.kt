package org.agrinext.agrimobile.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.acra.ACRA.log
import org.agrinext.agrimobile.Android.BaseCompatActivity
import org.agrinext.agrimobile.Android.FormActivityUI
import org.agrinext.agrimobile.Android.FormAdapter
import org.agrinext.agrimobile.R
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.json.JSONObject

class FormGeneratorActivity : BaseCompatActivity() {

    private var formrecyclerview: RecyclerView? = null
    private var meta: JSONObject? = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        formrecyclerview = findViewById(R.id.form_recycler_view)
        var adapter = FormAdapter(generateData())
        val layoutManager = LinearLayoutManager(applicationContext)
        formrecyclerview?.layoutManager = layoutManager
        formrecyclerview?.itemAnimator = DefaultItemAnimator()

        formrecyclerview?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun generateData(): ArrayList<FormActivityUI> {
        var result = ArrayList<FormActivityUI>()
        setupMeta()

//        for (i in meta) {
//            var user: FormActivityUI = FormActivityUI("Zarrar", "Awesome work ;)")
//            result.add(user)
//        }

        var iter: Iterator<String> = meta!!.keys()
        while (iter.hasNext()) {
            var key = iter.next()
            var value: String = meta!!.get(key).toString()
            var user: FormActivityUI = FormActivityUI(key, value)
            result.add(user)
        }

        return result
    }

    fun setupMeta() {
        var pref = this.getSharedPreferences(ListingActivity.DOCTYPE_META, 0)
        val doctypeMetaString = pref.getString("produce_meta", null)
        meta = JSONObject(doctypeMetaString)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, FormGeneratorActivity::class.java)
            return intent
        }
    }
}

