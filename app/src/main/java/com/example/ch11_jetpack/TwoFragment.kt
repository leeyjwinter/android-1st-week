package com.example.ch11_jetpack

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ch11_jetpack.databinding.FragmentTwoBinding
import kotlinx.android.synthetic.main.fragment_two.*
import java.io.File
import java.net.URI
import java.util.logging.Level.parse
import kotlin.time.Duration.Companion.parse


class TwoFragment : Fragment() {
    var is_mark=0
    lateinit var rs: Cursor // lately initialized

    private var _binding: FragmentTwoBinding? = null
    private val binding get() = _binding!!

    private lateinit var memoryCache: LruCache<String, Bitmap>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //using cache/////////////////////////////
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {

            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.byteCount / 1024
            }
        }
        //////////////////////////////


        //doing binding
        // Inflate the layout for this fragment
        _binding = FragmentTwoBinding.inflate(inflater, container, false)

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){// permission check
            // if not granted
            // we need to request permission!
            requestPermissions(Array(1){ Manifest.permission.READ_EXTERNAL_STORAGE}, 121)
        }

        listImages()

        binding.markButton.setOnClickListener{
            if(is_mark==0){
                is_mark=1
                listImages()
                binding.markButton.text = "전체사진"
            }
            else{
                is_mark=0
                listImages()
                binding.markButton.text = "출근부"
            }

        }

        binding.refreshButton.setOnClickListener{
            listImages()
        }


        binding.refreshLayout.setOnRefreshListener{
            listImages()
            binding.refreshLayout.isRefreshing = false
        }



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


        //val photoUri: Uri = Uri.withAppendedPath(
        //    ,
        //    "/storage/emulated/0/Pictures/1st app"
        //)
        //val photoUri = Uri.fromFile(File("/storage/emulated/0/Pictures/1st app"))

        val selection = "${MediaStore.MediaColumns.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf("Pictures/1stapp%") // Test was my folder name

        //val selection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        //val selectionArgs = arrayOf(MimeTypeMap.getSingleton().getMimeTypeFromExtension("png"))

        //rs = requireActivity().contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        //    cols, null, null, null)!!
        println(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if(is_mark==0){
            rs = requireActivity().contentResolver.query(Uri.parse("content://media/external/images/media"),
                cols, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC")!!
        }
        else{
            rs = requireActivity().contentResolver.query(Uri.parse("content://media/external/images/media"),
                cols, selection, selectionArgs, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC")!!
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // finish()하고 오면, resultCode로 받아올 수 있음
        // next or prev 버튼 눌렀을 때 다음 사진 보여주게 하는 함수
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) { // 내가 어떤 activity를 종료했는가
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
            //Log.d("태그", "확인"+rs.count)
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


            // 이미지를 caching으로 로딩하기
            var bitmap = getBitmapFromMemCache(path) // cache에 bitmap이 있는가?
            if(bitmap != null){ // 있으면
                //Log.d("존재", "O")
                iv.setImageBitmap(bitmap) // 이미 있는 bitmap 로딩
            }
            else{
                //Log.d("없음", "1")
                var decd = BitmapFactory.decodeFile(path) // decoding이 가장 오래걸림!!
                decd = Bitmap.createScaledBitmap(decd, 300, 300, false) // resize해서 캐시에 넣기
                iv.setImageBitmap(decd) // imageview에 집어넣기 (중요)
                addBitmapToMemoryCache(path, decd) // 캐시에 넣기
            }
            ///// 캐싱 전 코드/////////////////
            //var bitmap = BitmapFactory.decodeFile(path)
            //bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false)
            //iv.setImageBitmap(bitmap)
            ////////////////////////////////

            iv.setScaleType(ImageView.ScaleType.CENTER_CROP)
            //val dplen = ConvertDPtoPX(context, 130)

            val wd = (resources.displayMetrics.widthPixels * 0.32).toInt()

            iv.layoutParams = AbsListView.LayoutParams(wd, wd) // 300 by 300 image로 보여줌??
            return iv;
        }

        fun ConvertDPtoPX(context: Context, dp: Int): Int {
            val density = context.resources.displayMetrics.density
            return Math.round(dp.toFloat() * density)
        }


        //cache helper methods
        fun addBitmapToMemoryCache(key: String?, bitmap: Bitmap?) {
            if (getBitmapFromMemCache(key) == null) {
                memoryCache.put(key, bitmap)
            }
        }

        fun getBitmapFromMemCache(key: String?): Bitmap? {
            return memoryCache.get(key)
        }
        ////////////////



    }



}


