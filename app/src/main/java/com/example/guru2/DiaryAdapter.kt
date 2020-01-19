package com.example.guru2

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.page_item.view.*
import java.util.ArrayList

class  DiaryAdapter(val context: Context, val itemCheck: (Diary)-> Unit)
    : RecyclerView.Adapter<DiaryAdapter.ViewHolder>(){

    private var items = ArrayList<Diary>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflater.inflate(R.layout.page_item, viewGroup, false)
        return ViewHolder(itemView, itemCheck)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Diary = items[position]
        viewHolder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setItems(items: ArrayList<Diary>) {
        this.items = items
    }

    fun addItem(item: Diary){
        items.add(item)
    }

    inner class ViewHolder(itemView: View, itemCheck: (Diary) -> Unit)
        :RecyclerView.ViewHolder(itemView){
        fun setItem(item: Diary) {
            val resourceId = context.resources.getIdentifier(
                item.photo,
                "drawable",
                context.packageName
            )
            if (resourceId in 0..1) {
                itemView.img_book_title?.setImageResource(R.mipmap.ic_launcher)
            }
            else {
                itemView.img_book_title?.setImageResource(resourceId)
            }
            itemView.txt_item_num.text = item.num
            itemView.txt_item_date.text = item.date
            itemView.txt_item_title.text = item.title
            itemView.setOnClickListener() { itemCheck(item) }
        }
    }
}