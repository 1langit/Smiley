package com.example.smiley.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.databinding.ItemContactBinding
import com.example.smiley.models.Patient

private typealias OnClickPatient = (Patient) -> Unit

class PatientContactAdapter(
    private val patientList: List<Patient>,
    private val onClickPatient: OnClickPatient
): RecyclerView.Adapter<PatientContactAdapter.ItemPatientContactViewHolder>() {

    inner class ItemPatientContactViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(patient: Patient) {
            with(binding) {
                txtName.text = patient.name
                txtInfo.text = "Patient"
            }

            itemView.setOnClickListener {
                onClickPatient(patient)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPatientContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemPatientContactViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return patientList.size
    }

    override fun onBindViewHolder(holder: ItemPatientContactViewHolder, position: Int) {
        holder.bind(patientList[position])
    }
}