package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technews.R
import br.com.alura.technews.database.AppDatabase
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.repository.NoticiaRepository
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.viewmodel.ListaNoticiasViewModel
import br.com.alura.technews.ui.viewmodel.NoticiaViewModelFactory

private const val TITULO_APPBAR = "Notícias"

class ListaNoticiasActivity : AppCompatActivity() {

    private val viewModel: ListaNoticiasViewModel by lazy {
        val repository = NoticiaRepository(AppDatabase.getInstance(this).noticiaDAO)
        val factory = NoticiaViewModelFactory(
            ListaNoticiasViewModel::class.java as Class<ViewModel>,
            repository
        )
        ViewModelProvider(this, factory).get(ListaNoticiasViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_noticias)
        title = TITULO_APPBAR
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        if (fragment is ListaNoticiasFragment) {
            fragment.fabClicado = { abreFormularioModoCriacao() }
            fragment.noticiaClicada = { noticia ->
                abreVisualizadorNoticia(noticia)
            }
        }
    }


    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    private fun abreVisualizadorNoticia(it: Noticia) {
        val intent = Intent(this, VisualizaNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, it.id)
        startActivity(intent)
    }

}
