package knk.ee.neverland.groups

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import knk.ee.neverland.R

class CreateGroupDialog(context: Context) : Dialog(context) {

    var onGroupCreated: ((String) -> Unit)? = null

    private var groupName: String = ""
    private var createButton: Button

    init {
        setContentView(R.layout.create_group_layout)

        createButton = findViewById(R.id.confirm)
        val cancelButton = findViewById<Button>(R.id.cancel)

        validateGroupName()

        createButton.setOnClickListener {
            onGroupCreated?.invoke(groupName)
            dismiss()
        }

        cancelButton.setOnClickListener {
            cancel()
        }

        findViewById<EditText>(R.id.group_name_box).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                groupName = text.toString()
                validateGroupName()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun validateGroupName() {
        createButton.isEnabled = groupNameIsCorrect()
    }

    private fun groupNameIsCorrect(): Boolean = !groupName.isBlank() // TODO: Normal validator
}
