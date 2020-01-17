package com.example.guruproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_book_list.*


class MyBookListActivity : AppCompatActivity() {
    companion object {
        //ReviewWriteActivity와 연결
        val REQUEST_CODE_MENU = 2230
        val BOOK_PHOTO = "photo"
        val BOOK_TITLE = "title"
        val BOOK_AUTHOR = "author"
        val BOOK_DATE = "date"
        val BOOK_CONTENT = "content"
        //ReviewDetailActivity와 연결
        val DELETE_BOOK_STATE = "false"
        val DELETE_REQUEST_CODE = 1680
        val DELETE_BOOK_TITLE = "delete"
    }


    private lateinit var database: SQLiteDatabase

    private lateinit var adapter: BookItemAdapter

    private val bookList = arrayListOf<BookItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_book_list)

        //리사이클러뷰 내 리스트 아이템 클릭 시 해당 글 자세히 보기
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = BookItemAdapter(this) {
            val reviewIntent = Intent(this, ReviewDetailActivity::class.java)

            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_PHOTO, it.photo)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_TITLE, it.title)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_AUTHOR, it.author)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_DATE, it.date)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_CONTENT, it.content)
            startActivityForResult(reviewIntent, DELETE_REQUEST_CODE)


        }
        adapter.setItems(bookList)
        recyclerView.adapter = adapter

        // ****DB불러오기 //

        //사용자 책DB 생성 혹은 열기
        openOrCreateUserDB()

        //사용자 책DB 테이블 생성 혹은 열기
        openOrCreateUserTable()

        //사용자 책 DB 조회
        updateDB()

        // ****DB 끝 //


        //글쓰기 화면 가기
        btn_toWrite.setOnClickListener() {
            val intentToWrite = Intent(this, ReviewWriteActivity::class.java)
            startActivityForResult(intentToWrite, REQUEST_CODE_MENU)
        }

    }

    //DB생성 혹은 열기 함수
    fun openOrCreateUserDB(){
        database = openOrCreateDatabase("MyBookDB.db", Context.MODE_PRIVATE, null)
        if(database != null){
            Toast.makeText(this, "나의 책 목록 읽는 중..", Toast.LENGTH_LONG).show()
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
            Toast.makeText(this, "나의 책 리뷰  : " + cursor.getCount() + "개", Toast.LENGTH_LONG).show()


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
                    adapter.addItem(BookItem(photo, title, author, date, content))
                    adapter.notifyDataSetChanged()


                }
            }
            cursor.close()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "데이터 저장 완료", Toast.LENGTH_LONG).show()
                }
                //리사이클러뷰로 출력
                adapter.addItem(BookItem(photo, title, author, date, content))
                adapter.notifyDataSetChanged()
            }
            //글 삭제 activity에서 intent받을 때
            DELETE_REQUEST_CODE->{
                val deleteState:Boolean = data.getBooleanExtra(DELETE_BOOK_STATE, false)
                if(deleteState){
                    val deleteTitle = data.getStringExtra(DELETE_BOOK_TITLE)
                    database.execSQL("delete from MybookTable where title = '" + deleteTitle + "'")
                    Toast.makeText(this,"삭제가 완료되었습니다", Toast.LENGTH_LONG).show()
                    //데이터 삭제 후 현재 액티비티 갱신
                    val intent = intent
                    finish()
                    startActivity(intent)
                }else{
                    return
                }

            }
        }
    }
}
