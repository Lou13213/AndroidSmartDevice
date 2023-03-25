package fr.isen.boisson.androidsmartdevice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class ScanAdapter(private val dataList: List<String>) : RecyclerView.Adapter<fr.isen.boisson.androidsmartdevice.ScanAdapter.ViewHolder>() {


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        /*val textView: TextView = itemView.findViewById(R.id.deviceName)
        val macAddress: TextView = itemView.findViewById(R.id.macAddress)
        val card : ConstraintLayout = itemView.findViewById(R.id.cardDevice)*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.activity_cell_adapter,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
