package knk.ee.neverland.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.exceptions.NetworkException
import knk.ee.neverland.exceptions.QuestAPIException
import knk.ee.neverland.utils.Constants
import java.io.File

class SubmitProofActivity : AppCompatActivity() {

    private var proofImage: ImageView? = null
    private var proofComment: EditText? = null
    private var imageFile: File? = null

    private var submittingExecuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_proof)

        proofImage = findViewById(R.id.proof_image)
        proofComment = findViewById(R.id.proof_comment)

        findViewById<Button>(R.id.proof_submit).setOnClickListener {
            if (!submittingExecuted) {
                SubmitProofTask().execute()
            }
        }

        findViewById<Button>(R.id.proof_retake_picture).setOnClickListener {
            openSelectingProofImageActivity()
        }

        setImageFromIntentToView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val image = ImagePicker.getImages(data).firstOrNull()
            setImageToView(image!!.path)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setImageFromIntentToView() {
        val path = intent.extras!!.getString("pathToImage")
        setImageToView(path)
    }

    private fun setImageToView(path: String) {
        imageFile = File(path)

        Glide.with(this)
            .load(imageFile)
            .into(proofImage!!)
    }

    private fun openSelectingProofImageActivity() {
        val imageTitle = getString(R.string.proof_select_image)

        ImagePicker.create(this)
            .single()
            .showCamera(true)
            .theme(R.style.AppTheme_NoActionBar)
            .toolbarImageTitle(imageTitle)
            .start()
    }

    private fun makeProofToSubmit(): ProofToSubmit = ProofToSubmit(proofComment!!.text.toString(),
        imageFile!!, intent.extras!!.getInt("questID"))

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class SubmitProofTask() : AsyncTask<Void, Void, Int>() {

        override fun doInBackground(vararg p0: Void?): Int {
            try {
                submittingExecuted = true
                DefaultAPI.proofAPI.submitProof(makeProofToSubmit())
                return Constants.SUCCESS_CODE
            } catch (ex: QuestAPIException) {
                return ex.code
            } catch (ex: NetworkException) {
                return ex.code
            }
        }

        override fun onPostExecute(code: Int?) {
            when (code) {
                Constants.BAD_REQUEST_TO_API_CODE -> showToast(getString(R.string.error_invalid_api_request))
                Constants.NETWORK_ERROR_CODE -> showToast(getString(R.string.error_network_down))
                Constants.FAIL_CODE -> showToast(getString(R.string.error_submitting_proof))
                Constants.SUCCESS_CODE -> finish() // TODO: Finish with success result?
                else -> showToast(String.format(getString(R.string.error_unexpected_code), code))
            }

            submittingExecuted = false
        }
    }
}
