package com.example.ch11_jetpack


import android.content.Context
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
    var pauseTime = 0L // 양수 누적시간




    // base값을 저장된 time 기반으로 수정

    ////SharedPreferences
    private val sharedManager: SharedManager by lazy { SharedManager(requireActivity().applicationContext) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //_binding
        _binding = FragmentThreeBinding.inflate(inflater, container, false)


        val currentUser = sharedManager.getCurrentUser() // current user 받아오기
        Log.d("시간", "time"+currentUser.age)
        pauseTime = (currentUser.age?.toLong() ?: 0) // 받아서 있으면 빼고, 없으면 0

        ///////////////

        //binding.chronometer.base = -(저장된 데이터)

        binding.chronometer.setOnChronometerTickListener(OnChronometerTickListener { chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            // 시간 저장
            val currentUser = User().apply {
                age = time.toInt()
            }
            sharedManager.saveCurrentUser(currentUser)
            //
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - h * 3600000 - m * 60000).toInt() / 1000
            val t =
                (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            chronometer.text = t
        })
        binding.chronometer.setBase(SystemClock.elapsedRealtime())

        val h = (pauseTime / 3600000).toInt()
        val m = (pauseTime - h * 3600000).toInt() / 60000
        val s = (pauseTime - h * 3600000 - m * 60000).toInt() / 1000
        val t =
            (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s

        binding.chronometer.setText(t)


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