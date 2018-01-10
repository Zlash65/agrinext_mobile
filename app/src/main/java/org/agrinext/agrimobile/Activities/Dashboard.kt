package org.agrinext.agrimobile.Activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import org.agrinext.agrimobile.R
import org.jetbrains.anko.support.v4.find

class Dashboard : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.activity_dashboard, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillUp()
    }

    fun fillUp() {
        val desktop_text = find<TextView>(R.id.desktop_text)
        val linearLayoutDesktop = find<LinearLayout>(R.id.linearLayoutDesktop)
        desktop_text.setText(R.string.welcome)
    }

}