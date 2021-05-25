package br.com.alura.technews.repository

open class Resource<T>(val dado: T?, val erro: String? = null) {}

class SucessoResource<T>(dado: T) : Resource<T>(dado){

    fun atualiza(): Resource<T?>{
        return Resource(dado = dado)
    }
}

class FalhaResource<T>(_dado: T?, _erro: String?) : Resource<T>(_dado, _erro) {

    fun atribuiMensagemErro(): Resource<T?> {
        if (dado != null) {
            return Resource(dado = dado, erro = erro)
        }
        return Resource(dado = null, erro = erro)
    }
}