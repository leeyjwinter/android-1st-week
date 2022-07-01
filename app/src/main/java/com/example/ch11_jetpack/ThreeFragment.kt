package com.example.ch11_jetpack


import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer.OnChronometerTickListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.ch11_jetpack.databinding.FragmentThreeBinding


class ThreeFragment : Fragment() {

    private var _binding: FragmentThreeBinding? = null
    private val binding get() = _binding!!
    var initTime = 0L
    var pauseTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //_binding
        _binding = FragmentThreeBinding.inflate(inflater, container, false)

        binding.chronometer.setOnChronometerTickListener(OnChronometerTickListener { chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - h * 3600000 - m * 60000).toInt() / 1000
            val t =
                (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            chronometer.text = t
        })
        binding.chronometer.setBase(SystemClock.elapsedRealtime())
        binding.chronometer.setText("00:00:00")


        binding.startButton.setOnClickListener{
            binding.chronometer.base = SystemClock.elapsedRealtime() - pauseTime
            binding.chronometer.start()

            binding.stopButton.isEnabled = true
//            binding.resetButton.isEnabled = true
            binding.startButton.isEnabled = false
            binding.stop.isVisible = false
            binding.computer.isVisible = true

        }

        binding.stopButton.setOnClickListener{
            pauseTime = SystemClock.elapsedRealtime()-binding.chronometer.base
//            Log.d("pausetime","${pauseTime}")
//            Log.d("elapsedrealtime","${SystemClock.elapsedRealtime()}")
//            Log.d("binding.chronometer.base","${binding.chronometer.base}")
            binding.chronometer.stop()
            binding.stopButton.isEnabled = false
            binding.startButton.text = "RESUME"
//            binding.resetButton.isEnabled = true
            binding.startButton.isEnabled = true
            binding.stop.isVisible = true
            binding.computer.isVisible = false

        }

//        binding.resetButton.setOnClickListener{
//            pauseTime = 0L
//            binding.chronometer.base = SystemClock.elapsedRealtime()
//            binding.chronometer.stop()
//            binding.stopButton.isEnabled = false
//            binding.resetButton.isEnabled = false
//            binding.startButton.isEnabled = true
//
//        }

        return binding.root
    }


}