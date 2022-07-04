package com.example.ch11_jetpack

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.view.View
import com.example.ch11_jetpack.databinding.ActivityMainBinding
import com.example.ch11_jetpack.databinding.PhoneModifyBinding
import kotlinx.android.synthetic.main.activity_display_image.*
import kotlinx.android.synthetic.main.phone_modify.view.*

/*
var x = Intent(requireActivity().applicationContext, ModifyNumberActivity::class.java) // intent 생성
            x.putExtra("path", path) // "key", value
            x.putExtra("loc", move_pos)
            //x.putExtra("i", i) // 몇 번째 사진인지 넘기기
            startActivityForResult(x, 100)
 */
class ModifyNumberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PhoneModifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        binding.modifiedName.hint = "${name}"

        val number = intent.getStringExtra("number")
        binding.modifiedNumber.hint = "${number}"

        val gender = intent.getStringExtra("gender")
        if (gender == "남"){
            binding.MaleRadio.isChecked = true
            binding.FemaleRadio.isChecked = false
            binding.grayMan.visibility = View.VISIBLE
            binding.grayWoman.visibility = View.GONE
            binding.graySchool.visibility = View.GONE
        }

        else if (gender == "여"){
            binding.MaleRadio.isChecked = false
            binding.FemaleRadio.isChecked = true
            binding.grayMan.visibility = View.GONE
            binding.grayWoman.visibility = View.VISIBLE
            binding.graySchool.visibility = View.GONE
        }

        else{
            binding.MaleRadio.isChecked = false
            binding.FemaleRadio.isChecked = false
            binding.grayMan.visibility = View.GONE
            binding.grayWoman.visibility = View.GONE
            binding.graySchool.visibility = View.VISIBLE
        }

        binding.saveButton.setOnClickListener{

        }


    }



}





//{
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_modify_number)
//
//        var path = intent.getStringExtra("path") // intent.getStringExtra: fragment에서 putExtra로 넘긴 값을 꺼내옴
//
//        var loc = intent.getIntExtra("loc", 0) // 꺼내옴
//        next_button.setOnClickListener{
//            setResult(loc+1, intent) // setResult(넘길 정수, intent)
//            finish() //override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { 를 fragment에 호출
//        }
//
//        prev_button.setOnClickListener{
//            setResult(loc-1, intent)
//            finish()
//        }
//
//    }
//    override fun onBackPressed(){
//        setResult(100, intent)
//        finish()
//    }
//}