package com.manish.teachmintassignment.presentation.screens.repoDetails

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manish.teachmintassignment.data.remote.NetworkResult
import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import com.manish.teachmintassignment.domain.repository.GitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(private val repository: GitRepository) : ViewModel()  {

    private val _repoDetails = MutableSharedFlow<NetworkResult<Response<GitRepoItem>?>>()
    val repoDetails: SharedFlow<NetworkResult<Response<GitRepoItem>?>> = _repoDetails

    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading: SharedFlow<Boolean> = _isLoading

    val isLoading2 = mutableStateOf(false)

    fun getRepoDetails(repoOwner: String, repoName: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            repository.getRepoDetailsByRepoFullName(repoOwner, repoName).collect {
                _repoDetails.emit(it)
                _isLoading.emit(false)
            }
        }
    }

}