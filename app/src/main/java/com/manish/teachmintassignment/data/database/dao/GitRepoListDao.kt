package com.manish.teachmintassignment.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.manish.teachmintassignment.domain.enitties.GitRepoItem

@Dao
interface GitRepoListDao {

    @Insert
    suspend fun insertGitRepoList(cachedBrandsList: List<GitRepoItem>)

    @Query("SELECT * FROM git_repo_items")
    suspend fun getGitRepoList(): List<GitRepoItem>
}