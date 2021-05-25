package br.com.alura.technews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technews.repository.NoticiaRepository

class NoticiaViewModelFactory(
    private val viewModelClass: Class<ViewModel>,
    private val repository: NoticiaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return viewModelClass.getConstructor(NoticiaRepository::class.java)
            .newInstance(repository) as T
    }
}

