package com.example.guru2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: DiaryAdapter

    private lateinit var adapter2: DiaryPagerAdapter

    private val diaryList = arrayListOf<Page>(
        Page("엽서의 제목", "2020.01.03", "1", "ic_launcher_background"),
        Page("엽서2", "2020.01.15", "2", "ic_launcher_background") ,
        Page("엽서3", "2020.01.16", "3", "ic_launcher_background")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DiaryAdapter(this){
            Toast.makeText(this, "휴..", Toast.LENGTH_LONG).show()
        }
        adapter.setItems(diaryList)
        recyclerView.adapter=adapter

        //뷰페이저 구현
        adapter2 = DiaryPagerAdapter(this, diaryList)
        viewPager.adapter = adapter2

        //양옆 미리보기 구현
        val density = resources.displayMetrics.density
        val partialWidth = 16 * density.toInt() // 16dp
        val pageMargin = 8 * density.toInt() // 8dp
        val viewPagerPadding: Int = 150 + pageMargin
        viewPager.pageMargin = pageMargin
        viewPager.setPadding(viewPagerPadding, 0, viewPagerPadding, 0)
    }
}
