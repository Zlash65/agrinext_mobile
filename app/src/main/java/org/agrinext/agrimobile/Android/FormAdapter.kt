package org.agrinext.agrimobile.Android

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.agrinext.agrimobile.R

/**
 * Created by allen on 16/01/18.
 */
class FormAdapter(private var items: ArrayList<FormActivityUI>): RecyclerView.Adapter<FormAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var userDto = items[position]
        holder?.txtName?.text = userDto.name
        holder?.txtComment?.text = userDto.comment
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.form_edit_text, parent, false)

        return ViewHolder(itemView)
    }

    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var txtName: TextView? = null
        var txtComment: TextView? = null

        init {
            this.txtName = row?.findViewById<TextView>(R.id.txtName)
            this.txtComment = row?.findViewById<TextView>(R.id.txtComment)
        }
    }
}