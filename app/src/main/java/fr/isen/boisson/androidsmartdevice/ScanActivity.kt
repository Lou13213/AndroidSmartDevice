package fr.isen.boisson.androidsmartdevice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.boisson.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    private var mScanning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.scanTitle.setOnClickListener {
            togglePlayPauseAction()
        }

        binding.scanPause.setOnClickListener {
            togglePlayPauseAction()
        }
        
        binding.scanList.layoutManager = LinearLayoutManager(this)
        binding.scanList.adapter = ScanAdapter(arrayListOf("Device 1", "Device 2"))
    }

    private fun togglePlayPauseAction() {
        mScanning = !mScanning

        if (mScanning) {
            binding.scanTitle.text = getString(R.string.ble_scan_title_pause)
            binding.scanPause.setImageResource(R.drawable.baseline_play_arrow_24)
            binding.progressBar.isVisible = true
        }
        else {
            binding.scanTitle.text = getString(R.string.ble_scan_title_play)
            binding.scanPause.setImageResource(R.drawable.baseline_pause_24)
            binding.progressBar.isVisible = false
        }
    }
}
