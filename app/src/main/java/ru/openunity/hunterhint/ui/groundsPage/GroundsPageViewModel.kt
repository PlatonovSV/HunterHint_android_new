package ru.openunity.hunterhint.ui.groundsPage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.comment.CommentRepository
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.data.offer.FindOffersParams
import ru.openunity.hunterhint.data.offer.OfferRepository
import ru.openunity.hunterhint.data.user.UserRepository
import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.Review
import ru.openunity.hunterhint.ui.StateE
import ru.openunity.hunterhint.ui.components.ComponentError
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentSuccess
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GroundsPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groundsRepository: GroundRepository,
    private val offerRepository: OfferRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private var _uiState: MutableStateFlow<GroundsPageUiState> =
        MutableStateFlow(GroundsPageUiState())

    init {
        viewModelScope.launch {
            userRepository.isAuthorized.collect { isAuthorized ->
                _uiState.update {
                    it.copy(
                        isAuthorized = isAuthorized
                    )
                }
            }
        }
    }

    val uiState: StateFlow<GroundsPageUiState> = _uiState.asStateFlow()

    fun onGroundsCardClick(groundId: Int) {
        if (_uiState.value.ground.id != groundId) {
            getGround(groundId)
        } else {
            if (_uiState.value.state != StateE.Success) {
                _uiState.update {
                    it.copy(state = StateE.Success)
                }
            }
        }
    }

    private fun getGround(groundsId: Int) {
        _uiState.update {
            it.copy(
                ground = Ground(id = groundsId)
            )
        }
        downloadReviewList()
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    state = StateE.Loading, offersParams = FindOffersParams(groundsId = groundsId)
                )
            }
            _uiState.update {
                try {
                    it.copy(
                        ground = groundsRepository.getGround(groundsId),
                        numberOfCurrentImage = 0,
                        state = StateE.Success
                    )
                } catch (e: IOException) {
                    Log.e("IOException", e.toString())
                    it.copy(state = StateE.Error)
                } catch (e: HttpException) {
                    Log.e("HttpException", e.message())
                    it.copy(state = StateE.Error)
                }
            }
            _uiState.update {
                try {
                    it.copy(
                        groundsOwner = groundsRepository.getGroundsOwner(_uiState.value.ground.id),
                        state = StateE.Success
                    )
                } catch (e: IOException) {
                    Log.e("IOException", e.toString())
                    it.copy(state = StateE.Error)
                } catch (e: HttpException) {
                    Log.e("HttpException", e.message())
                    it.copy(state = StateE.Error)
                }
            }
            if (_uiState.value.state == StateE.Success) {
                getOffers()
            }
        }
    }

    fun getOffers() {
        _uiState.update {
            it.copy(
                offers = OffersLoading
            )
        }
        viewModelScope.launch {
            val params = _uiState.value.offersParams
            var result: OffersState
            try {
                val offers = offerRepository.findOffers(params.getParams())
                result = OffersSuccess(offers)
            } catch (e: IOException) {
                result = OffersError(R.string.server_error)
            } catch (e: HttpException) {
                result = OffersError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    offers = result
                )
            }
        }
    }


    fun changeImage(increment: Boolean) {
        _uiState.update {
            val lastImage = it.ground.images.lastIndex
            val current = it.numberOfCurrentImage
            it.copy(
                numberOfCurrentImage = when {
                    current < lastImage && increment -> current.inc()
                    current == lastImage && increment -> 0
                    current == 0 -> lastImage
                    else -> current.dec()
                }
            )
        }
    }

    fun downloadReviewList() {
        val groundsId = _uiState.value.ground.id
        viewModelScope.launch {
            _uiState.update {
                it.copy(reviewsListState = ComponentLoading)
            }
            val result = try {
                val reviewIds = commentRepository.getGroundsReview(groundsId)
                val reviews = mutableListOf<Review>()
                for (id in reviewIds) {
                    reviews.add(
                        Review(
                            id = id, state = ComponentLoading
                        )
                    )
                }
                _uiState.update {
                    it.copy(
                        reviews = reviews
                    )
                }
                ComponentSuccess
            } catch (e: IOException) {
                ComponentError(R.string.server_error)
            } catch (e: HttpException) {
                ComponentError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(reviewsListState = result)
            }
            downloadReview()
        }
    }

    fun downloadReview() {
        val reviews = _uiState.value.reviews
        reviews.mapIndexed { index, review ->
            if (review.state !is ComponentSuccess) {
                viewModelScope.launch(Dispatchers.IO) {
                    var newReview: Review = review
                    val result = withContext(Dispatchers.IO) {
                        try {
                            val dto = commentRepository.getReview(review.id)
                            newReview = Review.fromDto(dto)
                            ComponentSuccess
                        } catch (e: IOException) {
                            ComponentError(R.string.server_error)
                        } catch (e: HttpException) {
                            ComponentError(R.string.no_internet)
                        }
                    }
                    newReview = newReview.copy(state = result)
                    _uiState.update { groundsPageUiState ->
                        groundsPageUiState.copy(reviews = groundsPageUiState.reviews.toMutableList()
                            .apply {
                                set(index, newReview)
                            })
                    }
                }
            }
        }
    }

    fun expandComment(id: Long) {
        _uiState.update { groundsPageUiState ->
            groundsPageUiState.copy(reviews = groundsPageUiState.reviews.map {
                if (it.id == id) {
                    it.copy(isExpanded = !it.isExpanded)
                } else {
                    it
                }
            })
        }
    }

    fun changeReviewImage(reviewId: Long, isIncrement: Boolean) {
        _uiState.update { groundsPageUiState ->
            groundsPageUiState.copy(
                reviews = groundsPageUiState.reviews.map {
                    if (it.id == reviewId) {
                        var numberOfCurrentImage = it.numberOfCurrentImage
                        if (isIncrement) {
                            if (numberOfCurrentImage == it.images.size - 1) {
                                numberOfCurrentImage = 0
                            } else {
                                numberOfCurrentImage++
                            }
                        } else {
                            if (numberOfCurrentImage == 0) {
                                numberOfCurrentImage = it.images.size - 1
                            } else {
                                numberOfCurrentImage--
                            }
                        }
                        it.copy(numberOfCurrentImage = numberOfCurrentImage)
                    } else {
                        it
                    }
                }
            )
        }
    }

}