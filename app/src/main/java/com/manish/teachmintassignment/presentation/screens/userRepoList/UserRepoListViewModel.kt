package com.manish.teachmintassignment.presentation.screens.userRepoList

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
class UserRepoListViewModel  @Inject constructor(private val repository: GitRepository) : ViewModel()  {

    private val _userRepositoryList = MutableSharedFlow<NetworkResult<Response<List<GitRepoItem>>?>>()
    val userRepositoryList: SharedFlow<NetworkResult<Response<List<GitRepoItem>>?>> = _userRepositoryList

    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading: SharedFlow<Boolean> = _isLoading

    val isLoading2 = mutableStateOf(false)

    fun getUserRepoList(userName: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            repository.getRepositoriesOfAUser(userName).collect {
                _userRepositoryList.emit(it)
                _isLoading.emit(false)
            }
        }
    }

}