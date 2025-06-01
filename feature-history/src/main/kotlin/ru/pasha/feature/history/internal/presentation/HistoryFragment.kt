package ru.pasha.feature.history.internal.presentation

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.pasha.common.format
import ru.pasha.common.pattern.BaseFragment
import ru.pasha.common.pattern.SideEffect
import ru.pasha.feature.history.databinding.HistoryFragmentBinding
import javax.inject.Inject

internal class HistoryFragment @Inject constructor(
    private val viewModelFactory: HistoryViewModel.Factory,
) : BaseFragment<HistoryViewState, HistoryViewModel, HistoryFragmentBinding>(
    viewModelClass = HistoryViewModel::class.java,
) {

    private var historyAdapter: HistoryAdapter? = null

    override fun createViewModel(): HistoryViewModel = viewModelFactory.create()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): HistoryFragmentBinding {
        return HistoryFragmentBinding.inflate(inflater, container, false).apply {
            errorRefreshButton.setOnClickListener {
                viewModel.loadRoutes()
            }
            backButton.setOnClickListener {
                viewModel.navigateBack()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyRecyclerView.adapter = HistoryAdapter(
            downloadCallback = viewModel::downloadRoute,
            removeLocalCallback = viewModel::removeRoute,
            openCallback = viewModel::navigateToPreview,
            loadMore = viewModel::loadRoutes,
        ).also { historyAdapter = it }
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
    }

    override fun render(viewState: HistoryViewState) {
        renderShimmer(viewState)
        binding.historyProgressBar.isIndeterminate = viewState.isLoading

        binding.historyRecyclerView.isVisible = viewState.error == null
        binding.historyRecyclerView.itemAnimator = DefaultItemAnimator().apply {
            supportsChangeAnimations = true
        }
        historyAdapter?.updateItems(viewState.routes, forceUpdate = false)

        viewState.error?.message?.let { binding.errorTitle.text = it.format(requireContext()) }
        binding.errorTitle.isVisible = viewState.error != null
        binding.errorRefreshButton.isVisible = viewState.error != null
    }

    override fun consumeSideEffect(effect: SideEffect) = when (effect) {
        is DownloadError -> {
            showSnackbar(effect.title.format(requireContext()).toString())
        }

        else -> Unit
    }

    override fun onDestroyView() {
        historyAdapter = null
        super.onDestroyView()
    }

    private fun renderShimmer(viewState: HistoryViewState) {
        if (viewState.isLoading) {
            binding.historyShimmer.root.isVisible = true
        } else {
            binding.historyShimmer.root.safeAnimate {
                ObjectAnimator
                    .ofFloat(binding.historyShimmer.root, View.ALPHA, 1f, 0f).apply {
                        duration = SHIMMER_HIDING_DURATION
                        doOnEnd {
                            binding.historyShimmer.root.stopShimmer()
                            binding.historyShimmer.root.isVisible = false
                        }
                    }
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG,
        ).apply {
            setBackgroundTint(
                resources.getColor(
                    ru.pasha.common.R.color.walking_app_light_500,
                    requireActivity().theme
                )
            )
        }.show()
    }

    private companion object {
        const val SHIMMER_HIDING_DURATION = 1000L
    }
}
