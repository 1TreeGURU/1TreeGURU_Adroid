package com.example.guru2

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.page_item.view.*
import java.util.ArrayList

class  DiaryAdapter(val context: Context, val itemCheck: (Page)-> Unit)
    : RecyclerView.Adapter<DiaryAdapter.ViewHolder>(){

    private var items = ArrayList<Page>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflater.inflate(R.layout.page_item, viewGroup, false)
        return ViewHolder(itemView, itemCheck)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Page = items[position]
        viewHolder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setItems(items: ArrayList<Page>) {
        this.items = items
    }

    inner class ViewHolder(itemView: View, itemCheck: (Page) -> Unit)
        :RecyclerView.ViewHolder(itemView){
        fun setItem(item: Page) {
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