package com.example.guru2

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_review_detail.*

class ReviewDetailActivity : AppCompatActivity() {

    private lateinit var category: String

    companion object{
        val REVIEW_TITLE = "title"
        val REVIEW_AUTHOR = "author"
        val REVIEW_PHOTO = "photo"
        val REVIEW_DATE = "date"
        val REVIEW_CONTENT = "content"
    }

    var uploadBook = Diary()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_detail)

        //카테고리 스피너 설정
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent:AdapterView<*>, view:View,
                                        position:Int, id:Long) {
                when(position) {
                    0 -> {
                        category = "none"
                    }
                    1 -> { category = "기쁨을 나누고 싶어"
                    }
                    2 -> { category = "울적함을 달래자"
                    }
                    3 -> { category = "화를 분출할 곳이 필요해"
                    }
                    4 -> { category = "등골이 오싹한 뭐 없을까"
                    }
                    5 -> { category = "커피 한 잔의 여유와 함께"
                    }
                    6 -> { category = "힐링이 필요해"
                    }
                    7 -> { category = "더 멋진 나로 거듭나자"

                    }
                }


            }

            override fun onNothingSelected(parent:AdapterView<*>) {
                Toast.makeText(applicationContext, "카테고리를 선택해주세요", Toast.LENGTH_LONG).show()
            }
        }



        //intent 받아와서 글로 보이기
        val receivedIntent = intent ?:return
        tv_BookTitle.text = receivedIntent.getStringExtra(REVIEW_TITLE)
        tv_Author.text = receivedIntent.getStringExtra(REVIEW_AUTHOR)
        tv_Photo.text = receivedIntent.getStringExtra(REVIEW_PHOTO)
        tv_Date.text = receivedIntent.getStringExtra(REVIEW_DATE)
        tv_Content.text = receivedIntent.getStringExtra(REVIEW_CONTENT)


        //삭제버튼->데이터베이스에서 해당 책 리뷰 삭제
        btn_delete.setOnClickListener(){
            val deleteIntent = intent ?: return@setOnClickListener

            deleteIntent.putExtra(HomeFragment.DELETE_BOOK_TITLE, tv_BookTitle.text.toString())
            deleteIntent.putExtra(HomeFragment.DELETE_BOOK_STATE, true)
            setResult(Activity.RESULT_OK, deleteIntent)
            finish()

        }





        //업로드 진행 프로그레스바 숨김
        progressBar.visibility = View.GONE

        //공유하기 버튼 클릭 시 커뮤니티(파이어베이스 서버)에 나의 책 리뷰 업로드
        btn_upload.setOnClickListener(){
            if(category=="none"){
                Toast.makeText(this, "카테고리를 선택해주세요", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else {
                uploadBook = Diary(
                    tv_Photo.text.toString(),
                    tv_BookTitle.text.toString(),
                    tv_Author.text.toString(),
                    tv_Date.text.toString(),
                    tv_Content.text.toString()
                )
                commUpload()
            }

        }

    }

    //커뮤니티(파이어베이스 서버)에 작성글 공유하는 함수
    private fun commUpload(){
        progressBar.visibility = View.VISIBLE
        var firestore = FirebaseFirestore.getInstance()
        firestore?.collection(category)?.document(uploadBook.title!!)?.set(uploadBook)
            ?.addOnCompleteListener{task->
                progressBar.visibility = View.GONE
                if(task.isSuccessful){
                    Toast.makeText(this, "리뷰가 정상적으로 업로드 되었습니다.", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "리뷰 업로드 실패 : " + task.exception?.message, Toast.LENGTH_LONG).show()
                }

            }
    }
}
