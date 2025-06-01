package ru.pasha.common.pattern

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.Router
import ru.pasha.common.NavigationProvider
import ru.pasha.common.R
import ru.pasha.common.di.findDependency
import ru.pasha.common.extensions.safeSetOnApplyWindowInsetsListener

abstract class BindingFragment<VB : ViewBinding> : NavigationProvider, Fragment() {

    override val router: Router by lazy { findDependency<Router>() }

    private var isFragmentViewDestroyed = false

    private lateinit var _binding: VB
    protected val binding get() = _binding

    private val animators = mutableListOf<Animator>()

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun onApplyInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        val barsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

        binding.root.updateLayoutParams<MarginLayoutParams> {
            updateMargins(
                top = barsInsets.top,
                left = barsInsets.left,
                right = barsInsets.right,
                bottom = imeInsets.bottom.coerceAtLeast(barsInsets.bottom)
            )
        }
        return insets
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return getViewBinding(inflater, container).also { viewBinding ->
            isFragmentViewDestroyed = false
            _binding = viewBinding

            viewBinding.root.safeSetOnApplyWindowInsetsListener { _, insets ->
                onApplyInsets(insets)
            }
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFragmentViewDestroyed = true

        animators.forEach { it.cancel() }
        animators.clear()
    }

    fun AlertDialog.Builder.showAlertDialog() {
        val dialog = show()

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onPause(owner: LifecycleOwner) {
                dialog.dismiss()
            }
        })
    }

    protected fun View.safeAnimate(animator: () -> Animator) {
        with(animator()) {
            if (isFragmentViewDestroyed) {
                cancel()
                return
            }

            animator.apply {
                addListener(SafeAnimatorListener())
                start()
                animators.add(this@with)
            }
        }
    }

    private inner class SafeAnimatorListener : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            if (isFragmentViewDestroyed) {
                animation.cancel()
                animators.remove(animation)
            }
        }

        override fun onAnimationEnd(animation: Animator) {
            animators.remove(animation)
        }

        override fun onAnimationCancel(animation: Animator) {
            animators.remove(animation)
        }
    }

    private fun applyColorSchemeWithInsets() {
        activity?.window?.statusBarColor = resources.getColor(R.color.walking_app_night_500)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity?.window?.navigationBarColor = resources.getColor(R.color.walking_app_night_500)
    }
}
