package br.com.alura.technews.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.technews.R
import br.com.alura.technews.database.AppDatabase
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.repository.NoticiaRepository
import br.com.alura.technews.repository.Resource
import br.com.alura.technews.ui.fragment.extensions.mostraErro
import br.com.alura.technews.ui.recyclerview.adapter.ListaNoticiasAdapter
import br.com.alura.technews.ui.viewmodel.ListaNoticiasViewModel
import br.com.alura.technews.ui.viewmodel.NoticiaViewModelFactory
import kotlinx.android.synthetic.main.lista_noticias.*

private const val MENSAGEM_FALHA_CARREGAR_NOTICIAS = "Não foi possível carregar as novas notícias"

class ListaNoticiasFragment : Fragment() {

    lateinit var fabClicado: () -> Unit
    lateinit var noticiaClicada: (noticia: Noticia) -> Unit

    private val adapter: ListaNoticiasAdapter by lazy {
        context?.let { contexto ->
            ListaNoticiasAdapter(context = contexto)
        } ?: throw IllegalArgumentException("Contexto inválido")

    }

    private val viewModel: ListaNoticiasViewModel by lazy {
        context?.let {
            val repository = NoticiaRepository(AppDatabase.getInstance(context!!).noticiaDAO)
            val factory = NoticiaViewModelFactory(
                ListaNoticiasViewModel::class.java as Class<ViewModel>,
                repository
            )
            ViewModelProvider(
                context as AppCompatActivity,
                factory
            ).get(ListaNoticiasViewModel::class.java)
        } ?: throw IllegalAccessException("Contexto inválido")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaNoticias()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lista_noticias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraFabAdicionaNoticia()
    }

    private fun configuraFabAdicionaNoticia() {
        lista_noticias_fab_salva_noticia.setOnClickListener {
            fabClicado()
        }
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        lista_noticias_recyclerview.addItemDecoration(divisor)
        lista_noticias_recyclerview.adapter = adapter
        configuraAdapter()
    }

    private fun configuraAdapter() {
        adapter.quandoItemClicado = noticiaClicada
    }

    private fun buscaNoticias() {
        viewModel.buscaTodos().observe(this, { resource: Resource<List<Noticia>?> ->
            resource.dado?.let { adapter.atualiza(resource.dado) }
            resource.erro?.let { mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS) }
        })
    }
}