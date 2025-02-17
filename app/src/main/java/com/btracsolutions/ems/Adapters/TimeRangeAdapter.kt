package com.btracsolutions.ems.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.ems.Model.Category
import com.btracsolutions.ems.Model.Equipment
import com.btracsolutions.ems.Model.Time
import com.btracsolutions.ems.R

class TimeRangeAdapter(private val context: Context, private val list: ArrayList<Time>,
                       private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<TimeRangeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = list[position]
        holder.timeTV.text = ItemsViewModel.time_in_hr



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

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(ItemsViewModel.time_in_minute,position)
        }


    }



    interface CellClickListener {
        fun onCellClickListener(time: String,position: Int)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val timeTV: TextView = itemView.findViewById(R.id.time)


    }
}