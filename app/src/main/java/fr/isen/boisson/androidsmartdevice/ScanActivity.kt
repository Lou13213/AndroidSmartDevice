package fr.isen.boisson.androidsmartdevice

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import fr.isen.boisson.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    private val bluetoothAdapter: BluetoothAdapter? by
    lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private var mscanning = false
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: ScanAdapter

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                scanBLEDevices()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (bluetoothAdapter == null) {
            Snackbar.make(
                binding.root,
                "Bluetooth pas pris en charge sur cet appareil",
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            if (bluetoothAdapter?.isEnabled == true) {
                val device = mutableListOf<String>()
                scanDeviceWithPermissions()

                togglePlayPauseAction()

                Toast.makeText(this, "Bluetooth activé", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Bluetooth doit être activé", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStop() {
        super.onStop()
        if (bluetoothAdapter?.isEnabled == true && allPermissionsGranted()) {
            mscanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }

    private fun handleBLENotAvailable() {
        binding.scanTitle.text = getString(R.string.ble_scan_missing)
    }

    private fun scanDeviceWithPermissions() {
        if (allPermissionsGranted()) {
            initToggleActions()
        } else {
            requestPermissionLauncher.launch(getAllPermissions())
        }
    }


    @SuppressLint("MissingPermission")
    private fun scanBLEDevices() {

        if (!mscanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                mscanning = false
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
                togglePlayPauseAction()
            }, SCAN_PERIOD)
            mscanning = true
            bluetoothAdapter?.bluetoothLeScanner?.startScan(leScanCallback)
        } else {
            mscanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
        togglePlayPauseAction()
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.d("Scan", "result : $result")
            adapter.addDevice(result.device)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initToggleActions() {
        binding.scanTitle.setOnClickListener {
            scanBLEDevices()
        }

        binding.scanPause.setOnClickListener {
            scanBLEDevices()
        }

        binding.scanList.layoutManager = LinearLayoutManager(this)
        adapter = ScanAdapter(arrayListOf()) {
            val intent = Intent(this, DeviceActivity::class.java)
            intent.putExtra("device", it)
            startActivity(intent)
        }
        binding.scanList.adapter = adapter
    }

    private fun allPermissionsGranted(): Boolean {
        val allPermissions = getAllPermissions()
        return allPermissions.all {
            when (it) {
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION -> {
                    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
                }

                else -> false
            }
        }
    }

    private fun getAllPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun togglePlayPauseAction() {
        if (mscanning) {
            binding.scanTitle.text = getString(R.string.ble_scan_title_pause)
            binding.scanPause.setImageResource(R.drawable.baseline_play_arrow_24)
            binding.progressBar.isVisible = true
        } else {
            binding.scanTitle.text = getString(R.string.ble_scan_title_play)
            binding.scanPause.setImageResource(R.drawable.baseline_pause_24)
            binding.progressBar.isVisible = false
        }
    }

    companion object {
        private val SCAN_PERIOD: Long = 10000
    }

}


