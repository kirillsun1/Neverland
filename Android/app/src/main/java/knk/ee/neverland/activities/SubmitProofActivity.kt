package knk.ee.neverland.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.utils.APIAsyncRequest
import knk.ee.neverland.utils.Constants
import java.io.File

class SubmitProofActivity : AppCompatActivity() {

    private lateinit var proofImage: ImageView
    private lateinit var proofComment: EditText
    private var imageFile: File? = null

    private var submittingExecuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_proof)

        proofImage = findViewById(R.id.proof_image)
        proofComment = findViewById(R.id.proof_comment)

        findViewById<Button>(R.id.proof_submit).setOnClickListener {
            if (!submittingExecuted) {
                runSubmitProofTask(makeProofToSubmit())
            }
        }

        findViewById<Button>(R.id.proof_retake_picture).setOnClickListener {
            openSelectingProofImageActivity()
        }

        setImageFromIntentToView()
    }

    private fun setImageFromIntentToView() {
        val path = intent.extras!!.getString("pathToImage")
        setImageToView(path)
    }

    private fun setImageToView(path: String) {
        imageFile = File(path)

        Glide.with(this)
            .load(imageFile)
            .into(proofImage)
    }

    private fun openSelectingProofImageActivity() {

    }

    private fun changeButtonsEnablePropertyWhenSubmittingExecutionChanges() {
        val enabled = !submittingExecuted
        findViewById<Button>(R.id.proof_retake_picture).isEnabled = enabled
        findViewById<Button>(R.id.proof_submit).isEnabled = enabled
    }

    private fun makeProofToSubmit(): ProofToSubmit = ProofToSubmit(proofComment.text.toString(),
        imageFile!!, intent.extras!!.getInt("questID"))

    private fun runSubmitProofTask(proofToSubmit: ProofToSubmit) {
        if (!submittingExecuted) {
            var success = false

            APIAsyncRequest.Builder<Boolean>()
                .before {
                    submittingExecuted = true
                    changeButtonsEnablePropertyWhenSubmittingExecutionChanges()
                }
                .request {
                    DefaultAPI.proofAPI.submitProof(proofToSubmit)
                    success = true
                    true
                }
                .onAPIFailMessage { R.string.error_submitting_proof }
                .setContext(this)
                .showMessages(true)
                .after {
                    submittingExecuted = false
                    changeButtonsEnablePropertyWhenSubmittingExecutionChanges()
                    if (success) {
                        setResult(Constants.SUCCESS_CODE)
                        finish()
                    }
                }
                .finish()
                .execute()
        }
    }
}
