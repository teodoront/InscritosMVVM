package br.com.orlandoneto.mysubscribers.ui.subscriberlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import br.com.orlandoneto.mysubscribers.R
import br.com.orlandoneto.mysubscribers.data.db.AppDatabase
import br.com.orlandoneto.mysubscribers.data.db.dao.SubscriberDAO
import br.com.orlandoneto.mysubscribers.extension.navigateWithAnimations
import br.com.orlandoneto.mysubscribers.repository.DatabaseDataSource
import br.com.orlandoneto.mysubscribers.repository.SubscriberRepository
import kotlinx.android.synthetic.main.subscriber_list_fragment.*

class SubscriberListFragment : Fragment(R.layout.subscriber_list_fragment) {

    private val viewModel: SubscriberListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val subscriberDAO: SubscriberDAO =
                    AppDatabase.getInstance(requireContext()).subscriberDAO

                val repository: SubscriberRepository = DatabaseDataSource(subscriberDAO)
                return SubscriberListViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelEvents()
        configureViewListeners()
    }

    private fun observeViewModelEvents() {
        viewModel.allSubscriberEvent.observe(viewLifecycleOwner) {
            //Lista que está vindo do banco e aplicando no recycler view
            val subscriberListAdapter = SubscriberListAdapter(it).apply {
                onItemClick = { subscriber ->
                    val directions =
                        SubscriberListFragmentDirections.actionSubscriberListFragmentToSubscriberFragment(
                            subscriber
                        )
                    findNavController().navigateWithAnimations(directions)
                }
            }
            with(recycler_subscribers) {
                setHasFixedSize(true)
                adapter = subscriberListAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSubscribers()
    }

    private fun configureViewListeners() {

        fabAddSubscriber.setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_subscriberListFragment_to_subscriberFragment)
        }

    }
}