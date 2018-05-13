package knk.ee.neverland.groups

import android.app.AlertDialog
import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mikhaellopez.circularimageview.CircularImageView
import com.mindorks.placeholderview.annotations.Click
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View
import knk.ee.neverland.R
import knk.ee.neverland.api.DefaultAPI
import knk.ee.neverland.models.Group
import knk.ee.neverland.network.APIAsyncTask
import knk.ee.neverland.utils.UIErrorView

@Layout(R.layout.group_element)
class JoinGroupElement(private val context: Context,
                       private val group: Group) {

    @View(R.id.group_element_layout)
    lateinit var groupElementLayout: LinearLayout

    @View(R.id.group_avatar)
    lateinit var groupAvatar: CircularImageView

    @View(R.id.group_name_box)
    lateinit var groupName: TextView

    @View(R.id.group_creator)
    lateinit var groupInfo: TextView

    @Resolve
    fun onResolve() {
        groupName.text = group.name
        groupInfo.text = "${group.quantity} subscriber(s)"
        loadAvatar(context, group)
    }

    @Click(R.id.group_element_layout)
    fun clickOnGroupElement() {
        AlertDialog.Builder(context)
            .setTitle("Confirmation")
            .setMessage("Do you really want to join group ${group.name}?")
            .setPositiveButton(R.string.yes, { dialogInterface, _ ->
                runJoinGroupTask()
                dialogInterface.dismiss()
            })
            .setNegativeButton(R.string.no, { dialogInterface, i ->
                dialogInterface.cancel()
            })
            .create()
            .show()
    }

    private fun runJoinGroupTask() {
        var success = false
        APIAsyncTask<Boolean>()
            .request {
                DefaultAPI.groupAPI.subscribe(group.id)
                success = true
                true
            }
            .uiErrorView(UIErrorView.Builder()
                .with(context)
                .create())
            .doAfter {
                if (success) {
                    (context as FindGroupActivity).finish()
                }
            }
            .execute()
    }

    private fun loadAvatar(context: Context, group: Group) {
        Glide.with(context)
            .load(group.avatarLink)
            .apply(RequestOptions()
                .placeholder(R.drawable.no_avatar_group)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
            .into(groupAvatar)
    }
}
