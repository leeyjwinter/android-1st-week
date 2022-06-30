package com.example.ch11_jetpack

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.ch11_jetpack.databinding.FragmentTwoBinding
import kotlinx.android.synthetic.main.fragment_two.*


class TwoFragment : Fragment() {

    lateinit var rs: Cursor // lately initialized

    private var _binding: FragmentTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTwoBinding.inflate(inflater, container, false)

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){// permission check
            // if not granted
            // we need to request permission!
            requestPermissions(Array(1){ Manifest.permission.READ_EXTERNAL_STORAGE}, 121)
        }

        listImages()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // onrequestPermissionResult 재정의
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 121 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 121: 위에서 정해준 코드 121
            listImages() // image를 보여주는 함수
        }
    }




    // image를 보여주는 함수 만들기
    // external storage의 모든 이미지 불러오기!

    private fun listImages(){
        // image name colunm만 원함
        var cols = listOf<String>(MediaStore.Images.Thumbnails.DATA).toTypedArray()
        // internal을 원하면, INTERNAL_CONTENT_URI를 선택하면 됨
        rs = requireActivity().contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            cols, null, null, null)!!
       // if(rs?.moveToNext()!!){ // ?: null checking
       //     Toast.makeText(requireActivity().applicationContext, rs?.getString(0), Toast.LENGTH_LONG).show()
       // }


        var gridview = binding.gridview
        gridview.adapter = ImageAdapter(requireActivity().applicationContext)

        // click하면 띄우게
        // click하면 rs가 특정 position으로 이동하고,
        // 이동한 위치의 path의 이미지를 띄운다

        gridview.setOnItemClickListener{ adapterView, view, i, l ->
            rs.moveToPosition(i)
            var path = rs.getString(0) // path 가져옴
            var x = Intent(requireActivity().applicationContext, DisplayImageActivity::class.java)
            x.putExtra("path", path)
            x.putExtra("loc", i)
            //x.putExtra("i", i) // 몇 번째 사진인지 넘기기
            startActivityForResult(x, 100) // 값 넘기기
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // next or prev 버튼 눌렀을 때 다음 사진 보여주게 하는 함수
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if(resultCode==100){
                return
            }
            var move_pos = resultCode // 사진의 새로운 위치
            if(resultCode<0) move_pos = rs.count-1
            else if(resultCode>=rs.count) move_pos=0
            rs.moveToPosition(move_pos)
            var path = rs.getString(0) // path 가져옴
            var x = Intent(requireActivity().applicationContext, DisplayImageActivity::class.java)
            x.putExtra("path", path)
            x.putExtra("loc", move_pos)
            //x.putExtra("i", i) // 몇 번째 사진인지 넘기기
            startActivityForResult(x, 100)
        }
    }

    inner class ImageAdapter: BaseAdapter { // BaseAdapter, alt+enter
        lateinit var context:Context
        constructor(context: Context){
            this.context = context
        }
        override fun getCount(): Int { // how many images?
            Log.d("태그", "확인"+rs.count)
            return rs.count
        }

        override fun getItem(p0: Int): Any {
            return p0
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            // imageview를 만들어서, o0
            var iv = ImageView(context) //iv: imageview
            rs.moveToPosition(p0)
            var path = rs.getString(0) // getstring: name of image
            var bitmap = BitmapFactory.decodeFile(path)
            iv.setImageBitmap(bitmap)
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP)
            val dplen = ConvertDPtoPX(context, 130)
            iv.layoutParams = AbsListView.LayoutParams(dplen, dplen) // 300 by 300 image로 보여줌??
            return iv;
        }

        fun ConvertDPtoPX(context: Context, dp: Int): Int {
            val density = context.resources.displayMetrics.density
            return Math.round(dp.toFloat() * density)
        }

    }
}