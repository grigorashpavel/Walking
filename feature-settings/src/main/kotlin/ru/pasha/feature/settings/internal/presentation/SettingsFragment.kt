package ru.pasha.feature.settings.internal.presentation

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.pasha.common.format
import ru.pasha.common.pattern.BaseFragment
import ru.pasha.common.pattern.SideEffect
import ru.pasha.common.views.FeedbackDialog
import ru.pasha.common.views.FeedbackView
import ru.pasha.feature.settings.databinding.SettingsFragmentBinding
import ru.pasha.feature.settings.internal.domain.SettingsEntity
import javax.inject.Inject

internal class SettingsFragment @Inject constructor(
    private val viewModelFactory: SettingsViewModel.Factory,
) : BaseFragment<SettingsViewState, SettingsViewModel, SettingsFragmentBinding>(
    viewModelClass = SettingsViewModel::class.java,
) {

    private var settingsAdapter: SettingsAdapter? = null

    override fun createViewModel(): SettingsViewModel = viewModelFactory.create()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SettingsFragmentBinding {
        return SettingsFragmentBinding.inflate(inflater, container, false).apply {
            backButton.setOnClickListener {
                viewModel.navigateBack()
            }
            logoutButton.setOnClickListener {
                viewModel.logout()
            }

            settingsRecyclerView.adapter = SettingsAdapter(
                changeLanguageCallback = {
                    viewModel.changeLanguage(it)
                    requireActivity().recreate()
                },
                changeThemeCallback = {
                    viewModel.changeTheme(it)
                    requireActivity().recreate()
                },
                changeLocationOptionCallback = viewModel::changeLocationOption,
                feedbackCallback = {
                    FeedbackView.show(
                        requireContext(),
                        isReport = true,
                        object : FeedbackDialog.Callback {
                            override fun onSubmit(rating: Int, comment: String) {
                                viewModel.reportProblem(comment)
                            }

                            override fun onCancel() = Unit
                        }
                    )
                }
            ).also { settingsAdapter = it }
            settingsRecyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }
    }

    override fun render(viewState: SettingsViewState) {
        renderShimmer(viewState)
        val items = viewState.toSettingsItems()
        settingsAdapter?.updateItems(items, forceUpdate = false)
    }

    override fun consumeSideEffect(effect: SideEffect) = when (effect) {
        is DownloadError -> {
            showSnackbar(effect.title.format(requireContext()).toString())
        }

        else -> Unit
    }

    override fun onDestroyView() {
        settingsAdapter = null
        super.onDestroyView()
    }

    private fun renderShimmer(viewState: SettingsViewState) {
        binding.settingsShimmer.root.safeAnimate {
            ObjectAnimator
                .ofFloat(binding.settingsShimmer.root, View.ALPHA, 1f, 0f).apply {
                    duration = SHIMMER_HIDING_DURATION
                    doOnEnd {
                        binding.settingsShimmer.root.stopShimmer()
                        binding.settingsShimmer.root.isVisible = false
                    }
                }
        }
    }

    private fun SettingsViewState.toSettingsItems() = listOf(
        SettingsEntity.Theme(current = theme),
        SettingsEntity.Language(current = language),
        SettingsEntity.LocationTracking(enabled = locationTrackingEnabled),
        SettingsEntity.Feedback
    )

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
