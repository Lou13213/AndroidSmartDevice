package fr.isen.boisson.androidsmartdevice

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.boisson.androidsmartdevice.databinding.ScanCellBinding

class ScanAdapter(var devices: ArrayList<ScanResult>, var onDeviceClickListener: (BluetoothDevice) -> Unit) : RecyclerView.Adapter<ScanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScanCellBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(binding: ScanCellBinding) : RecyclerView.ViewHolder(binding.root) {
        val deviceName = binding.deviceName
        val rssi = binding.RSSI
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deviceName.text = devices[position].device.address
        holder.rssi.text = devices[position].rssi.toString()

        holder.itemView.setOnClickListener {
            onDeviceClickListener(devices[position].device)
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun addDevice(result: ScanResult) {
        var shouldAddDevice = true
        devices.forEachIndexed { index, bluetoothDevice ->
            if (bluetoothDevice.device.address == result.device.address) {
                devices[index] = result
                shouldAddDevice = false
            }
        }
        if (shouldAddDevice) {
            devices.add(result)
        }
    }
}
