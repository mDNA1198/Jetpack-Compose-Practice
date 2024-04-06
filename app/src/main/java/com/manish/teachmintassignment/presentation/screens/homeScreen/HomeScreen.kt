package com.manish.teachmintassignment.presentation.screens.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import com.manish.teachmintassignment.presentation.theme.TeachmintAssignmentTheme
import com.manish.teachmintassignment.utils.handleResponse
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController?) {
    val context = LocalContext.current

    val homeViewModel = hiltViewModel<HomeViewModel>()

    val homeRepoList = rememberSaveable {
        mutableStateOf<List<GitRepoItem>>(mutableListOf())
    }

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(context) {
        homeViewModel.apply {
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
            launch {
                clearList.collect { clear ->
                    if (clear) {
                        homeRepoList.value = mutableListOf()
                    }
                }
            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(15.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchText,
            onValueChange = {
                searchText = it
                homeViewModel.setSearchText(searchText)
            },
            label = { Text("Search Here") },
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Filled.Search, "search")
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = {
                        searchText = ""
                    }) {
                        Icon(Icons.Filled.Clear, "clear")
                    }
                }
            },
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Search Results")
        Divider()
        Spacer(modifier = Modifier.height(15.dp))

        AnimatedVisibility(
            visible = (searchText.isEmpty() || homeRepoList.value.isEmpty()),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (searchText.isEmpty()) {
                        "Please Input Some Search Text"
                    } else {
                        "No Search Result Found"
                    }
                )
            }
        }

        GitRepoList(listOfGitRepo = homeRepoList.value, onCardClicked = {

        }, onUserClicked = {

        }) {
            homeViewModel.onScrollEnd()
        }

        AnimatedVisibility(visible = homeViewModel.isLoading.value) {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .padding(15.dp)
            ) {
                CircularProgressIndicator(color = Color.Cyan)
            }
        }
    }

}

@Composable
fun GitRepoList(
    listOfGitRepo: List<GitRepoItem>,
    onCardClicked: () -> Unit,
    onUserClicked: () -> Unit,
    onScrollEnd: () -> Unit,
) {

    /* val scrollState = rememberScrollState()

     if (scrollState.value == (scrollState.maxValue - 100)) {
         onScrollEnd.invoke()
     }*/

    LazyColumn {
        items(items = listOfGitRepo) { repoItem ->
            GitRepoUIItem(
                repoItem = repoItem,
                onCardClicked = {
                    onCardClicked.invoke()
                },
                onUserClicked = {
                    onUserClicked.invoke()
                },
            )
        }
        item {
            LaunchedEffect(true) {
                onScrollEnd.invoke()
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GitRepoUIItem(repoItem: GitRepoItem, onCardClicked: () -> Unit, onUserClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(color = Color.White)
    ) {
        Column(Modifier.padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                GlideImage(
                    model = repoItem.owner?.avatarUrl,
                    contentDescription = "user image",
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .height(30.dp)
                        .width(30.dp)
                        .background(color = Color.Red),
                )
                Text(text = "${repoItem.owner?.login}")
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "${repoItem.description}")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TeachmintAssignmentTheme {
        HomeScreen(null)
    }
}