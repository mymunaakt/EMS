package com.btracsolutions.ems.Adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.ems.Model.Category
import com.btracsolutions.ems.R

class CategoryAdapter(private val context: Context, private val list: ArrayList<Category>,
                      private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectedItem: Int = 0

    private inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {


        internal var tvName: TextView

        init {

            tvName = itemView.findViewById(R.id.name) // Initialize your All views prensent in list items
            itemView.setOnClickListener(this)
        }

        internal fun bind(position: Int) {
            // This method will be called anytime a list item is created or update its data
            //Do your stuff here

            tvName.text = list[position].categoryName
        }

        override fun onClick(v: View?) {
            val position:Int = adapterPosition
            selectedItem = position
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position,this@CategoryAdapter,itemView)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(list[position].isSelected){

            holder.itemView.setBackgroundResource(R.drawable.btn_shape_round_filled)
            holder.itemView.findViewById<ConstraintLayout>(R.id.constraintCategoryItem).setBackgroundResource(R.drawable.btn_shape_round_filled)


            // holder.itemView.setBackgroundColor(Color.YELLOW)
            //holder.itemView.findViewById<ConstraintLayout>(R.id.constraintCategoryItem).setBackgroundColor(Color.YELLOW)
        } else{

            holder.itemView.setBackgroundResource(R.drawable.btn_shape_round)
            holder.itemView.findViewById<ConstraintLayout>(R.id.constraintCategoryItem).setBackgroundResource(R.drawable.btn_shape_round)

            //holder.itemView.setBackgroundColor(Color.WHITE)
            //holder.itemView.findViewById<ConstraintLayout>(R.id.constraintCategoryItem).setBackgroundColor(Color.WHITE)
        }

 /*       if(selectedItem == position){
            holder.itemView.setBackgroundColor(Color.YELLOW)
            holder.itemView.findViewById<ConstraintLayout>(R.id.constraintCategoryItem).setBackgroundColor(Color.YELLOW)
        }
        else{
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.itemView.findViewById<ConstraintLayout>(R.id.constraintCategoryItem).setBackgroundColor(Color.WHITE)
        }*/
        (holder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int,adapter:CategoryAdapter,v:View)
    }
}