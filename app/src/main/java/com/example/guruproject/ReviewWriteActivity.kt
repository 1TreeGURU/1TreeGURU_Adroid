package com.example.guruproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.guruproject.MyBookListActivity.Companion.BOOK_AUTHOR
import com.example.guruproject.MyBookListActivity.Companion.BOOK_CONTENT
import com.example.guruproject.MyBookListActivity.Companion.BOOK_DATE
import com.example.guruproject.MyBookListActivity.Companion.BOOK_PHOTO
import com.example.guruproject.MyBookListActivity.Companion.BOOK_TITLE
import com.example.guruproject.MyBookListActivity.Companion.DELETE_BOOK_STATE
import com.example.guruproject.MyBookListActivity.Companion.DELETE_BOOK_TITLE
import com.example.guruproject.MyBookListActivity.Companion.DELETE_REQUEST_CODE
import com.example.guruproject.MyBookListActivity.Companion.REQUEST_CODE_MENU
import kotlinx.android.synthetic.main.activity_review_write.*

class ReviewWriteActivity : AppCompatActivity() {
    companion object {
        //SearchBookApi에서 받아온 정보
        val SEARCH_REQUEST_CODE_MENU = 1339
        val SEARCH_TITLE = "title"
        val SEARCH_PHOTO = "photo"
        val SEARCH_AUTHOR = "autor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)



        //책검색 버튼-> api를 통해 검색 후 책 정보(제목, 저자)를 글쓰기 액티비티에 받아옴
        btn_toSearchBookAct.setOnClickListener(){
            val intentSearch = Intent(this, SearchBookApi::class.java)
            startActivityForResult(intentSearch, SEARCH_REQUEST_CODE_MENU)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            return
        }
        when (requestCode) {
            //SearchBookApi에서 intent 받을 때
            SEARCH_REQUEST_CODE_MENU -> {
                val photo = data.getStringExtra(SEARCH_PHOTO)
                val title = data.getStringExtra(SEARCH_TITLE)
                val author = data.getStringExtra(SEARCH_AUTHOR)

                editBookTitle.setText(title)
                editPhoto.setText(photo)
                editAuthor.setText(author)


            }



        }
    }
}
