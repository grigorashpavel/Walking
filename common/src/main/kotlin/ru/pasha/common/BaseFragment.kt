package ru.pasha.common

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<ViewState, ViewModel : BaseViewModel<*, ViewState>, Binding : ViewBinding>(
    private val viewModelClass: Class<BaseViewModel<*, ViewState>>
) : BindingFragment<ViewBinding>(), ViewModelProvider.Factory {
    protected val viewModel: BaseViewModel<*, ViewState> by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, this)[viewModelClass]
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates.onEach(::render).launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.sideEffects.onEach(::consumeSideEffect).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    abstract fun render(viewState: ViewState)
    abstract fun consumeSideEffect(effect: SideEffect)

    protected abstract fun createViewModel(): ViewModel

    final override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return createViewModel() as T
    }
}
