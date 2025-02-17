package com.btracsolutions.ems.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.ems.Model.Equipment
import com.btracsolutions.ems.Model.EquipmentSubCategoryList
import com.btracsolutions.ems.Model.Equipments
import com.btracsolutions.ems.Model.LatestStreamData
import com.btracsolutions.ems.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EquipmentAdapter (mContext:Context,private val mList: List<EquipmentSubCategoryList>,  private val cellClickListener: CellClickListener) : RecyclerView.Adapter<EquipmentAdapter.ViewHolder>(){
    // create new views
    val equipmentParameters = ArrayList<LatestStreamData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.equipment_row, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        val context = holder.itemView.context

        // sets the image to the imageview from our itemHolder class
      //  holder.imageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class




        for (i in ItemsViewModel.equipment){
            Log.d("text", "djsf "+i.name)
        }

        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(ItemsViewModel.equipment[position])
        }


        for (i in mList[position].equipment){
       //     val item = mList[position].equipmentSubCategoryList[i]
            holder.equipmentNameTV.text = i.name

            //date change
            if(!i.createdAt.isNullOrEmpty()) {
/*
                val sdfSource = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
                val date: Date = sdfSource.parse(i.createdAt)
                val sdfDestination = SimpleDateFormat("dd MMMM yyyy")
                holder.dateTV.text = sdfDestination.format(date)
*/




                var date1: Date? = null
                var dd:String ?=null

                @SuppressLint("SimpleDateFormat") val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH)
                try {
                    simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                    date1 = simpleDateFormat.parse(i.createdAt)
                    simpleDateFormat.timeZone = TimeZone.getDefault()
                    //2024-01-24T04:29:37.573000Z
                    //2021-02-15T00:00:00.000Z

                } catch (e: ParseException) {
                    Log.e("Error here${date1}", e.toString())
                }


                @SuppressLint("SimpleDateFormat") val simpleDateFormatF = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
                try {
                    simpleDateFormatF.timeZone = TimeZone.getTimeZone("UTC")
                    dd = simpleDateFormatF.format(date1)
                    simpleDateFormatF.timeZone = TimeZone.getDefault()
                    holder.dateTV.text = dd

                } catch (e: ParseException) {
                    Log.e("Error here${date1}", e.toString())
                }
            }
            //  System.out.println(sdfDestination.format(date))


            if(i.latestStreamData.size>0){

                for (j in i.latestStreamData){
                    equipmentParameters.add(j)

                }


                Log.d("mamsmasn ","fhsdjf "+equipmentParameters.size)
            }

            setupChipGroupDynamically(holder,context)




        }


    }

    private fun setupChipGroupDynamically(holder: ViewHolder, context: Context) {
        val chipGroup = ChipGroup(context)
        chipGroup.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.isSingleSelection = true
        chipGroup.isSingleLine = true

        for (i in equipmentParameters){
            chipGroup.addView(createChip(i.param_data+" "+i.unit_name,context))

        }



        holder.rootViewChips.addView(chipGroup)

    }


    private fun createChip(label: String?,context: Context): Chip {
        val chip = Chip(context, null, R.style.Theme_EMS)
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        chip.setChipStrokeColorResource(R.color.button_unselect_border)
        chip.setChipStrokeWidthResource(R.dimen.text_margin_1)

        chip.text = label
       // chip.isCloseIconVisible = true
        //chip.isChipIconVisible = true
        //chip.isCheckable = true
        chip.isClickable = true
        chip.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "Chip deleted successfully", Toast.LENGTH_SHORT).show()

        })
        /*chip.setOnCloseIconClickListener {
            Toast.makeText(this, "Chip deleted successfully", Toast.LENGTH_SHORT).show()
        }*/
        return chip
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
      //  val imageView = itemView.findViewById<ImageView>(R.id.imageview)
        val equipmentNameTV: TextView = itemView.findViewById(R.id.equipmentNameTV)
        val dateTV : TextView = itemView.findViewById(R.id.dateTV)
        val textViewheadrer: TextView = itemView.findViewById(R.id.textViewHeader)
        val rootViewChips : LinearLayout = itemView.findViewById(R.id.rootContainer)

    }

    interface CellClickListener {
        fun onCellClickListener(data: Equipment)
    }
}