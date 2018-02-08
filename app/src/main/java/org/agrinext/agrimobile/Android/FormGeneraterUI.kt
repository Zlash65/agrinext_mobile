package org.agrinext.agrimobile.Android

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.agrinext.agrimobile.Frappe.DocField
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.json.JSONArray

class FormGeneraterUI(docMeta: DocField): AnkoComponent<ViewGroup> {

//    val doc = doc
    val docMeta = docMeta

    override fun createView(ui: AnkoContext<ViewGroup>): View {

        val out = with(ui) {
            cardView {
                lparams(width= matchParent, height=dip(50))

                linearLayout() {
                    lparams(width = matchParent, height = matchParent)
                    weightSum = 1f
                    orientation = LinearLayout.HORIZONTAL

                    textView {
                        id = Ids.fieldName
                        text = "FieldName"
                        textSize = dip(7).toFloat()
                        gravity = Gravity.CENTER
                    }.lparams(width = dip(0)) {
                        weight = 0.40f //not support value
                    }

                    if (docMeta.fieldtype=="Data" || docMeta.fieldtype=="Link"
                            || docMeta.fieldtype=="DateTime" || docMeta.fieldtype=="Date") {

                        editText {
                            id = Ids.fieldValue
                            setText("FieldValue")
                            textSize = dip(8).toFloat()
                            backgroundResource = android.R.color.transparent
                        }.lparams(width = dip(0)) {
                            weight = 0.60f
                        }

                    } else if(docMeta.fieldtype=="Check"){

                        checkBox {
                            id = Ids.fieldValue
                            check(true)
                        }.lparams(width = dip(0)) {
                            weight = 0.60f
                        }

                    } else {

                        textView {
                            id = Ids.fieldValue
                            text = "FieldName"
                            textSize = dip(8).toFloat()
                        }.lparams(width = dip(0)) {
                            weight = 0.60f
                        }
                    }


                }
            }
        }
        return out
    }

    companion object {
        object Ids {
            val fieldName = 0
            val fieldValue = 1
        }
    }
}