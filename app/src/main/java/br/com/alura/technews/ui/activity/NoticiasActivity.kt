package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.geraTransacaoFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment

class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_noticias)

        abreListaNoticias(savedInstanceState)
    }

    private fun abreListaNoticias(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            geraTransacaoFragment {
                this.add(R.id.noticias_container, ListaNoticiasFragment())
            }
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        when (fragment) {
            is ListaNoticiasFragment -> {
                fragment.fabClicado = this::abreFormularioModoCriacao
                fragment.noticiaClicada = { noticia -> abreVisualizadorNoticia(noticia) }
            }
            is VisualizaNoticiaFragment -> {
                fragment.abreFormularioEdicao = { noticia -> abreFormularioEdicao(noticia) }
                fragment.finaliza = this::finish
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

        geraTransacaoFragment {
            this.addToBackStack(null)
            this.replace(R.id.noticias_container, fragment)
        }
    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }

}
