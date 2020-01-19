package com.example.guru2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.page_item.view.*

class DiaryPagerAdapter(val context: Context, val pages: ArrayList<Diary>): PagerAdapter() {

    override fun getCount(): Int = pages.size

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view.equals(obj)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        //데이터를 불러옴(array)
        val excel = pages[position]
        //카드 아이템 레이아웃을 불러옴
        var view = LayoutInflater.from(context).inflate(R.layout.page_item, container, false)
        //카드 아이템의 뷰를 선언
        var imageView = view.findViewById(R.id.img_book_title) as ImageView
        var titleTextView = view.findViewById(R.id.txt_item_title) as TextView
        var numTextView = view.findViewById(R.id.txt_item_num) as TextView
        var dateTextVIew = view.findViewById(R.id.txt_item_date) as TextView
        var numOfAllTextView = view.findViewById(R.id.txt_num_of_all) as TextView
        //텍스트뷰에 데이터 뿌려줌
        titleTextView.text = excel.title
        numTextView.text = excel.num
        dateTextVIew.text = excel.date
        //현재 가지고있는 페이지갯수중에 몇번째인지를 표시
        numOfAllTextView.text = excel.num + "/" + pages.size

        imageView.setImageResource(R.drawable.ic_launcher_background)

        view.setOnClickListener {
           //내용 보여주기
            val reviewIntent = Intent(context, ReviewDetailActivity::class.java)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_PHOTO, excel.photo)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_TITLE, excel.title)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_AUTHOR, excel.author)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_DATE, excel.date)
            reviewIntent.putExtra(ReviewDetailActivity.REVIEW_CONTENT, excel.content)
            context.startActivity(reviewIntent)
        }

        container.addView(view)
        return view
    }


}