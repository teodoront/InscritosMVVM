package br.com.orlandoneto.mysubscribers.ui

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orlandoneto.mysubscribers.R
import br.com.orlandoneto.mysubscribers.repository.SubscriberRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class SubscriberViewModel(
    private val repository: SubscriberRepository
) : ViewModel() {

    private val _subscriberStateEventData = MutableLiveData<SubscriberState>()
    val subscriberStateEventData: LiveData<SubscriberState>
        get() = _subscriberStateEventData

    private val _messageEventData = MutableLiveData<Int>()
    val messageEventData: LiveData<Int>
        get() = _messageEventData

    fun addOrUpdateSubscriber(id: Long = 0, name: String, email: String) {
        if (name == "" || email == "") {
            _subscriberStateEventData.value = SubscriberState.Alert
            _messageEventData.value = R.string.todos_campos_devem_estar_preenchidos
        } else {
            if (id > 0) {
                updatedSubscriber(id, name, email)
            } else {
                insertSubscriber(name, email)
            }
        }
    }

    fun updatedSubscriber(id: Long, name: String, email: String) = viewModelScope.launch {
        try {

            repository.updateSubscriber(id, name, email)
            _subscriberStateEventData.value = SubscriberState.Updated
            _messageEventData.value = R.string.alterado_com_sucesso
        } catch (ex: Exception) {
            _messageEventData.value = R.string.alterado_com_erro
            Log.e(TAG, ex.toString())
        }
    }

    fun insertSubscriber(name: String, email: String) = viewModelScope.launch {
        try {

            val id = repository.insertSubscriber(name, email)

            if (id > 0) {
                _subscriberStateEventData.value = SubscriberState.Inserted
                _messageEventData.value = R.string.inserido_com_sucesso
            }


        } catch (ex: Exception) {
            _messageEventData.value = R.string.inserido_com_erro
            Log.e(TAG, ex.toString())
        }
    }


    fun removeSubscriber(id: Long) = viewModelScope.launch {
        try {
            if (id > 0) {
                repository.deleteSubscriber(id)
                _subscriberStateEventData.value = SubscriberState.Deleted
                _messageEventData.value = R.string.subscriber_deleted_sucessfuly
            }
        } catch (ex: Exception) {
            _messageEventData.value = R.string.deletado_com_sucesso
            Log.e(TAG, ex.toString())

        }
    }


    sealed class SubscriberState {
        object Inserted : SubscriberState()
        object Updated : SubscriberState()
        object Deleted : SubscriberState()
        object Alert: SubscriberState()
    }

    companion object {
        private val TAG = SubscriberViewModel::class.java.simpleName

    }

}