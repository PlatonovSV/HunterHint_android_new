package ru.openunity.hunterhint.ui.components

import androidx.annotation.StringRes
import retrofit2.HttpException
import retrofit2.Response
import ru.openunity.hunterhint.R
import java.io.IOException

sealed interface ComponentState

data object ComponentLoading : ComponentState

class ComponentError(@StringRes strResId: Int = R.string.empty) : ComponentState

data object ComponentSuccess : ComponentState

data object ComponentWait : ComponentState
