package com.manish.teachmintassignment.presentation.screens.repoDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import com.manish.teachmintassignment.utils.handleResponse
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailsScreen (navController: NavHostController, gitRepoOwner: String, gitRepoItem: String) {

    val context = LocalContext.current

    val repoDetailsViewModel = hiltViewModel<RepoDetailsViewModel>()

    val repositoryDetails = rememberSaveable {
        mutableStateOf<GitRepoItem>(GitRepoItem())
    }

    val isDataLoading = rememberSaveable {
        mutableStateOf<Boolean>(true)
    }

    LaunchedEffect(context) {
        Timber.e("RepoDetailsScreen gitRepoItem $gitRepoItem")

        launch {
            repoDetailsViewModel.getRepoDetails(gitRepoOwner, gitRepoItem)
        }

        repoDetailsViewModel.apply {
            launch {
                repoDetails.collect{ res ->
                    res.handleResponse(
                        context,
                        isLoading2,
                        res.message,
                        showProgress = true,
                    ) {
                        res.data?.body()?.let { repDetails ->
                            repositoryDetails.value = repDetails
                        }
                    }
                }
            }
            launch {
                isLoading.collect{
                    isDataLoading.value = it
                }
            }
        }

    }

    if(isDataLoading.value){
        Box(Modifier.size(40.dp)) {
            CircularProgressIndicator()
        }
    }else{
        Column {
            MediumTopAppBar(title = {
                Text(text = "${repositoryDetails.value.fullName}")
            })
        }
    }
}