package knk.ee.neverland.groups

import android.content.Context
import android.content.Intent
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
import knk.ee.neverland.models.Group

@Layout(R.layout.group_element)
class GroupElement(private val context: Context, private val group: Group) {

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
        groupInfo.text = "Created by ${group.admin}"
        loadAvatar(context, group)
    }

    @Click(R.id.group_element_layout)
    fun clickOnGroupElement() {
        val intent = Intent(context.applicationContext, GroupDetailsActivity::class.java)
        intent.putExtra("group", group)
        context.startActivity(intent)
    }

    private fun loadAvatar(context: Context, group: Group) {
        Glide.with(context)
            .load(group.admin.avatarLink)
            .apply(RequestOptions()
                .placeholder(R.drawable.no_avatar_group)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
            .into(groupAvatar)
    }
}