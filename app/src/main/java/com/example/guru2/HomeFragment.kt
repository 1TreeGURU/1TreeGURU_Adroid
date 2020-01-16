package com.example.guru2


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var adapter: DiaryAdapter

    private lateinit var adapter2: DiaryPagerAdapter

    private val diaryList = arrayListOf<Page>(
        Page("엽서의 제목", "2020.01.03", "1", "ic_launcher_background"),
        Page("엽서2", "2020.01.15", "2", "ic_launcher_background") ,
        Page("엽서3", "2020.01.16", "3", "ic_launcher_background")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = DiaryAdapter(requireContext()){
        }
        adapter.setItems(diaryList)
        recyclerView.adapter=adapter

        //뷰페이저 구현
        adapter2 = DiaryPagerAdapter(requireContext(), diaryList)
        viewPager.adapter = adapter2

        //양옆 미리보기 구현
        val density = resources.displayMetrics.density
        val partialWidth = 16 * density.toInt() // 16dp
        val pageMargin = 8 * density.toInt() // 8dp
        val viewPagerPadding: Int = 170 + pageMargin
        viewPager.pageMargin = pageMargin
        viewPager.setPadding(viewPagerPadding, 0, viewPagerPadding, 0)
    }

}
