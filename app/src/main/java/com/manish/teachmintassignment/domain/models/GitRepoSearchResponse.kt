package com.manish.teachmintassignment.domain.models

import com.manish.teachmintassignment.domain.enitties.GitRepoItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitRepoSearchResponse (
    @SerialName("total_count")
    var totalCount: Int = 0,

    @SerialName("incomplete_results")
    var incompleteResults: Boolean = false,

    @SerialName("items")
    var items: List<GitRepoItem>,
) : java.io.Serializable