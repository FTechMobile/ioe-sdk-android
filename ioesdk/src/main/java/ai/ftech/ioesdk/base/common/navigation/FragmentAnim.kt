package ai.ftech.ioesdk.base.common.navigation

import ai.ftech.ioesdk.R
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes

class FragmentAnim(
    @AnimatorRes @AnimRes val enter: Int = R.anim.slide_enter_left_to_right,
    @AnimatorRes @AnimRes val exist: Int = R.anim.slide_exit_right_to_left,
    @AnimatorRes @AnimRes val popEnter: Int = R.anim.slide_pop_enter_right_to_left,
    @AnimatorRes @AnimRes val popExit: Int = R.anim.slide_pop_exit_left_to_right
)
