package org.agrinext.agrimobile.Activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.mntechnique.otpmobileauth.auth.AuthReqCallback
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.agrinext.agrimobile.Android.FrappeClient
import org.agrinext.agrimobile.R
import org.json.JSONObject
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import org.agrinext.agrimobile.Android.PermissionUtils
import android.provider.MediaStore
import android.content.Intent
import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import com.facebook.drawee.view.SimpleDraweeView
import org.agrinext.agrimobile.BuildConfig
import org.jetbrains.anko.accountManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UserProfile : Fragment() {
    var frappeClient: FrappeClient? = null

    companion object {
        val SELECT_FILE = 0
        val REQUEST_CAMERA = 1
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        frappeClient = FrappeClient(context)
        return inflater?.inflate(R.layout.activity_user_profile, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProfileText()
        setupProfilePhoto()
        ivProfileImage.setOnClickListener {
            selectImage()
        }
    }

    fun setupProfilePhoto() {
        var picture = ""
        val request = OAuthRequest(Verb.GET, frappeClient?.getServerURL() + getString(R.string.openIDEndpoint))
        val callback = object : AuthReqCallback{
            override fun onErrorResponse(error: String) {
                Log.d("responseError", error)
            }

            override fun onSuccessResponse(result: String) {
                val jsonResponse = JSONObject(result)
                picture = jsonResponse.getString("picture")

                val uri = Uri.parse(picture)
                (ivProfileImage as SimpleDraweeView).setImageURI(uri)
            }
        }
        frappeClient?.executeRequest(request, callback)
    }

    fun setupProfileText() {
        val request = OAuthRequest(Verb.GET, frappeClient?.getServerURL() + getString(R.string.openIDEndpoint))
        val callback = object : AuthReqCallback{
            override fun onErrorResponse(error: String) {
                Log.d("responseError", error)
            }

            override fun onSuccessResponse(result: String) {
                val jsonResponse = JSONObject(result)
                tvFullName.setText(jsonResponse.getString("name"))
            }
        }
        frappeClient?.executeRequest(request, callback)
    }

    fun selectImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Add Photo!")
        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
            val result = PermissionUtils().checkStoragePermission(activity)
            if (items[item] == "Take Photo") {
                if (result) {
                    val cameraPerm = PermissionUtils().checkCameraPermission(activity)
                    if (cameraPerm) cameraIntent()
                }
            } else if (items[item] == "Choose from Library") {
                if (result)
                    galleryIntent()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    fun cameraIntent() {
        val mIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(mIntent, REQUEST_CAMERA)
    }

    fun galleryIntent() {
        val mIntent = Intent()
        mIntent.type = "image/*"
        mIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(mIntent, "Select File"), SELECT_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_FILE -> onSelectFromGalleryResult(data!!)
                REQUEST_CAMERA -> onCaptureImageResult(data!!)
            }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            val bm = MediaStore.Images.Media.getBitmap(activity.applicationContext.contentResolver, data.data)
            val cursor = activity.contentResolver.query(data.data, null, null, null, null);
            var byteArrayOutputStream = ByteArrayOutputStream()

            var displayName = ""

            if (cursor != null && cursor.moveToFirst()) {
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
            bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val picb64string = android.util.Base64.encodeToString(byteArrayOutputStream.toByteArray(), android.util.Base64.DEFAULT);
            Log.d("b64enc", picb64string)
            uploadPhoto(picb64string, displayName)
            //ivProfileImage.setImageBitmap(bm)
        }
    }

    fun uploadPhoto(fileData:String, filename: String? = null) {
        val accounts = context.accountManager.getAccountsByType(BuildConfig.APPLICATION_ID)
        val request = OAuthRequest(Verb.PUT, frappeClient?.getServerURL() + "/api/method/agrinext.api.upload_file")
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        var _filename = filename
        if (filename.isNullOrEmpty()) {
            _filename = UUID.randomUUID().toString().replace("-", "")
        }

        request.addBodyParameter("filename", _filename)
        request.addBodyParameter("filedata", fileData)
        request.addBodyParameter("doctype", "User")
        request.addBodyParameter("docname", accounts[0].name)
        request.addBodyParameter("decode", "true")
        request.addBodyParameter("docfield", "user_image")

        val callback = object : AuthReqCallback {
            override fun onSuccessResponse(result: String) {
                Log.d("SUCCESS!", result)
                setupProfilePhoto()
            }

            override fun onErrorResponse(error: String) {
                Log.d("ERROR!", error)
            }

        }

        frappeClient?.executeRequest(request, callback)
    }
    fun onCaptureImageResult(data:Intent) {
        val thumbnail = data.extras.get("data") as Bitmap
        val bytes = ByteArrayOutputStream()

        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        val picb64string = android.util.Base64.encodeToString(bytes.toByteArray(), android.util.Base64.DEFAULT);

        Log.d("b64enc", picb64string)
        uploadPhoto(picb64string)
        /*

        val destination = File(getExternalStorageDirectory(),
                System.currentTimeMillis().toString() + ".jpg")
        val fo: FileOutputStream
        destination.createNewFile()
        fo = FileOutputStream(destination)
        fo.write(bytes.toByteArray())
        fo.close()
        ivProfileImage.setImageBitmap(thumbnail)

        */
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        // mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }
}
