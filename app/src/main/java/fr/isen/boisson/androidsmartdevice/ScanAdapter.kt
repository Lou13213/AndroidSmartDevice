package fr.isen.boisson.androidsmartdevice

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import fr.isen.boisson.androidsmartdevice.databinding.ScanCellBinding

class ScanAdapter(var devices: ArrayList<BluetoothDevice>, var onDeviceClickListener: (BluetoothDevice) -> Unit) : RecyclerView.Adapter<ScanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScanCellBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(binding: ScanCellBinding) : RecyclerView.ViewHolder(binding.root) {
        val deviceName = binding.deviceName
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deviceName.text = devices[position].address
        holder.itemView.setOnClickListener {
            onDeviceClickListener(devices[position])
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun addDevice(device: BluetoothDevice) {
        var shouldAddDevice = true
        devices.forEachIndexed { index, bluetoothDevice ->
            if (bluetoothDevice.address == device.address) {
                devices[index] = device
                shouldAddDevice = false
            }
        }
        if(shouldAddDevice) {
            devices.add(device)
        }
    }
}


/*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.activity_cell_adapter,parent,false)
        return ViewHolder(binding)
    }*/