package ru.pasha.common

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

abstract class BindingFragment<VB : ViewBinding> : ViewBinding, Fragment() {

    private var isFragmentViewDestroyed = false

    private lateinit var _binding: VB
    protected val binding get() = _binding

    private val animators = mutableListOf<Animator>()

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun onApplyInsets(insets: WindowInsetsCompat): WindowInsetsCompat = insets

    @Suppress("FunctionOnlyReturningConstant")
    protected fun insetsAutoConsumeEnabled(): Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return getViewBinding(inflater, container).also { viewBinding ->
            isFragmentViewDestroyed = false
            _binding = viewBinding

            if (!insetsAutoConsumeEnabled()) {
                viewBinding.root.fitsSystemWindows = false
                viewBinding.root.safeSetOnApplyWindowInsetsListener { _, insets ->
                    onApplyInsets(insets)
                }
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
}
