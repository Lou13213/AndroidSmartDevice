package fr.isen.boisson.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import fr.isen.boisson.androidsmartdevice.databinding.ActivityDeviceBinding
import java.time.chrono.JapaneseEra.values
import java.util.*


@SuppressLint("MissingPermission")
class DeviceActivity : AppCompatActivity() {

    private var LED1ON = false
    private var LED2ON = false
    private var LED3ON = false
    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null

    private val serviceUUID = UUID.fromString("0000feed-cc7a-482a-984a-7f2ed5b3e58f")
    private val characteristicLedUUID = UUID.fromString("0000abcd-8e22-4541-9d4c-21edae82ed19")
    private val characteristicButtonUUID = UUID.fromString("00001234-8e22-4541-9d4c-21edae82ed19")

    private var clickCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")
        val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)
        bluetoothGatt?.connect()
        binding.clickCount.text=clickCount.toString()
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
        binding.clickCount.isVisible = false
    }

    private fun displayLED() {
        binding.detailLoaderTitle.text = getString(R.string.device_led_text_connected)
        binding.detailLoader.isVisible = false
        binding.led1.isVisible = true
        binding.led2.isVisible = true
        binding.led3.isVisible = true
        binding.nbIncrementation.isVisible = true
        binding.clickCount.isVisible = true
        clickLED()
    }

    private fun clickLED() {

        val service = bluetoothGatt?.getService(serviceUUID)
        val characteristicLedUUID = service?.getCharacteristic(characteristicLedUUID)

        binding.led1.setOnClickListener {
            updateCount()
            LED1ON = !LED1ON
            Log.d("LED1",LED1ON.toString())
            turnOnLED(1)

            if(LED1ON){
                Toast.makeText(this, "LED 1 activée", Toast.LENGTH_LONG).show()
                characteristicLedUUID?.value=(byteArrayOf(0x01))
                bluetoothGatt?.writeCharacteristic(characteristicLedUUID)}
            else{
                Toast.makeText(this, "LED 1 éteinte", Toast.LENGTH_LONG).show()
                characteristicLedUUID?.value=(byteArrayOf(0x00))
                bluetoothGatt?.writeCharacteristic(characteristicLedUUID)
            }
        }

        binding.led2.setOnClickListener {
            updateCount()
            LED2ON = !LED2ON
            turnOnLED(2)
            if(LED2ON){
                Toast.makeText(this, "LED 2 activée", Toast.LENGTH_LONG).show()
                characteristicLedUUID?.value=(byteArrayOf(0x02))
                bluetoothGatt?.writeCharacteristic(characteristicLedUUID)}
            else{
                Toast.makeText(this, "LED 2 éteinte", Toast.LENGTH_LONG).show()
                characteristicLedUUID?.value=(byteArrayOf(0x00))
                bluetoothGatt?.writeCharacteristic(characteristicLedUUID)}
        }

        binding.led3.setOnClickListener {
            updateCount()
            LED3ON = !LED3ON
            turnOnLED(3)
            if(LED3ON){
                Toast.makeText(this, "LED 3 activée", Toast.LENGTH_LONG).show()
            characteristicLedUUID?.value=(byteArrayOf(0x03))
            bluetoothGatt?.writeCharacteristic(characteristicLedUUID)
            }
            else{
                Toast.makeText(this, "LED 3 éteinte", Toast.LENGTH_LONG).show()
                characteristicLedUUID?.value=(byteArrayOf(0x00))
                bluetoothGatt?.writeCharacteristic(characteristicLedUUID)}
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

    private fun updateCount(){
        clickCount++
        binding.clickCount.text=clickCount.toString()
    }
}