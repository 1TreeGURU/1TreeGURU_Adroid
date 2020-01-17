package com.example.guruproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.guruproject.MyBookListActivity.Companion.REQUEST_CODE_MENU
import kotlinx.android.synthetic.main.activity_review_write.*

class ReviewWriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)

        btn_toSearchBookAct.setOnClickListener(){
            val intentSearch = Intent(this, SearchBookApi::class.java)
            startActivityForResult(intentSearch, 8953)
        }


        //글 쓰기 완료 후 기기내 DB에 저장버튼
        btn_submit.setOnClickListener(){
            val addIntent = intent ?: return@setOnClickListener
            if(editBookTitle.text.isEmpty() || editAuthor.text.isEmpty() || editDate.text.isEmpty() || editPhoto.text.isEmpty() || editContent.text.isEmpty()){
                Toast.makeText(this, "모두 입력하세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            addIntent.putExtra(MyBookListActivity.BOOK_PHOTO, editPhoto.text.toString())
            addIntent.putExtra(MyBookListActivity.BOOK_TITLE, editBookTitle.text.toString())
            addIntent.putExtra(MyBookListActivity.BOOK_AUTHOR, editAuthor.text.toString())
            addIntent.putExtra(MyBookListActivity.BOOK_DATE, editDate.text.toString())
            addIntent.putExtra(MyBookListActivity.BOOK_CONTENT, editContent.text.toString())
            setResult(Activity.RESULT_OK, addIntent)
            finish()

        }
    }
}
