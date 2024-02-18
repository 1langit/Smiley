package com.example.smiley.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.databinding.ItemContactBinding
import com.example.smiley.models.Dentist

private typealias OnClickDentist = (Dentist) -> Unit

class DentistContactAdapter(
    private val dentistList: List<Dentist>,
    private val onClickDentist: OnClickDentist
): RecyclerView.Adapter<DentistContactAdapter.ItemDentistContactViewHolder>() {

    inner class ItemDentistContactViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dentist: Dentist) {
            with(binding) {
                txtName.text = dentist.name
                txtInfo.text = dentist.city
            }

            itemView.setOnClickListener {
                onClickDentist(dentist)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDentistContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemDentistContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dentistList.size
    }

    override fun onBindViewHolder(holder: ItemDentistContactViewHolder, position: Int) {
        holder.bind(dentistList[position])
    }
}