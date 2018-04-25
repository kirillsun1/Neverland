package knk.ee.neverland.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.api.models.ProofToSubmit
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.Constants
import knk.ee.neverland.utils.UIErrorView
import java.io.File

class SubmitProofActivity : AppCompatActivity() {
    @BindView(R.id.proof_image)
    lateinit var proofImage: ImageView

    @BindView(R.id.proof_comment)
    lateinit var proofComment: EditText

    private var imageFile: File? = null
    private var submittingExecuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_proof)
        ButterKnife.bind(this)

        setImageFromIntentToView()
    }

    @OnClick(R.id.proof_submit)
    fun sumbitProofButtonClick() {
        if (!submittingExecuted) {
            runSubmitProofTask(makeProofToSubmit())
        }
    }

    @OnClick(R.id.proof_retake_picture)
    fun retakePictureButtonClick() {
        openSelectingProofImageActivity()
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
        // TODO: Find another image picture and finish this!
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

            APIAsyncTask<Boolean>()
                .doBefore {
                    submittingExecuted = true
                    changeButtonsEnablePropertyWhenSubmittingExecutionChanges()
                }
                .request {
                    DefaultAPI.proofAPI.submitProof(proofToSubmit)
                    success = true
                    true
                }
                .uiErrorView(UIErrorView.Builder().with(this)
                    .messageOnAPIFail(R.string.error_submitting_proof)
                    .create())
                .doAfter {
                    submittingExecuted = false
                    changeButtonsEnablePropertyWhenSubmittingExecutionChanges()
                    if (success) {
                        setResult(Constants.SUCCESS_CODE)
                        finish()
                    }
                }
                .execute()
        }
    }
}
