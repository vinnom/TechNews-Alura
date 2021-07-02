package br.com.alura.technews.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.alura.technews.R
import br.com.alura.technews.database.AppDatabase
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.repository.NoticiaRepository
import br.com.alura.technews.ui.activity.NOTICIA_ID_CHAVE
import br.com.alura.technews.ui.fragment.extensions.mostraErro
import br.com.alura.technews.ui.viewmodel.NoticiaViewModelFactory
import br.com.alura.technews.ui.viewmodel.VisualizaNoticiaViewModel
import kotlinx.android.synthetic.main.visualiza_noticias.*

private const val NOTICIA_NAO_ENCONTRADA = "Notícia não encontrada"
private const val MENSAGEM_FALHA_REMOCAO = "Não foi possível remover notícia"

class VisualizaNoticiaFragment : Fragment() {

    lateinit var finaliza: () -> Unit
    lateinit var abreFormularioEdicao: (noticia: Noticia) -> Unit


    private val noticiaId: Long by lazy {
        arguments?.getLong(NOTICIA_ID_CHAVE)
            ?: throw IllegalArgumentException("Id inválido!")
    }

    private val viewModel by lazy {
        context?.let {
            val repository = NoticiaRepository(AppDatabase.getInstance(context!!).noticiaDAO)
            val factory = NoticiaViewModelFactory(
                VisualizaNoticiaViewModel::class.java as Class<ViewModel>,
                repository
            )
            ViewModelProvider(
                context as AppCompatActivity,
                factory
            ).get(VisualizaNoticiaViewModel::class.java)
        } ?: throw IllegalArgumentException("Contexto inválido!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.visualiza_noticias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscaNoticiaSelecionada()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.visualiza_noticia_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.visualiza_noticia_menu_edita -> {
                viewModel.buscaPorId(noticiaId).observe(this, { noticia ->
                    if (noticia != null) {
                        abreFormularioEdicao(noticia)
                    }
                })
            }
            R.id.visualiza_noticia_menu_remove -> remove()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun buscaNoticiaSelecionada() {
        viewModel.buscaPorId(noticiaId).observe(this, { noticia ->
            if (noticia != null) {
                preencheCampos(noticia)
            } else {
                verificaIdDaNoticia()
            }
        })
    }

    private fun verificaIdDaNoticia() {
        if (noticiaId == 0L) {
            mostraErro(NOTICIA_NAO_ENCONTRADA)
            finaliza
        }
    }

    private fun preencheCampos(noticia: Noticia) {
        visualiza_noticia_titulo.text = noticia.titulo
        visualiza_noticia_texto.text = noticia.texto
    }

    private fun remove() {
        viewModel.buscaPorId(noticiaId).observe(this, { noticia ->
            if (noticia != null) {
                viewModel.remove(noticia).observe(this, { resource ->
                    if (resource.erro == null) {
                        finaliza
                    } else {
                        mostraErro(MENSAGEM_FALHA_REMOCAO)
                    }
                })
            }
        })

    }


}