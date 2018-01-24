package org.agrinext.agrimobile.Activities

import android.os.Bundle
import org.jetbrains.anko.setContentView
import org.json.JSONObject
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.mntechnique.otpmobileauth.auth.AuthReqCallback
import org.acra.ACRA.log
import org.agrinext.agrimobile.Android.*
import org.agrinext.agrimobile.R
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import org.json.JSONArray


class FormGeneratorActivity : BaseCompatActivity() {
    var docData = JSONObject()

    companion object {
        val DOC_DATA = "DOC_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.doctype = intent.extras.get("DocType").toString()
        val docname = intent.extras.get("Docname").toString()
        super.setupDocType(this.doctype!!)
        fetchDoc(docname)

        FormGeneraterUI(docData, docMeta!!).setContentView(this)
    }

    override fun onBackPressed() {
        finish()
    }

    fun fetchDoc(docname: String) {
        val filters = JSONArray().put(JSONArray().put("name").put("=").put(docname))
        val request = FrappeClient(this).get_all(
                doctype = this.doctype!!,
                filters = filters.toString(),
                fields = "[\"*\"]"
        )

        val responseCallback = object : AuthReqCallback {
            override fun onSuccessResponse(result: String) {
                this@FormGeneratorActivity.docData = JSONObject(JSONObject(result).getJSONArray("data").get(0).toString())
            }

            override fun onErrorResponse(error: String) {
                toast(R.string.somethingWrong)
            }
        }

        FrappeClient(this).executeRequest(request, responseCallback)
    }

    fun get_DocData(): JSONObject {
        return this.docData
    }

//    open fun setupDocType() {
//        if (doctype == null) {
//            this.doctype = "Note"
//        }
//
//        val keyDocTypeMeta = StringUtil.slugify(this.doctype) + "_meta"
//        var pref = this.getSharedPreferences(FormGeneratorActivity.DOC_DATA, 0)
//        val editor = pref.edit()
//        val doctypeMetaString = pref.getString(keyDocTypeMeta, null)
//        if (doctypeMetaString != null){
//            this.docData = JSONObject(doctypeMetaString)
//        } else {
//            doAsync {
//                FrappeClient(activity).retrieveDocTypeMeta(editor, keyDocTypeMeta, doctype)
//                uiThread {
//                    setupDocType()
//                }
//            }
//        }
//    }
}
