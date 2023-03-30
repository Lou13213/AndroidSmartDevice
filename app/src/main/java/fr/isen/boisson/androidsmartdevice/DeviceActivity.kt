package fr.isen.boisson.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import fr.isen.boisson.androidsmartdevice.databinding.ActivityDeviceBinding
import java.util.*


@SuppressLint("MissingPermission")
class DeviceActivity : AppCompatActivity() {

    private var LED1ON = false
    private var LED2ON = false
    private var LED3ON = false
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
        binding.nbIncrementation.isVisible = false
    }

    private fun displayLED() {
        binding.detailLoaderTitle.text = getString(R.string.device_led_text_connected)
        binding.detailLoader.isVisible = false
        binding.led1.isVisible = true
        binding.led2.isVisible = true
        binding.led3.isVisible = true
        binding.nbIncrementation.isVisible = true
        clickLED()
    }

    private fun clickLED() {
        binding.led1.setOnClickListener {
            LED1ON = !LED1ON
            Log.d("LED1",LED1ON.toString())
            turnOnLED(1)
        }

        binding.led2.setOnClickListener {
            LED2ON = !LED2ON
            turnOnLED(2)
        }

        binding.led3.setOnClickListener {
            LED3ON = !LED3ON
            turnOnLED(3)
        }
    }

    private fun turnOnLED(ledNumber: Int) {
        val imageView = when (ledNumber) {
            1 -> binding.led1
            2 -> binding.led2
            3 -> binding.led3
            else -> return
        }

        val imageResource = if (when (ledNumber) {
                1 -> LED1ON
                2 -> LED2ON
                3 -> LED3ON
                else -> false
            }) {
            R.drawable.ledon
        } else {
            R.drawable.ledoff
        }

        imageView.setImageResource(imageResource)
    }
}