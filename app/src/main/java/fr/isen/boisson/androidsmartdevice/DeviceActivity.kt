package fr.isen.boisson.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import fr.isen.boisson.androidsmartdevice.databinding.ActivityDeviceBinding
import java.util.*


@SuppressLint("MissingPermission")
class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")
        val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)
        bluetoothGatt?.connect()
    }


    override fun onStop() {
        super.onStop()
        bluetoothGatt?.close()
    }

    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTING) {
                runOnUiThread {
                    displayContentConnected()
                }
            } else {
                runOnUiThread {
                    displayLED()
                }
            }
        }
    }

    private fun displayContentConnected() {
        binding.detailLoaderTitle.text = getString(R.string.device_led_text)
        binding.detailLoader.isVisible = true
        binding.led1.isVisible = false
        binding.led2.isVisible = false
        binding.led3.isVisible = false
    }

    private fun displayLED() {
        binding.detailLoaderTitle.text = getString(R.string.device_led_text_connected)
        binding.detailLoader.isVisible = false
        binding.led1.isVisible = true
        binding.led2.isVisible = true
        binding.led3.isVisible = true
    }

    private fun clickLED() {
        binding.led1.setOnClickListener {
            turnOnLED1()
        }

        binding.led3.setOnClickListener {
            turnOnLED2()
        }

        binding.led3.setOnClickListener {
            turnOnLED3()
        }
    }

    private fun turnOnLED1() {
        TODO("Not yet implemented")
    }

    private fun turnOnLED2() {
        TODO("Not yet implemented")
    }

    private fun turnOnLED3() {
        TODO("Not yet implemented")
    }
}