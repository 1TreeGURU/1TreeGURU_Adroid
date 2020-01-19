package com.example.guru2


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    companion object{
        //ReviewWriteActivity와 연결
        val REQUEST_CODE_MENU = 2230
        //데이터
        val BOOK_PHOTO = "photo"
        val BOOK_TITLE = "title"
        val BOOK_AUTHOR = "author"
        val BOOK_DATE = "date"
        val BOOK_CONTENT = "content"

        //ReviewDetailActivity와 연결
        val DELETE_REQUEST_CODE = 1680
        //데이터
        val DELETE_BOOK_STATE = "false"
        val DELETE_BOOK_TITLE = "delete"
    }

    private lateinit var database: SQLiteDatabase
    private lateinit var adapter: DiaryAdapter //리싸이클러뷰 어댑터
    private lateinit var adapter2: DiaryPagerAdapter //뷰페이저 어댑터

    private val diaryList = arrayListOf<Diary>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //리싸이클러뷰
        btn_main_list.setOnClickListener() {
            viewPager.isVisible = false
            recyclerView.isVisible = true
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = DiaryAdapter(requireContext()) {
            val reviewIntent = Intent(context, ReviewDetailActivity::class.java)

            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_PHOTO, it.photo)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_TITLE, it.title)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_AUTHOR, it.author)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_DATE, it.date)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_CONTENT, it.content)
            startActivityForResult(reviewIntent, DELETE_REQUEST_CODE)


        }
        adapter.setItems(diaryList)
        recyclerView.adapter = adapter

        //뷰페이저 구현
        btn_card_list.setOnClickListener(){
            recyclerView.isVisible = false
            viewPager.isVisible = true
        }

        adapter2 = DiaryPagerAdapter(requireContext(), diaryList)
        viewPager.adapter = adapter2

        //양옆 미리보기 구현
        val density = resources.displayMetrics.density
        //val partialWidth = 16 * density.toInt() // 16dp
        val pageMargin = 8 * density.toInt() // 8dp
        val viewPagerPadding: Int = 170 + pageMargin
        viewPager.pageMargin = pageMargin
        viewPager.setPadding(viewPagerPadding, 0, viewPagerPadding, 0)

        //글쓰기 버튼 클릭
        btn_toWrite.setOnClickListener() {
            val intentToWrite = Intent(this.activity, ReviewWriteActivity::class.java)
            startActivityForResult(intentToWrite, REQUEST_CODE_MENU)
        }

        //DB 구현부
        //DB생성 혹은 열기 함수
        fun openOrCreateUserDB(){
            database = requireContext().openOrCreateDatabase("MyBookDB.db", Context.MODE_PRIVATE, null)
            if(database != null){
                Toast.makeText(context, "나의 책 목록 읽는 중..", Toast.LENGTH_LONG).show()
            }

        }

        //DB테이블 생성 함수
        fun openOrCreateUserTable(){
            if(database != null){
                database.execSQL("create table if not exists MybookTable (_id integer PRIMARY KEY autoincrement, title text, photo text, author text, date text, content text)")
                //Toast.makeText(this, "테이블 생성 완료", Toast.LENGTH_LONG).show()
            }
        }

        //DB 조회 함수
        fun updateDB(){
            //사용자 책DB 조회
            lateinit var cursor: Cursor
            if(database != null) {
                cursor = database.rawQuery("select title, photo, author, date, content from MybookTable", null)
                Toast.makeText(context, "나의 책 리뷰  : " + cursor.getCount() + "개", Toast.LENGTH_LONG).show()


                if(cursor.getCount() == 0){
                    return
                }else {
                    cursor.moveToFirst();
                    while(!cursor.isAfterLast()){

                        var title = cursor.getString(0)
                        var photo =  cursor.getString(1)
                        var author = cursor.getString(2)
                        var date = cursor.getString(3)
                        var content = cursor.getString(4)
                        cursor.moveToNext()

                        //조회된 데이터 리사이클러뷰로 출력
                        adapter.addItem(Diary(photo, title, author, date, content))
                        adapter.notifyDataSetChanged()


                    }
                }
                cursor.close()
            }

        }

        // ****DB불러오기 //

        //사용자 책DB 생성 혹은 열기
        openOrCreateUserDB()

        //사용자 책DB 테이블 생성 혹은 열기
        openOrCreateUserTable()

        //사용자 책 DB 조회
        updateDB()

        // ****DB 끝 //


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
            return
        }
        when (requestCode) {
            //글 작성 activity에서 intent 받을 때
            REQUEST_CODE_MENU -> {
                val photo = data.getStringExtra(BOOK_PHOTO)
                val title = data.getStringExtra(BOOK_TITLE)
                val author = data.getStringExtra(BOOK_AUTHOR)
                val date = data.getStringExtra(BOOK_DATE)
                val content = data.getStringExtra(BOOK_CONTENT)
                //작성한 데이터 DB에 저장하기
                if(database != null){
                    var sql: String = ("insert into MybookTable (title, photo, author, date, content) values (?, ?, ?, ?, ?)")
                    var array = arrayOf(title, photo, author, date, content)
                    database.execSQL(sql, array)
                    Toast.makeText(context, "데이터 저장 완료", Toast.LENGTH_LONG).show()
                }
                //리사이클러뷰로 출력
                adapter.addItem(Diary(photo, title, author, date, content))
                adapter.notifyDataSetChanged()
            }
            //글 삭제 activity에서 intent받을 때
            DELETE_REQUEST_CODE->{
                val deleteState:Boolean = data.getBooleanExtra(DELETE_BOOK_STATE, false)
                if(deleteState){
                    val deleteTitle = data.getStringExtra(DELETE_BOOK_TITLE)
                    database.execSQL("delete from MybookTable where title = '" + deleteTitle + "'")
                    Toast.makeText(context,"삭제가 완료되었습니다", Toast.LENGTH_LONG).show()

                    //데이터 삭제후 리싸이클러 뷰 및 뷰페이저 갱신
                    adapter.notifyDataSetChanged()
                    adapter2.notifyDataSetChanged()

                }else{
                    return
                }

            }


        }
    }

}
