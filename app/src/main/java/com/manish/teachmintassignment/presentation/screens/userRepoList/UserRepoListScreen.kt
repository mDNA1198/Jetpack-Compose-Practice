package com.manish.teachmintassignment.presentation.screens.userRepoList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.manish.teachmintassignment.presentation.common.GitRepoList
import com.manish.teachmintassignment.utils.handleResponse
import kotlinx.coroutines.launch
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRepoListScreen (navController: NavHostController, userName: String) {

    val context = LocalContext.current

    val userRepoListViewModel = hiltViewModel<UserRepoListViewModel>()

    val userRepoList = rememberSaveable {
        mutableStateOf<List<GitRepoItem>>(mutableListOf())
    }

    val isDataLoading = rememberSaveable {
        mutableStateOf<Boolean>(true)
    }

    LaunchedEffect(context) {
        Timber.e("UserRepoListScreen userName $userName")

        launch {
            userRepoListViewModel.getUserRepoList(userName)
        }

        userRepoListViewModel.apply {
            launch {
                userRepositoryList.collect{ res ->
                    res.handleResponse(
                        context,
                        isLoading2,
                        res.message,
                        showProgress = true,
                    ) {
                        res.data?.body()?.let { repoList ->
                            userRepoList.value = repoList
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
        Column (Modifier.padding(20.dp)){
            TopAppBar(title = { Text(text = userName) },)
            Spacer(modifier = Modifier.height(10.dp))
            Divider()
            Spacer(modifier = Modifier.height(20.dp))
            if(userRepoList.value.isEmpty()){
                Text(text = "This User Does not have any repositories")
            }else{
                GitRepoList(listOfGitRepo = userRepoList.value, navController = navController) {

                }
            }
        }
    }
}
