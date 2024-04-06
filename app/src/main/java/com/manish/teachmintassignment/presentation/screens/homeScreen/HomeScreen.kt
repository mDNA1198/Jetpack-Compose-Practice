package com.manish.teachmintassignment.presentation.screens.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import com.manish.teachmintassignment.presentation.theme.TeachmintAssignmentTheme
import com.manish.teachmintassignment.utils.handleResponse
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeScreen (navController: NavHostController){
    val context = LocalContext.current

    val homeViewModel = hiltViewModel<HomeViewModel>()

    val homeRepoList = rememberSaveable {
        mutableStateOf<List<GitRepoItem>>(mutableListOf())
    }

    LaunchedEffect(context) {
        homeViewModel.apply {
            launch {

            }
            launch {
                searchRes.collect { res ->
                    res.handleResponse(
                        context,
                        isLoading,
                        res.message,
                        showProgress = true,
                    ) {
                        res.data?.body()?.items?.let { newList ->
                            homeRepoList.value = homeRepoList.value.toMutableList().apply {
                                addAll(newList)
                            }
                        }
                    }
                }
            }
        }

    }

    Column {
        Greeting(name = "test", homeViewModel = homeViewModel)
        GitRepoList(listOfGitRepo = homeRepoList.value)
    }

}

@Composable
fun GitRepoList(listOfGitRepo: List<GitRepoItem>){
    LazyColumn(){
        items(items = listOfGitRepo){ repoItem ->
            GitRepoUIItem(repoItem = repoItem)
        }
    }
}

@Composable
fun GitRepoUIItem(repoItem: GitRepoItem){
    Card {
        Text(text = repoItem.name ?: "")
    }
}

@Composable
fun Greeting(name: String, homeViewModel: HomeViewModel) {

    Button(onClick = {
        homeViewModel.searchRepositories("MVVM Test Test")
        Timber.d("das")
    }) {
        Text(text = "Click TO Search")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TeachmintAssignmentTheme {

    }
}