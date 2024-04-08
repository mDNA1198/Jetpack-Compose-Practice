package com.manish.teachmintassignment.presentation.screens.repoDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.manish.teachmintassignment.domain.enitties.ContributorItem
import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import com.manish.teachmintassignment.presentation.Routes
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

    val repositoryContributors = rememberSaveable {
        mutableStateOf<List<ContributorItem>>(mutableListOf())
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
                            repDetails.contributorsUrl?.let {
                                repoDetailsViewModel.getRepoContributorsList(it)
                            }
                        }
                    }
                }
            }
            launch {
                isLoading.collect{
                    isDataLoading.value = it
                }
            }
            launch {
                repoContributorsList.collect{ res ->
                    res.handleResponse(
                        context,
                        isLoading2,
                        res.message,
                        showProgress = true,
                    ) {
                        res.data?.body()?.let { repContriList ->
                            repositoryContributors.value = repContriList
                        }
                    }
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
            TopAppBar(title = { Text(text = repositoryDetails.value.fullName) },)
            AsyncImage(
                model = "${repositoryDetails.value.owner?.avatarUrl}.jpeg",
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(80.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(text = "Project Link:", style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Gray))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "${repositoryDetails.value.url}")
            Spacer(modifier = Modifier.height(25.dp))
            Text(text = "Project Description:", style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Gray))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "${repositoryDetails.value.description}")
            Spacer(modifier = Modifier.height(25.dp))
            Text(text = "Contributors List:", style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Gray))
            Spacer(modifier = Modifier.height(10.dp))
            GitRepoContributorsList(repositoryContributors.value, navController)
        }
    }
}

@Composable
fun GitRepoContributorsList(
    listOfGitRepoContributors: List<ContributorItem>,
    navController: NavHostController?,
) {

    LazyRow {
        items(items = listOfGitRepoContributors) { contributor ->
            AsyncImage(
                model = "${contributor.avatarUrl}.jpeg",
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 15.dp)
                    .size(40.dp).clickable {
                        navController?.navigate(
                            Routes.USER_REPO_LIST_SCREEN.plus("/${contributor.login}")
                        )
                    }
            )
        }
    }
}