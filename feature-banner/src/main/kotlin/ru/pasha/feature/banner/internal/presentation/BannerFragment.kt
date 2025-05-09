package ru.pasha.feature.banner.internal.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.pasha.common.extensions.dpToPx
import ru.pasha.common.extensions.setText
import ru.pasha.common.format
import ru.pasha.common.orEmpty
import ru.pasha.common.pattern.BaseFragment
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.banner.databinding.BannerFragmentBinding
import javax.inject.Inject

internal class BannerFragment @Inject constructor(
    private val viewModelFactory: BannerViewModel.Factory
) : BaseFragment<BannerViewState, BannerViewModel, BannerFragmentBinding>(
    viewModelClass = BannerViewModel::class.java
) {

    private var autoScrollJob: Job? = null
    private val adapter = BannerAdapter()

    override fun createViewModel(): BannerViewModel = viewModelFactory.create()

    override fun onApplyInsets(insets: WindowInsetsCompat): WindowInsetsCompat {
        return insets.also(::applyInsets)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): BannerFragmentBinding {
        return BannerFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        startAutoScroll()
    }

    override fun render(viewState: BannerViewState) {
        if (viewState !is BannerViewState.Success) {
            binding.bannerProgressIndicator.isVisible = false
            binding.bannerPager.isVisible = false
        }
        when (viewState) {
            is BannerViewState.Loading -> {}
            is BannerViewState.Success -> with(binding) {
                if (viewState.banners.isEmpty()) return

                bannerProgressIndicator.isInvisible = !viewState.showProgress
                bannerPager.isVisible = true

                adapter.submitList(viewState.banners)
                bannerProgressIndicator.setCount(viewState.banners.size)
                bannersButton.setText(viewState.buttonText)
            }

            is BannerViewState.Error -> {}
        }
    }

    override fun consumeSideEffect(effect: SideEffect) {
        when (effect) {
            is BannerSideEffect.ShowError -> {
                Snackbar.make(
                    binding.root,
                    effect.message.orEmpty().format(requireContext()),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onPause() {
        cancelAutoScroll()
        super.onPause()
    }

    override fun onDestroyView() {
        cancelAutoScroll()
        super.onDestroyView()
    }

    private fun setupViewPager() {
        binding.bannerPager.apply {
            adapter = this@BannerFragment.adapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.bannerProgressIndicator.setPosition(
                        position, tickDuration = BANNER_VISIBILITY_DURATION
                    )
                    resetAutoScroll()
                }
            })
        }
    }

    private fun startAutoScroll() {
        if (adapter.itemCount < 1) return
        autoScrollJob = viewLifecycleOwner.lifecycleScope.launch {
            while (true) {
                delay(BANNER_VISIBILITY_DURATION)
                val next = (binding.bannerPager.currentItem + 1) % adapter.itemCount
                binding.bannerPager.setCurrentItem(next, true)
            }
        }
    }

    private fun resetAutoScroll() {
        cancelAutoScroll()
        startAutoScroll()
    }

    private fun cancelAutoScroll() {
        autoScrollJob?.cancel()?.also { autoScrollJob = null }
    }

    private fun applyInsets(insets: WindowInsetsCompat) {
        val barsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        binding.bannerProgressIndicator.updateLayoutParams<MarginLayoutParams> {
            updateMargins(top = barsInsets.top + 8.dpToPx)
        }
        binding.bannersButton.updateLayoutParams<MarginLayoutParams> {
            updateMargins(bottom = barsInsets.bottom + 8.dpToPx)
        }
    }

    private companion object {
        const val BANNER_VISIBILITY_DURATION = 5000L
    }
}
