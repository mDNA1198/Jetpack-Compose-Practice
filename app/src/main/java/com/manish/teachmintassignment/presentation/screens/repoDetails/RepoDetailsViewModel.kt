package com.manish.teachmintassignment.presentation.screens.repoDetails

import androidx.lifecycle.ViewModel
import com.manish.teachmintassignment.domain.repository.GitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepoDetailsViewModel @Inject constructor(private val repository: GitRepository) : ViewModel()  {
}