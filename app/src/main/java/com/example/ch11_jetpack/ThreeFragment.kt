package com.example.ch11_jetpack


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.red
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer.OnChronometerTickListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ch11_jetpack.databinding.FragmentThreeBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_three.*
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class ThreeFragment : Fragment() {

    private var _binding: FragmentThreeBinding? = null
    private val binding get() = _binding!!
    private lateinit var playASMR: MediaPlayer
    var initTime = 0L
    var pauseTime = 0L // 양수 누적시간
    var time ="" //출근시
    var min ="" //출근분
    var ampm ="" //출근 AM / PM




    // base값을 저장된 time 기반으로 수정

    ////SharedPreferences
    private val sharedManager: SharedManager by lazy { SharedManager(requireActivity().applicationContext) }

    class ScoreCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {

            val my_time = value.toInt()
            val h = ((my_time / 3600).toInt())
            val m = ((my_time - h * 3600).toInt() / 60)
            val s = ((my_time - h * 3600 - m * 60).toInt() / 1)

            val hh = h.toString().padStart(2, '0')
            val mm = m.toString().padStart(2, '0')
            val ss = s.toString().padStart(2, '0')
            return "$hh" + ":" + "$mm" + ":" + "$ss" + ""

            //val score = value.toString().split(".")
            //return score[0]+"점"
        }
    }


    class DateCustomFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            var rdate = LocalDate.now()
            var ret = rdate.plusDays(value.toLong())
            //return ret.toString()
            return ret.monthValue.toString().padStart(2, '0') + "/" + ret.dayOfMonth.toString().padStart(2, '0')
        }
    }

    fun drawsimpleGraph(d6:Int, d5:Int, d4:Int, d3:Int, d2:Int, d1:Int, d0:Int){ // 그래프를 그려주는 함수
        //fun drawsimpleGraph(){ // 그래프를 그려주는 함수

        val entries: ArrayList<BarEntry> = ArrayList()

        //for(i: Int in 0..6){
        //    entries.add(BarEntry(i.toFloat(), time_table[i].toFloat()))
        //}


        entries.add(BarEntry(-6f, d6.toFloat()))
        entries.add(BarEntry(-5f, d5.toFloat()))
        entries.add(BarEntry(-4f, d4.toFloat()))
        entries.add(BarEntry(-3f, d3.toFloat()))
        entries.add(BarEntry(-2f, d2.toFloat()))
        entries.add(BarEntry(-1f, d1.toFloat()))
        entries.add(BarEntry(0f, d0.toFloat()))


        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val datas = BarData(barDataSet).apply{
            setValueTextSize(10f)
        }
        datas.setValueFormatter(ScoreCustomFormatter())
        binding.barChart.data = datas


        //hide grid lines
        binding.barChart.axisLeft.setDrawGridLines(false)
        binding.barChart.axisLeft.setDrawLabels(false)
        binding.barChart.axisLeft.setDrawAxisLine(false)
        binding.barChart.xAxis.setDrawGridLines(false)
        //binding.barChart.xAxis.setDrawAxisLine(false)
        // 축 레이블 숨기기
        //binding.barChart.xAxis.setDrawLabels(false)
        binding.barChart.xAxis.setValueFormatter(DateCustomFormatter())
        //binding.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.barChart.xAxis.setTextSize(13f)
        binding.barChart.setExtraBottomOffset(10f)

        //binding.barChart.valueTextSize = 10.0f
        //binding.barChart.axisRight.textSize = 30f
        //binding.barChart.getAxis.setTextSize(50f)
        //binding.barChart.getAxis.setDrawLabels(false)

        //remove right y-axis
        binding.barChart.axisRight.isEnabled = false

        //remove legend
        binding.barChart.legend.isEnabled = false


        //remove description label
        binding.barChart.description.isEnabled = false

        //val yAxis: YAxis = binding.barChart.value // get the left or right axis

        //yAxis.valueFormatter =


        //add animation
        //binding.barChart.animateY(3000)


        //draw chart
        binding.barChart.invalidate()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //_binding
        _binding = FragmentThreeBinding.inflate(inflater, container, false)


        // 출근시간을 가져오는 함수
        fun OnClickTime() {
            val timePicker = binding.timePicker
            //timePicker.hour = 5
            //timePicker.minute = 30

            timePicker.setOnTimeChangedListener { _, hour, minute -> var hour = hour
                var am_pm = ""
                // AM_PM decider logic
                when {hour == 0 -> { hour += 12
                    am_pm = "AM"
                }
                    hour == 12 -> am_pm = "PM"
                    hour > 12 -> { hour -= 12
                        am_pm = "PM"
                    }
                    else -> am_pm = "AM"
                }

                time = if (hour < 10) "0" + hour else hour.toString()
                min = if (minute < 10) "0" + minute else minute.toString()
                ampm = am_pm
            }
        }

        //버튼 상태 바꾸는 함수
        fun toggleButtons(){
            if (binding.stopButton.isEnabled == false){
                binding.stopButton.setBackgroundResource(R.drawable.round_gray)
            }
            else{
                binding.stopButton.setBackgroundResource(R.drawable.round)
            }

            if (binding.startButton.isEnabled == false){
                binding.startButton.setBackgroundResource(R.drawable.round_gray)
            }
            else{
                binding.startButton.setBackgroundResource(R.drawable.round)
            }

            if (binding.submitLeaveButton.isEnabled == false){
                binding.submitLeaveButton.setBackgroundResource(R.drawable.round_gray)
            }
            else{
                binding.submitLeaveButton.setBackgroundResource(R.drawable.round)
            }
        }


        //오늘 날짜 시간 가져오기
        var now = LocalDate.now()

        // base 날짜인 2022-06-20 설정하기
        val strdate = "2022-06-28"
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
                Log.d("퇴근??", "O")
                datedb = "112152412141234112153412114512341912333334211111" + "00000000".repeat(994)
            }
            sharedManager.saveCurrentUser(currentUser)
        }
        val currentUser = sharedManager.getCurrentUser()

        var fulldata = currentUser.datedb // 전체 데이터 가지고 있기(String으로)
        //Log.d("시간", "time"+currentUser.datedb)
        //Log.d("서브스트링", currentUser.datedb?.substring(idx*8, idx*8+8) ?: "x")
        pauseTime = (currentUser.datedb?.substring(idx*8, idx*8+8)?.toLong() ?: 0) // 받아서 있으면 빼고, 없으면 0

        ///////////////
        /////////현재 시간을 text로 간단하게 출력해보기!/////////////////////
        fun StringToTime(str_time: String): String{ // 시간을 스트링으로
            val my_time = str_time.toInt()
            val h = ((my_time / 3600000).toInt())
            val m = ((my_time - h * 3600000).toInt() / 60000)
            val s = ((my_time - h * 3600000 - m * 60000).toInt() / 1000)

            val hh = h.toString()
            val mm = m.toString()
            val ss = s.toString()
            return "$hh $mm $ss "
        }

        fun StringToDate(i: Int): String{
            var rdate = LocalDate.now()
            var ret = rdate.plusDays(i.toLong())
            //return ret.toString()
            return ret.monthValue.toString().padStart(2, '0') + "/" + ret.dayOfMonth.toString().padStart(2, '0')
        }


        fun viewWeekTime(w_idx: Int){ // 한 주 시간을 다 보여주기
            var weekTime = ""
            var date_week = ""

            for(i: Int in -6..0) {
                weekTime += "i is $i: "
                weekTime += StringToTime(fulldata!!.substring((w_idx+i)*8, (w_idx+i+1)*8))
                weekTime += "\n"
                date_week += StringToDate(i) + "  "
            }

            //showdate xml
            /*
            <TextView
            android:id="@+id/showDate"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="17dp"
            android:layout_marginTop="-25dp"
            android:text="TextView"
            android:textSize="15sp"
            app:layout_constraintStart_toStartOf="@+id/barChart"
            app:layout_constraintTop_toBottomOf="@+id/barChart" />
            */
            //binding.showDate.setText(date_week)

            // draw 진행해보기! (그래프 그리기)
            var time_table = arrayOf(0, 0, 0, 0, 0, 0, 0)
            for(i: Int in -6..0) {
                time_table[i+6] = (fulldata!!.substring((w_idx+i)*8, (w_idx+i+1)*8).toInt() / 1000)
            }
            drawsimpleGraph(time_table[0], time_table[1], time_table[2], time_table[3], time_table[4], time_table[5], time_table[6])
            //drawsimpleGraph()

            //binding.testTime.text = weekTime
        }

        viewWeekTime(idx)
        /////////////////////////////////////////////////////////

        //binding.chronometer.base = -(저장된 데이터)

        binding.chronometer.setOnChronometerTickListener(OnChronometerTickListener { chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            // 시간 저장
            val currentUser = User().apply {
                var savetimeString = time.toInt().toString().padStart(8, '0')

                var now = LocalDate.now()

                //draw graph!


                Log.d("0625", "time"+fulldata!!.substring((idx-6)*8, (idx-5)*8))
                Log.d("0626", "time"+fulldata!!.substring((idx-5)*8, (idx-4)*8))
                Log.d("0627", "time"+fulldata!!.substring((idx-4)*8, (idx-3)*8))
                Log.d("0628", "time"+fulldata!!.substring((idx-3)*8, (idx-2)*8))
                Log.d("0629", "time"+fulldata!!.substring((idx-2)*8, (idx-1)*8))
                Log.d("0630", "time"+fulldata!!.substring((idx-1)*8, (idx-0)*8))
                Log.d("0701", now.toString()+"time"+fulldata!!.substring((idx-0)*8, (idx+1)*8))
                viewWeekTime(idx)
                if(savetimeString.toLong() > fulldata!!.substring(idx*8, idx*8+8).toLong()){
                    datedb = fulldata!!.substring(0, idx*8) + savetimeString + fulldata!!.substring(idx*8+8, 1000*8) // 원하는 위치에 데이터 저장
                    fulldata = datedb
                }
                else{
                    datedb = fulldata
                }
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


        ////////////////theme 바꾸기//////////////
        var ivImage = binding.background
        var themes = 0
        var soundon = 0

        Glide
            .with(this)
            .load(R.raw.gray_img)
            .centerCrop()
            .into(ivImage)
        ivImage.clipToOutline = true

        fun themechange(){
            if(themes==0){
                Glide
                    .with(this)
                    .load(R.raw.gray_img)
                    .centerCrop()
                    .into(ivImage)
                ivImage.clipToOutline = true
            }
            else if(themes==1){
                Glide
                    .with(this)
                    .load(R.raw.fire_flames)
                    .centerCrop()
                    .into(ivImage)
                ivImage.clipToOutline = true
            }
            else if(themes==2){
                Glide
                    .with(this)
                    .load(R.raw.forest)
                    .centerCrop()
                    .into(ivImage)
                ivImage.clipToOutline = true
            }
            else if(themes==3){
                Glide
                    .with(this)
                    .load(R.raw.rain)
                    .centerCrop()
                    .into(ivImage)
                ivImage.clipToOutline = true
            }
        }

        fun playsound(){
            if(themes==0){
                playASMR = MediaPlayer.create(requireActivity().applicationContext, R.raw.asmr0)
                playASMR.start()
            }
            if(themes==1){
                playASMR = MediaPlayer.create(requireActivity().applicationContext, R.raw.asmr1)
                playASMR.start()
            }
            if(themes==2){
                playASMR = MediaPlayer.create(requireActivity().applicationContext, R.raw.asmr2)
                playASMR.start()
            }
            if(themes==3){
                playASMR = MediaPlayer.create(requireActivity().applicationContext, R.raw.asmr3)
                playASMR.start()
            }
        }

        binding.chronometer.setOnClickListener {
            themes = (themes+1)%4
            themechange()
            if(soundon==1){
                playASMR.stop()
                playsound()
            }
        }

        binding.asmrButton.setOnClickListener {
            soundon = 1-soundon
            if(soundon==1){
                playsound()
                binding.asmrButton.setBackgroundResource(R.drawable.soundon)
            }
            else{
                playASMR.pause()
                binding.asmrButton.setBackgroundResource(R.drawable.soundoff)
            }
        }

        binding.startButton.setOnClickListener{

            var now = LocalDate.now()
            var cidx = ChronoUnit.DAYS.between( basedate , now ).toInt() // 날짜를 계속 갱신!
            if(pauseTime == 0L){ // 내가 오늘 아직 아무것도 하지 않았으면 처음 일하는거니까 지금의 날짜로 바꿈
                idx = cidx
            }

            binding.chronometer.base = SystemClock.elapsedRealtime() - pauseTime
            binding.chronometer.start()

            binding.stopButton.isEnabled = true
            binding.submitLeaveButton.isEnabled = true
            binding.startButton.isEnabled = false
            binding.stop.isVisible = false
            binding.submitLeavePic.isVisible = false
            binding.computer.isVisible = true

            toggleButtons()

        }

        binding.stopButton.setOnClickListener{

            pauseTime = SystemClock.elapsedRealtime()-binding.chronometer.base

            // 날짜가 바뀌었으면 갱신하기!

//            Log.d("pausetime","${pauseTime}")
//            Log.d("elapsedrealtime","${SystemClock.elapsedRealtime()}")
//            Log.d("binding.chronometer.base","${binding.chronometer.base}")
            binding.chronometer.stop()
            binding.stopButton.isEnabled = false


//            binding.stopButton.setBackgroundColor(Color.RED)

//            binding.stopButton.SetBackgroundResource(R.drawable.round_red)
            binding.startButton.text = "계속"
            binding.submitLeaveButton.isEnabled = true
            binding.startButton.isEnabled = true
            binding.stop.isVisible = true
            binding.computer.isVisible = false
            binding.submitLeavePic.isVisible = false

            toggleButtons()

        }


        //////////////////////////image save part
        val mStorageExternalPermissionRequestCode = 131

        fun imageExternalSave(context: Context, bitmap: Bitmap, path: String): Boolean {
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {

                val rootPath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .toString()
                val dirName = "/" + path
                val fileName = System.currentTimeMillis().toString() + ".png"
                val savePath = File(rootPath + dirName)
                println(dirName)
                savePath.mkdirs()


                val file = File(savePath, fileName)
                if (file.exists()) file.delete()

                try {
                    val out = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    out.flush()
                    out.close()

                    //갤러리 갱신
                    context.sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())
                        )
                    )
                    println(Uri.parse("file://" + Environment.getExternalStorageDirectory()))

                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false
        }

        /*
        fun checkPermission(activity: Activty, permission: String): Boolean {
            val permissionChecker =
                ContextCompat.checkSelfPermission(activity.applicationContext, permission)

            //권한이 없으면 권한 요청
            if (permissionChecker == PackageManager.PERMISSION_GRANTED) return true
            ActivityCompat.requestPermissions(activity, arrayOf(permission), mStorageExternalPermissionRequestCode)
            return false
        }*/



        ////////////////////////////////////////////////////////////////////////////////////////////////////


        binding.submitLeaveButton.setOnClickListener{

            binding.stop.isVisible = false
            binding.computer.isVisible = false
            binding.submitLeavePic.isVisible = true

            //ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            //ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)

            //////////saveimagestart///////////////////////
            val rootView = binding.imageView.rootView
            rootView.isDrawingCacheEnabled = true
            rootView.buildDrawingCache(true)
            val b = Bitmap.createBitmap(rootView.drawingCache)
            rootView.isDrawingCacheEnabled = false
            //imageView.setImageBitmap(b)

            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){// permission check
                // if not granted
                // we need to request permission!
                requestPermissions(Array(1){ Manifest.permission.READ_EXTERNAL_STORAGE}, 121)
            }
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){// permission check
                // if not granted
                // we need to request permission!
                requestPermissions(Array(1){ Manifest.permission.WRITE_EXTERNAL_STORAGE}, 131)
            }

            //그림 저장
            if(!imageExternalSave(requireActivity().applicationContext, b, requireActivity().applicationContext.getString(R.string.app_name))){
                Toast.makeText(requireActivity().applicationContext, "그림 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(activity, "그림이 갤러리에 저장되었습니다", Toast.LENGTH_SHORT).show()

            ////////////////saveimageend/////////////////////////

            var now = LocalDate.now()
            var cidx = ChronoUnit.DAYS.between( basedate , now ).toInt() // 날짜를 계속 갱신!
            if(fulldata!!.substring((cidx-0)*8, (cidx+1)*8).equals("00000000")){
                idx = cidx
            }

            // 스크린샷 찍기



            pauseTime = 0L
            binding.chronometer.base = SystemClock.elapsedRealtime()
            binding.chronometer.stop()
            binding.chronometer.setText("00:00:00")
            binding.startButton.text = "시작"
            binding.stopButton.isEnabled = false
            binding.submitLeaveButton.isEnabled = false
            binding.startButton.isEnabled = true


            toggleButtons()



        }



        // OnclickTime 호출

        OnClickTime()
        binding.commuteTimeButton.setOnClickListener {
            println(time)
            println(min)
            println(ampm)
        }



        return binding.root
    }


}