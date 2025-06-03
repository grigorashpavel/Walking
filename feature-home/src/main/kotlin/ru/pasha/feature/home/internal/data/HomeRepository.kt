package ru.pasha.feature.home.internal.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.pasha.feature.home.internal.di.HomeScope
import ru.pasha.network.api.handleApiResponse
import java.util.UUID
import javax.inject.Inject

@HomeScope
internal class HomeRepository @Inject constructor(
    private val homeApi: HomeApi,
) {

    suspend fun reportRouteFeedback(message: String, rating: Int) = withContext(Dispatchers.IO) {
        return@withContext handleApiResponse<FeedbackSuccessData> {
            val request = FeedbackV1Request(message = message, rating = rating)
            homeApi.report(idempotencyKey = UUID.randomUUID(), request = request)
        }
    }
}
