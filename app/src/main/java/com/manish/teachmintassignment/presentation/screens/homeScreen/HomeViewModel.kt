package com.manish.teachmintassignment.presentation.screens.homeScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manish.teachmintassignment.data.remote.NetworkResult
import com.manish.teachmintassignment.domain.models.GitRepoSearchResponse
import com.manish.teachmintassignment.domain.repository.GitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: GitRepository) : ViewModel() {

    private val _searchRes = MutableSharedFlow<NetworkResult<Response<GitRepoSearchResponse>?>>()
    val searchRes: SharedFlow<NetworkResult<Response<GitRepoSearchResponse>?>> = _searchRes

    private val _textSearch = MutableStateFlow("")
    private val textSearch: StateFlow<String> = _textSearch.asStateFlow()

    private val _clearList = MutableSharedFlow<Boolean>()
    val clearList: SharedFlow<Boolean> = _clearList

    private var currentPage: Int = 1
    val isLoading = mutableStateOf(false)
    private var totalPages: Int = 1

    private var lastSearchWord: String = ""

    init {
        viewModelScope.launch {
            textSearch.debounce(500).collect { query ->
                searchRepositories(query)
            }
        }
    }

    fun setSearchText(it: String) {
        _textSearch.value = it
    }

    private fun searchRepositories(keyword: String) {
        if(keyword.isEmpty()){
            clearList()
            return
        }
        viewModelScope.launch {

            if(lastSearchWord != keyword){
                clearList()
            }

            lastSearchWord = keyword
            isLoading.value = true

            repository.searchRepositories(keyword, currentPage, 10).collect {
                _searchRes.emit(it)
                processPagination(it.data?.body())
                isLoading.value = false
            }
        }
    }

    fun onScrollEnd(){
        searchRepositories(lastSearchWord)
    }

    private fun clearList(){
        viewModelScope.launch {
            _clearList.emit(true)
            _clearList.emit(false)
            resetPagination()
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