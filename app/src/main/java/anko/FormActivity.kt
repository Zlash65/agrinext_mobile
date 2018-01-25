package anko

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import org.jetbrains.anko.*

import org.agrinext.agrimobile.R

/**
 * Generate with Plugin
 * @plugin Kotlin Anko Converter For Xml
 * @version 1.2.1
 */
class FormActivity : Activity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		scrollView {
			linearLayout {
				id = R.id.linear2
				gravity = Gravity.END //not support value
				orientation = LinearLayout.VERTICAL
				linearLayout {
					id = R.id.linear1
					orientation = LinearLayout.VERTICAL
					imageView {
						id = R.id.imageView4
						//android:maxHeight = 500dp //not support attribute
						//android:minHeight = 200dp //not support attribute
						//app:srcCompat = @drawable/ic_menu_manage //not support attribute
					}.lparams(width = matchParent, height = dip(255))
				}.lparams(width = matchParent)
				linearLayout {
					id = R.id.linear3
					gravity = Gravity.END //not support value
					orientation = LinearLayout.VERTICAL
					textView {
						id = R.id.docName
						text = "TextView"
						textSize = dip(15).toFloat()
					}.lparams(width = dip(411)) {
						weight = 0.30f //not support value
					}
					tableLayout {
						topPadding = dip(12)
						tableRow {
							//android:weightSum = 1 //not support attribute
							textView {
								id = R.id.fieldName
								text = "FieldName"
								textSize = dip(10).toFloat()
							}.lparams {
								weight = 0.30f //not support value
							}
							editText {
								id = R.id.fieldValue
//								text = "FieldValue"
								setText("FieldValue")
								textSize = dip(10).toFloat()
							}.lparams {
								weight = 0.70f //not support value
							}
						}.lparams(width = matchParent, height = matchParent)
					}.lparams(width = matchParent, height = matchParent)
				}.lparams(width = matchParent)
			}.lparams(width = matchParent)
		}
	}
}
