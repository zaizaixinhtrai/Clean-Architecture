package com.gcox.fansmeet.core.activity

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.os.SystemClock
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.BaseBundle
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.BeLiveDefaultTheme
import com.gcox.fansmeet.core.BeLiveThemeHelper
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.core.dialog.ProgressHUD
import com.gcox.fansmeet.util.FileUtility.getOutputMediaFile
import com.gcox.fansmeet.util.StringUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

import java.util.ArrayList
import java.util.Locale

/**
 * Created by User on 9/12/2015.
 */
abstract class BaseActivity : HandleErrorActivity() {

    protected lateinit var fileUri: Uri
    private val locale: Locale? = null

    internal var intent: Intent? = null
    // create static Dialog cause of crashing when transition between activity, activity should have  dialog
    var isShowing = false
    var dialog: ProgressHUD? = null

    internal var baseBundle: BaseBundle? = null

    private var mLastClickTime: Long = 0

    protected lateinit var utility: DialogInfoUtility

    protected var isInFront: Boolean = false

    protected lateinit var mRxPermissions: RxPermissions
    protected lateinit var mBeLiveThemeHelper: BeLiveThemeHelper
    protected lateinit var compositeDisposable: CompositeDisposable

    protected val isActivityRunning: Boolean
        get() = !isFinishing && !isDestroyed

    fun getBaseBundle(key: String): BaseBundle? {
        baseBundle = getIntent().extras!!.getParcelable(key)
        return baseBundle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        utility = DialogInfoUtility()
        fileUri = Uri.parse("")
        mRxPermissions = RxPermissions(this)
        mBeLiveThemeHelper = createTheme()
        compositeDisposable = CompositeDisposable()
        if (mBeLiveThemeHelper.isTransparentStatusBarRequired) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    open fun createTheme(): BeLiveThemeHelper {
        return BeLiveDefaultTheme()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("uriMedia", fileUri.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // get the file url
        val keyUri = savedInstanceState.getString("uriMedia", "")
        if (!keyUri.isEmpty()) fileUri = Uri.parse(keyUri)
    }

    override fun onResume() {
        super.onResume()
        isInFront = true
    }

    override fun onPause() {
        super.onPause()
        isInFront = false
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyDialog()
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    @Throws(NullPointerException::class)
    fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    fun startPickVideos() {
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "video/*"
            startActivityForResult(intent, Constants.PICK_VIDEO_FROM_LIBRARY_REQUEST)
        } catch (e: Exception) {
            Timber.e(e.message)
        }

    }

    fun takePictureFromGallery() {//com.google.android.apps.photos
        val photoPickerIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        //        photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        photoPickerIntent.type = "image/*"
        if (photoPickerIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(photoPickerIntent, Constants.REQUEST_PIC_FROM_LIBRARY)
        }
    }

    private fun takePictureFromGalleryWithoutGooglePhotos() {
        val intentShareList = ArrayList<Intent>()
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_PICK
        shareIntent.type = "image/*"
        val resolveInfoList = packageManager.queryIntentActivities(shareIntent, 0)

        for (resInfo in resolveInfoList) {
            val packageName = resInfo.activityInfo.packageName
            val name = resInfo.activityInfo.name

            if (!packageName.contains("com.google.android.apps.photos")) {
                val intent = Intent()
                intent.component = ComponentName(packageName, name)
                intent.action = Intent.ACTION_PICK
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                intentShareList.add(intent)
            }
        }

        if (intentShareList.isEmpty()) {
            Toast.makeText(this, "No apps available!", Toast.LENGTH_SHORT).show()
        } else {
            val chooserIntent = Intent.createChooser(intentShareList.removeAt(0), "")
            chooserIntent.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentShareList.toTypedArray<Parcelable>()
            )
            startActivityForResult(chooserIntent, Constants.REQUEST_PIC_FROM_LIBRARY)
        }
    }


    fun performCrop(inPut: Uri, outPut: Uri) {
        // take care of exceptions
        try {
            val maxWidth = 800// width and height;
            val maxHeight = 800// width and height;
            val options = UCrop.Options()
            options.setCompressionFormat(Bitmap.CompressFormat.PNG)
            options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL)
            options.setHideBottomControls(true)
            options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.color_header_title))

            var uCrop = UCrop.of(inPut, outPut)
            uCrop = uCrop.withAspectRatio(1f, 1f)//set squared output
            uCrop = uCrop.withMaxResultSize(maxWidth, maxHeight)
            uCrop.withOptions(options)
            uCrop.start(this, Constants.REQUEST_PIC_FROM_CROP)
        } catch (anfe: ActivityNotFoundException) {
            val toast = Toast
                .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT)
            toast.show()
            anfe.printStackTrace()
        }
        // respond to users whose devices do not support the crop action
    }

    fun getRealPathFromURI(contentURI: Uri?, activity: Activity): String? {

        if (contentURI == null) {
            return ""
        }
        val result: String?
        val cursor = activity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun showDialog(mcContext: Context, message: String?) {
        if (dialog == null) {
            dialog = ProgressHUD(
                mcContext,
                R.style.ProgressHUD
            )
            dialog!!.setTitle("")
            dialog!!.setContentView(R.layout.progress_hudd)
//            if (message == null || message.length == 0) {
//                dialog!!.findViewById<View>(R.id.message).visibility = View.VISIBLE
//            } else {
//                val txt = dialog!!.findViewById<View>(R.id.message) as TextView
//                txt.text = message
//            }
            dialog!!.setCancelable(false)
            dialog!!.window!!.attributes.gravity = Gravity.CENTER
            val lp = dialog!!.window!!.attributes
            lp.dimAmount = 0.2f
            dialog!!.window!!.attributes = lp

            dialog!!.show()

            isShowing = true
        }

    }

    fun dismissDialog() {
        if (dialog != null) {
            if (dialog!!.isShowing) {
                try {
                    dialog!!.dismiss()
                } catch (error: IllegalArgumentException) {
                    error.printStackTrace()
                    Timber.e(error)
                }

            }
            destroyDialog()

        }
    }

    fun destroyDialog() {
        dialog = null
        isShowing = false
    }

    override fun onStop() {
        super.onStop()

    }


    fun toastTextWhenNoInternetConnection(message: String) {
        if (StringUtil.isNullOrEmptyString(message)) {
            Toast.makeText(
                applicationContext,
                getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun preventMultiClicks(): Boolean {

        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true
        }

        mLastClickTime = SystemClock.elapsedRealtime()

        return false
    }

    fun setLocale() {
        val myLocale = Locale(AppsterApplication.mAppPreferences.appLanguage)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

    }

    fun navigateToActivity(clazz: Class<out Activity>) {
        val intent = Intent(this, clazz)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.push_in_to_right, R.anim.push_in_to_left
        )
        startActivity(intent, options.toBundle())
        finish()
    }

    fun startActivityProfile(userID: Int?, userName: String?) {
        if (userID == null || userName == null) return
        if (preventMultiClicks()) return

        val options =
            ActivityOptionsCompat.makeCustomAnimation(
                applicationContext,
                R.anim.push_in_to_right,
                R.anim.push_in_to_left
            )
    }

    companion object {
        private val ACTION_REQUEST_GALLERY = 3

    }
}
