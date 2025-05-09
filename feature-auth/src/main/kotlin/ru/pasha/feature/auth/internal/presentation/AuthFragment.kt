package ru.pasha.feature_start_preview.internal.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import ru.pasha.common.BaseFragment
import ru.pasha.common.SideEffect
import ru.pasha.feature_auth.databinding.AuthFragmentBinding

internal class AuthFragment (

): BaseFragment<AuthState, AuthViewModel, AuthFragmentBinding>() {
    override fun render(viewState: AuthState) {
        TODO("Not yet implemented")
    }

    override fun consumeSideEffect(effect: SideEffect) {
        TODO("Not yet implemented")
    }

    override fun createViewModel(): AuthViewModel {
        TODO("Not yet implemented")
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding {
        TODO("Not yet implemented")
    }

    override fun getRoot(): View {
        TODO("Not yet implemented")
    }
}