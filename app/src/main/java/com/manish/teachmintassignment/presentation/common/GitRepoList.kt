package com.manish.teachmintassignment.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import com.manish.teachmintassignment.presentation.Routes
import timber.log.Timber

@Composable
fun GitRepoList(
    listOfGitRepo: List<GitRepoItem>,
    navController: NavHostController?,
    onScrollEnd: () -> Unit,
) {

    LazyColumn {
        items(items = listOfGitRepo) { repoItem ->
            GitRepoUIItem(
                repoItem = repoItem,
                navController = navController
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
fun GitRepoUIItem(repoItem: GitRepoItem, navController: NavHostController?,) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(color = Color.White).clickable {
                Timber.e("GitRepoUIItem clickable fullName = ${repoItem.fullName}")
                repoItem.fullName.let {fullRepoName ->
                    navController?.navigate(
                        Routes.REPO_DETAILS_SCREEN.plus("/$fullRepoName")
                    )
                }
            }
    ) {
        Column(Modifier.padding(15.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "${repoItem.owner?.avatarUrl}.jpeg" ?: "https://imgv3.fotor.com/images/blog-richtext-image/10-profile-picture-ideas-to-make-you-stand-out.jpg",
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 15.dp)
                        .height(30.dp)
                        .width(30.dp)
                )
                Text(text = "${repoItem.owner?.login}")
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "${repoItem.description}", maxLines = 7,)
        }
    }
}