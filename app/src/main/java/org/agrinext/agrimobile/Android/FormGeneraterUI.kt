package org.agrinext.agrimobile.Android

import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import org.acra.ACRA
import org.agrinext.agrimobile.Activities.FormGeneratorActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by revant on 16/1/18.
 */
class FormGeneraterUI(doc: JSONObject, docMeta:JSONObject): AnkoComponent<FormGeneratorActivity> {

    val doc = doc
    val docMeta = docMeta

    override fun createView(ui: AnkoContext<FormGeneratorActivity>) = with(ui) {
        scrollView {
            verticalLayout {
                padding = dip(32)

                imageView(android.R.drawable.ic_menu_manage).lparams {
                    margin = dip(16)
                    gravity = Gravity.CENTER
                }

                var fieldsArray = returnLabel()
                Log.d("Form", doc.toString())
                for (i in 0 until fieldsArray.length() - 1) {
                    var x = fieldsArray.getJSONArray(i)
                    textView() {
                        text = x[1].toString()
                    }.lparams(width = matchParent, height = matchParent)
//                textView(){
//                    text = doc.getString(x[0].toString())
//                }.lparams(width= matchParent, height = matchParent)
                }



                button("Error!") {
                    onClick {
                        throw JSONException("User Thrown Exception!")
                    }
                }
            }
        }
    }

    fun returnLabel(): JSONArray{
        Log.d("Data", FormGeneratorActivity().get_DocData().toString())
        val fieldsArray = JSONArray()
        val fields = docMeta?.getJSONArray("fields")!!

        for (i in 0 until fields.length() - 1){
            if(fields.getJSONObject(i).has("label")){
                fieldsArray.put(
                        JSONArray()
                                .put(fields.getJSONObject(i).getString("fieldname"))
                                .put(fields.getJSONObject(i).getString("label"))
                )
            }
        }
//        Log.d()
        return fieldsArray
    }
}