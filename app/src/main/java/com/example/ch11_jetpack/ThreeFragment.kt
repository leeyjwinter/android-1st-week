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
import java.time.LocalDate
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


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


        //오늘 날짜 시간 가져오기
        var now = LocalDate.now()

        // base 날짜인 2022-06-20 설정하기
        val strdate = "2022-06-25"
        val basedate = LocalDate.parse(strdate, DateTimeFormatter.ISO_DATE) // localdate형식으로 변환
        //Log.d("오늘 날짜", "go"+ ChronoUnit.DAYS.between( basedate , now ))
        //Log.d("날짜", "00000000".repeat(1000))
        var idx = ChronoUnit.DAYS.between( basedate , now ).toInt() // idx: 오늘 날짜 기준 index

        val curRentUser = sharedManager.getCurrentUser() // current user 받아오기
        // 아직 한 번도 User가 안 생겼을 때 초기화하기
        if(curRentUser.datedb == ""){
            val currentUser = User().apply {
                //datedb = "00000000".repeat(1000)
                //임시로 만든 0625~0630 데이터
                datedb = "000152410001234100153412004512340012333300011111" + "00000000".repeat(994)
            }
            sharedManager.saveCurrentUser(currentUser)
        }
        val currentUser = sharedManager.getCurrentUser()

        var fulldata = currentUser.datedb // 전체 데이터 가지고 있기(String으로)
        //Log.d("시간", "time"+currentUser.datedb)
        //Log.d("서브스트링", currentUser.datedb?.substring(idx*8, idx*8+8) ?: "x")
        pauseTime = (currentUser.datedb?.substring(idx*8, idx*8+8)?.toLong() ?: 0) // 받아서 있으면 빼고, 없으면 0

        ///////////////

        //binding.chronometer.base = -(저장된 데이터)

        binding.chronometer.setOnChronometerTickListener(OnChronometerTickListener { chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            // 시간 저장
            val currentUser = User().apply {
                var savetimeString = time.toInt().toString().padStart(8, '0')

                var now = LocalDate.now()

                Log.d("0625", "time"+fulldata!!.substring((idx-6)*8, (idx-5)*8))
                Log.d("0626", "time"+fulldata!!.substring((idx-5)*8, (idx-4)*8))
                Log.d("0627", "time"+fulldata!!.substring((idx-4)*8, (idx-3)*8))
                Log.d("0628", "time"+fulldata!!.substring((idx-3)*8, (idx-2)*8))
                Log.d("0629", "time"+fulldata!!.substring((idx-2)*8, (idx-1)*8))
                Log.d("0630", "time"+fulldata!!.substring((idx-1)*8, (idx-0)*8))
                Log.d("0701", now.toString()+"time"+fulldata!!.substring((idx-0)*8, (idx+1)*8))



                datedb = fulldata!!.substring(0, idx*8) + savetimeString + fulldata!!.substring(idx*8+8, 1000*8) // 원하는 위치에 데이터 저장
                fulldata = datedb
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

            // 날짜가 바뀌었으면 갱신하기!
            var now = LocalDate.now()
            var cidx = ChronoUnit.DAYS.between( basedate , now ).toInt() // 날짜를 계속 갱신!
            if(fulldata!!.substring((cidx-0)*8, (cidx+1)*8).equals("00000000")){
                pauseTime = 0
                idx = cidx
            }



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