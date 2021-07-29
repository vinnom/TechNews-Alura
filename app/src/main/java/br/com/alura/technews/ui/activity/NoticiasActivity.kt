package br.com.alura.technews.ui.activity

import android.content.Intent
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.geraTransacaoFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment

private const val TAG_FRAGMENT_VISUALIZA_NOTICIA = "visualizaNoticia"

class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_noticias)

        abreListaNoticias(savedInstanceState)
    }

    private fun abreListaNoticias(savedInstanceState: Bundle?) {
        when (savedInstanceState) {
            null -> {
                geraTransacaoFragment {
                    this.add(R.id.noticias_container_primario, ListaNoticiasFragment())
                }
            }
            else -> {
                supportFragmentManager.apply {
                    this.popBackStack()
                    this.findFragmentByTag(TAG_FRAGMENT_VISUALIZA_NOTICIA)?.let { fragment ->
                        val argumentos = fragment.arguments
                        val novoFragment = VisualizaNoticiaFragment()
                        novoFragment.arguments = argumentos

                        geraTransacaoFragment { this.remove(fragment) }

                        geraTransacaoFragment {
                            val container = when (resources.configuration.orientation) {

                                ORIENTATION_LANDSCAPE -> R.id.noticias_container_secundario
                                else -> {
                                    this.addToBackStack(null)
                                    R.id.noticias_container_primario
                                }
                            }

                            this.replace(container, novoFragment, TAG_FRAGMENT_VISUALIZA_NOTICIA)
                        }
                    }
                }
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
            val container = when (resources.configuration.orientation) {
                ORIENTATION_LANDSCAPE -> R.id.noticias_container_secundario
                else -> {
                    this.addToBackStack(null)
                    R.id.noticias_container_primario
                }
            }

            this.replace(container, fragment, TAG_FRAGMENT_VISUALIZA_NOTICIA)
        }
    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }

}
