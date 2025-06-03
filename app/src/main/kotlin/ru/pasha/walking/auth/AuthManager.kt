package ru.pasha.walking.auth

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import ru.pasha.common.di.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class AuthManagerImpl @Inject constructor(
    private val authSdk: YandexAuthSdk
) : AuthManager {
    private val _authResult = MutableSharedFlow<AuthResult>(extraBufferCapacity = 1)
    override val authResult: Flow<AuthResult> get() = _authResult

    private var authLauncher: ActivityResultLauncher<YandexAuthLoginOptions>? = null

    override fun registerLauncher(activity: AppCompatActivity) {
        authLauncher = activity.registerForActivityResult(authSdk.contract) { result ->
            val authResult = when (result) {
                is YandexAuthResult.Success -> AuthResult.Success(result.token.value)
                is YandexAuthResult.Failure -> AuthResult.Error
                YandexAuthResult.Cancelled -> AuthResult.Error
            }
            _authResult.tryEmit(authResult)
        }
    }

    override fun launchAuthScreen(): Flow<Boolean> {
        authLauncher?.launch(YandexAuthLoginOptions())
        return authResult.map { it is AuthResult.Success }
    }
}

interface AuthManager {
    fun launchAuthScreen(): Flow<Boolean>
    fun registerLauncher(activity: AppCompatActivity)
    val authResult: Flow<AuthResult>
}

sealed interface AuthResult {
    data class Success(val token: String) : AuthResult
    data object Error : AuthResult
}
