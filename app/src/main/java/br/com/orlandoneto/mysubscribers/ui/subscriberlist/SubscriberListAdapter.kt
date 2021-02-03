package br.com.orlandoneto.mysubscribers.ui.subscriberlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.orlandoneto.mysubscribers.R
import br.com.orlandoneto.mysubscribers.data.db.entity.SubscriberEntity
import kotlinx.android.synthetic.main.subscriber_item.view.*

class SubscriberListAdapter(
    private val subscribers: List<SubscriberEntity>
) : RecyclerView.Adapter<SubscriberListAdapter.SubscriberListViewHolder>(){

    //função lambida do Kotlin

    var onItemClick: ((entity: SubscriberEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberListViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.subscriber_item, parent, false)


        return SubscriberListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubscriberListViewHolder, position: Int) {
        holder.bindView(subscribers[position])
    }

    override fun getItemCount() = subscribers.size





   inner class SubscriberListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val textViewSubscriberName: TextView = itemView.text_subscriber_name
        private val textViewSubscriberEmail: TextView = itemView.text_subscriber_email

        fun bindView(subscriber: SubscriberEntity){
            textViewSubscriberName.text = subscriber.name
            textViewSubscriberEmail.text = subscriber.email

            //evento de clique no item da RecyclerView
            itemView.setOnClickListener{
                onItemClick?.invoke(subscriber)
            }
        }

    }
}

