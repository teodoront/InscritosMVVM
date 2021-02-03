package br.com.orlandoneto.mysubscribers.repository

import androidx.lifecycle.LiveData
import br.com.orlandoneto.mysubscribers.data.db.entity.SubscriberEntity

interface SubscriberRepository {
    //vai fazer a ligação da ViewModel com o banco de dados
    //quando trabalhar com coroutines sempre usar suspend nas funções

    suspend fun insertSubscriber(name: String, email: String): Long

    suspend fun updateSubscriber(id: Long, name: String, email: String)

    suspend fun deleteSubscriber(id: Long)

    suspend fun deleteAllSubscribers()

    suspend fun getAllSubscribers(): List<SubscriberEntity>
}