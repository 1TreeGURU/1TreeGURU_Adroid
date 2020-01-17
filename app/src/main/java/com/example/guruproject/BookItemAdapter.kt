package com.example.guruproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_review_detail.view.*
import kotlinx.android.synthetic.main.book_list_item.view.*
import kotlinx.android.synthetic.main.search_list_item.view.*

class BookItemAdapter (val context: Context, val itemCheck: (BookItem) -> Unit)
    : RecyclerView.Adapter<BookItemAdapter.ViewHolder>() {
    private var items = ArrayList<BookItem>()
    private lateinit var itemClickListener: ItemClickListener //클릭리스너 선언

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflater.inflate(R.layout.book_list_item, viewGroup, false)

        return ViewHolder(itemView, itemCheck)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: BookItem = items[position]

        viewHolder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setItems(items: ArrayList<BookItem>) {
        this.items = items
    }

    fun addItem(item: BookItem){
        items.add(item)
    }


    interface ItemClickListener{
        fun onClick(view: View, position: Int)
    }


    inner class ViewHolder(itemView: View, itemCheck: (BookItem) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: BookItem) {
            val resourceId = context.resources.getIdentifier(
                item.photo,
                "drawable",
                context.packageName
            )
            if (resourceId in 0..1) {
                itemView.img_book?.setImageResource(R.mipmap.ic_launcher)
            }
            else {
                itemView.img_book?.setImageResource(resourceId)
            }
            Glide.with(itemView)    //url를 통해 이미지를 이미지뷰로 출력
                .load(item.photo)
                .apply(RequestOptions().centerCrop())
                .into(itemView.img_book)

            itemView.tv_bookTitle.text = item.title
            itemView.tv_bookDate.text = item.date
            itemView.setOnClickListener() { itemCheck(item) }
        }
    }
}