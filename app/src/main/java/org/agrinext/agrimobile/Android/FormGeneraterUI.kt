package org.agrinext.agrimobile.Android

import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import org.acra.ACRA
import org.agrinext.agrimobile.Activities.FormGeneratorActivity
import org.agrinext.agrimobile.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by revant on 16/1/18.
 */
class FormGeneraterUI(doc: JSONObject, docMeta: JSONObject) : AnkoComponent<FormGeneratorActivity> {

    val doc = doc
    val docMeta = docMeta

    override fun createView(ui: AnkoContext<FormGeneratorActivity>) = with(ui) {

        scrollView {
            linearLayout {
                id = R.id.linear2
                gravity = Gravity.END //not support value
                orientation = LinearLayout.VERTICAL

                linearLayout {
                    id = R.id.linear1
                    orientation = LinearLayout.VERTICAL

                    imageView(android.R.drawable.ic_menu_manage) {
                        id = R.id.imageView4
                    }.lparams(width = matchParent, height = dip(250))

                }.lparams(width = matchParent) // end of layout 1

                linearLayout {
                    id = R.id.linear3
                    gravity = Gravity.END //not support value
                    orientation = LinearLayout.VERTICAL

                    textView {
                        id = R.id.docName
                        text = "TextView"
                        textSize = dip(12).toFloat()
                        leftPadding = dip(10)
                    }.lparams(width = matchParent)

                    tableLayout {
                        topPadding = dip(12)

                        var fieldsArray = returnLabel()
                        for (i in 0 until fieldsArray.length() - 1) {
                            var x = fieldsArray.getJSONArray(i)

                            tableRow {
                                weightSum = 1f

                                textView {
                                    id = R.id.fieldName
                                    text = x[1].toString() + " : "
                                    textSize = dip(8).toFloat()
                                    gravity = Gravity.CENTER
                                }.lparams {
                                    weight = 0.30f //not support value
                                }

                                editText {
                                    id = R.id.fieldValue
                                    setText("FieldValue")
                                    textSize = dip(8).toFloat()
                                    inputType = 0
                                    backgroundResource = android.R.color.transparent
                                }.lparams {
                                    weight = 0.70f //not support value
                                }

                            }.lparams(width = matchParent, height = matchParent) // tablerow end
                        }

                    }.lparams(width = matchParent, height = matchParent) // tablelayout end

                }.lparams(width = matchParent) // linear3 end

            }.lparams(width = matchParent)
        }
    }

    fun returnLabel(): JSONArray {
        Log.d("Data", FormGeneratorActivity().get_DocData().toString())
        val fieldsArray = JSONArray()
        val fields = docMeta?.getJSONArray("fields")!!

        for (i in 0 until fields.length() - 1) {
            if (fields.getJSONObject(i).has("label")) {
                fieldsArray.put(
                        JSONArray()
                                .put(fields.getJSONObject(i).getString("fieldname"))
                                .put(fields.getJSONObject(i).getString("label"))
                )
            }
        }
        return fieldsArray
    }
}