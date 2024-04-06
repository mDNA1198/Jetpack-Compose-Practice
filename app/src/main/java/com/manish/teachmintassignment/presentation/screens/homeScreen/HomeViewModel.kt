package com.manish.teachmintassignment.presentation.screens.homeScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manish.teachmintassignment.data.remote.NetworkResult
import com.manish.teachmintassignment.domain.models.GitRepoSearchResponse
import com.manish.teachmintassignment.domain.repository.GitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: GitRepository) : ViewModel() {


    private val _searchRes = MutableSharedFlow<NetworkResult<Response<GitRepoSearchResponse>?>>()
    val searchRes: SharedFlow<NetworkResult<Response<GitRepoSearchResponse>?>> = _searchRes

    private var currentPage: Int = 1
    val isLoading = mutableStateOf(false)
    private var totalPages: Int = 1

    private var lastSearchWord: String = ""

    fun searchRepositories(keyword: String) {
        viewModelScope.launch {
            if(lastSearchWord != keyword){
                resetPagination()
            }
            isLoading.value = true
            repository.searchRepositories(keyword, currentPage, 10).collect {
                processPagination(it.data?.body())
                isLoading.value = false
                _searchRes.emit(it)
            }
        }
    }

    private fun processPagination(response: GitRepoSearchResponse?){
        val totalCount = response?.totalCount
        totalCount?.let {
            totalPages = totalCount / 10
            if(currentPage != totalCount) currentPage++
        }
    }

    private fun resetPagination(){
        currentPage = 1
        totalPages = 1
    }

}