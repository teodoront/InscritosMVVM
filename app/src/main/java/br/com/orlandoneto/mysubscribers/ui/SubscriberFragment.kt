package br.com.orlandoneto.mysubscribers.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.orlandoneto.mysubscribers.R
import br.com.orlandoneto.mysubscribers.data.db.AppDatabase
import br.com.orlandoneto.mysubscribers.data.db.dao.SubscriberDAO
import br.com.orlandoneto.mysubscribers.extension.hideKeyboard
import br.com.orlandoneto.mysubscribers.repository.DatabaseDataSource
import br.com.orlandoneto.mysubscribers.repository.SubscriberRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.subscriber_fragment.*

class SubscriberFragment : Fragment(R.layout.subscriber_fragment) {


    private val viewModel: SubscriberViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO =
                    AppDatabase.getInstance(requireContext()).subscriberDAO

                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)
                return SubscriberViewModel(repository) as T
            }

        }
    }

    private val args: SubscriberFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.subscriber?.let {
            button_subscriber.text = getString(R.string.subscriber_button_update)
            input_name.setText(it.name)
            input_email.setText(it.email)

            button_delete.visibility = View.VISIBLE
        }

        observeEvents()
        setListeners()
    }

    private fun observeEvents() {

        viewModel.subscriberStateEventData.observe(viewLifecycleOwner) { subscriberState ->

            when (subscriberState) {
                is SubscriberViewModel.SubscriberState.Inserted -> {
                    //Limpar os campos
                    clearFields()
                    //Esconder o teclado
                    hideKeyboard()
                    requireView().requestFocus()

                    //salva a informação, volta p tela anterior e atualiza a recyclerView
                    findNavController().popBackStack()
                }
                is SubscriberViewModel.SubscriberState.Updated -> {
                    clearFields()
                    hideKeyboard()
                    findNavController().popBackStack()
                }
                is SubscriberViewModel.SubscriberState.Deleted -> {
                    clearFields()
                    hideKeyboard()
                    findNavController().popBackStack()
                }
            }

            viewModel.messageEventData.observe(viewLifecycleOwner) { stringResId ->
                Toast.makeText(requireContext(), stringResId, Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun clearFields() {
        input_name.text?.clear()
        input_email.text?.clear()
    }

    private fun hideKeyboard() {
        val parentActivity = requireActivity()
        if (parentActivity is AppCompatActivity) {
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {
        button_subscriber.setOnClickListener {
            val name = input_name.text.toString()
            val email = input_email.text.toString()


            viewModel.addOrUpdateSubscriber(args.subscriber?.id ?: 0, name, email)


        }

        button_delete.setOnClickListener {
            viewModel.removeSubscriber(args.subscriber?.id ?: 0)
        }
    }


}