package br.com.alura.technews.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.repository.NoticiaRepository
import br.com.alura.technews.repository.Resource

class VisualizaNoticiaViewModel(private val repository: NoticiaRepository) :
    BuscaNoticiaViewModel(repository) {

    fun remove(noticia: Noticia): LiveData<Resource<Void?>> {
        val noticiaLiveData = MutableLiveData<Noticia?>()
        noticiaLiveData.value = noticia
        return noticiaLiveData.value?.run {
            repository.remove(this)
        } ?: MutableLiveData<Resource<Void?>>().also { liveData ->
            liveData.value = Resource(dado = null, erro = "Notícia não encontrada")
        }
    }
}
