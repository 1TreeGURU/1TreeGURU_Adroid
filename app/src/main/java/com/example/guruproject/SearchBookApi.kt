package com.example.guruproject

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import okhttp3.*
import org.w3c.dom.Element
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import kotlinx.android.synthetic.main.activity_my_book_list.*
import kotlinx.android.synthetic.main.activity_review_detail.*
import kotlinx.android.synthetic.main.activity_search_book_api.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.guruproject.MyBookListActivity.Companion.DELETE_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_review_write.*


class SearchBookApi : AppCompatActivity() {

    private lateinit var adapter: SearchItemAdapter

    private val searchBookList = arrayListOf<SearchBookItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_book_api)

        btn_searchBookApi.setOnClickListener() {
            if (edtSearchBook.text.isEmpty()) {
                return@setOnClickListener
            }
            val searchMyBookTitle = edtSearchBook.text.toString()

            val mViewItems = ArrayList<SearchBookItem>()
            adapter.setItems(mViewItems) //리사이클러뷰 초기화

            //Api에서 정보얻기
            getSearchBookListFromApi(searchMyBookTitle)
        }




            recyclerView2.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            adapter = SearchItemAdapter(this) {
                //해당 책 아이템 선택 시
                Toast.makeText(this, "${it.title}, ${it.author}이 선택되었습니다.", Toast.LENGTH_LONG)
                    .show()

                //ReviewWriteActivity로 선택된 아이템 정보 넘기기
                val writeIntent = intent

                writeIntent.putExtra(ReviewWriteActivity.SEARCH_PHOTO, it.photo)
                writeIntent.putExtra(ReviewWriteActivity.SEARCH_TITLE, it.title)
                writeIntent.putExtra(ReviewWriteActivity.SEARCH_AUTHOR, it.author)
                setResult(Activity.RESULT_OK, writeIntent)
                finish()
            }

            adapter.setItems(searchBookList)
            recyclerView2.adapter = adapter

    }


    //API에서 정보 얻는 함수
    private fun getSearchBookListFromApi(searchMyBookTitle: String) {
        progressBar2.visibility = View.VISIBLE
        val request = getRequestUrl(searchMyBookTitle)
        val client = okhttp3.OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {


                val body = response.body()?.string()?.byteInputStream()
                val buildFactory = DocumentBuilderFactory.newInstance()
                val docBuilder = buildFactory.newDocumentBuilder()
                val doc = docBuilder.parse(body, null)
                val nList = doc.getElementsByTagName("RECORD")

                for (n in 0 until nList.length) {
                    val element = nList.item(n) as Element

                    val searchNum = getValueFromKey(element, "NUMBER")
                    val bookTitle = getValueFromKey(element, "TITLE")
                    val newTitle = bookTitle.replace("<b>","") //제목에 <b>태그 따라붙는 것 삭제
                    val finalTitle = newTitle.replace("</b>", "")
                    val boolAuthor = getValueFromKey(element, "AUTHOR")
                    val newAuthor = boolAuthor.replace("<b>", "")//저자에 <b>태그 따라붙는 것 삭제
                    val finalAuthor = newAuthor.replace("</b>", "")
                    val publisher = getValueFromKey(element, "PUBLISHER")
                    val pubYear = getValueFromKey(element, "PUBYEAR")
                    var cover_Url = ""
                    if(getValueFromKey(element, "COVER_YN")=="Y"){
                        cover_Url = getValueFromKey(element, "COVER_URL")
                    }else if(getValueFromKey(element, "COVER_YN")=="N"){
                        cover_Url = "https://i.pinimg.com/originals/35/93/89/3593898fdeb3db9221256bd8771b1ec5.png"
                    }
                    adapter.addItem(SearchBookItem(cover_Url,finalTitle,finalAuthor,publisher,pubYear))

                }
                runOnUiThread {

                    adapter.notifyDataSetChanged()
                    progressBar2.visibility = View.GONE
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SearchBookApi, "로딩 실패" + e.message, Toast.LENGTH_LONG).show()
                    progressBar2.visibility = View.GONE
                }
            }
        })
    }

    private fun getRequestUrl(searchMyBookTitle: String) : Request {

        var url = "http://nl.go.kr/kolisnet/openApi/open.php?page=1&search_field1=total_field&per_page=10&sort_ksj%20=SORT_TITLE%20DESC"
        var httpUrl = okhttp3.HttpUrl.parse(url)
            ?.newBuilder()
            ?.addEncodedQueryParameter("value1", searchMyBookTitle)
            ?.build()

        return Request.Builder()
            .url(httpUrl)
            .addHeader("Content-Type",
                "application/x-www-form-urlencoded; text/xml; charset=utf-8")
            .build()
    }

    private fun getValueFromKey(element: Element, key: String) : String {
        return element.getElementsByTagName(key).item(0).firstChild.nodeValue
    }
}
