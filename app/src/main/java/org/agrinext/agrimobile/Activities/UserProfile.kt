package org.agrinext.agrimobile.Activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import org.agrinext.agrimobile.Android.ApplicationController
import org.agrinext.agrimobile.Android.BaseCompatActivity
import org.agrinext.agrimobile.R

class UserProfile : BaseCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        triggerEvents() //bundle up triggers
    }

    fun triggerEvents() {
        setSpinnerLocale()
    }

    fun setSpinnerLocale() {
        val sharedPref = this.getSharedPreferences("myApp", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val languageArray: HashMap<String, String> =
                hashMapOf("English" to "en", "Marathi" to "mr", "Telugu" to "te") // language codes

        val language = findViewById<Spinner>(R.id.tvLanguage)
        val items = arrayOf("English", "Marathi", "Telugu")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        language!!.setAdapter(adapter);

        if (sharedPref.contains("language")) {
            val spinnerPosition = adapter.getPosition(sharedPref.getString("language", ""))
            language.setSelection(spinnerPosition)
        } else {
            val lang = language.getSelectedItem().toString()
            editor.putString("language", lang).apply()
        }

        language.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                editor.putString("language", selectedItem).apply()
                ApplicationController.instance?.changeLocale(languageArray[selectedItem].toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // do nothing
            }
        }
    }
}
