package knk.ee.neverland.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar

class ViewProgressController(val progressBar: ProgressBar, vararg views: View) {
    private var viewList: MutableList<View> = mutableListOf()

    init {
        viewList.addAll(views)
        hideProgress()
    }

    fun showProgress() {
        changeEnables(true)
        changeVisibilities(true)
    }

    fun hideProgress() {
        changeVisibilities(false)
        changeEnables(false)
    }

    private fun changeVisibilities(inProgress: Boolean) {
        val viewVisibility = if (inProgress) GONE else VISIBLE
        viewList.forEach { it.visibility = viewVisibility }

        progressBar.visibility = if (inProgress) VISIBLE else GONE
    }

    private fun changeEnables(inProgress: Boolean) {
        viewList.forEach { it.isEnabled = !inProgress }
    }
}