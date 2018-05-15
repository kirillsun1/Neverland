package knk.ee.neverland.user

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
import knk.ee.neverland.models.User
import knk.ee.neverland.profile.ProfileActivity

@Layout(R.layout.user_element)
class UserElement(private val context: Context, private val user: User) {

    @View(R.id.user_element_layout)
    lateinit var userElementLayout: LinearLayout

    @View(R.id.user_avatar)
    lateinit var userAvatar: CircularImageView

    @View(R.id.user_name)
    lateinit var userName: TextView

    @View(R.id.user_user_name)
    lateinit var userUserName: TextView

    @Resolve
    fun onResolve() {
        userName.text = user.toString()
        userUserName.text = user.userName

        loadAvatar()
    }

    @Click(R.id.user_element_layout)
    fun onUserClicked() {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra("user", user)
        context.startActivity(intent)
        println("dfjghkldfjhdf")
    }

    private fun loadAvatar() {
        Glide.with(context)
            .load(user.avatarLink)
            .apply(RequestOptions()
                .placeholder(R.drawable.no_avatar)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
            .into(userAvatar)
    }
}