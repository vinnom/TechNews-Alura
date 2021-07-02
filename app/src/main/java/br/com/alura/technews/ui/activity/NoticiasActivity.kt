package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment

private const val TITULO_APPBAR = "Notícias"

class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_noticias)
        title = TITULO_APPBAR

        supportFragmentManager.beginTransaction()
            .add(R.id.noticias_container, ListaNoticiasFragment())
            .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is ListaNoticiasFragment -> {
                fragment.fabClicado = { abreFormularioModoCriacao() }
                fragment.noticiaClicada = { noticia ->
                    abreVisualizadorNoticia(noticia)
                }
            }
            is VisualizaNoticiaFragment -> {
                fragment.abreFormularioEdicao = { noticia ->
                    abreFormularioEdicao(noticia)
                }
                fragment.finaliza = { finish() }
            }
        }
    }

    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    private fun abreVisualizadorNoticia(noticia: Noticia) {
        val fragment = VisualizaNoticiaFragment()
        val dados = Bundle()
        dados.putLong(NOTICIA_ID_CHAVE, noticia.id)
        fragment.arguments = dados

        supportFragmentManager.beginTransaction()
            .replace(R.id.noticias_container, fragment)
            .commit()

    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }

}
