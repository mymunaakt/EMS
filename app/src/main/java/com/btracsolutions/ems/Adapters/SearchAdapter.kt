package com.btracsolutions.ems.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.ems.Model.Devices_Equipments
import com.btracsolutions.ems.Model.LatestDeviceData_Equipments
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Utils.DateConvertion
import com.btracsolutions.ems.Utils.getTimeAgoNew
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SearchAdapter(private val itemList: List<Devices_Equipments>, private val cellClickListener: SearchAdapter.CellClickListener, private val ChipListener: SearchAdapter.chipClickListener) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var filteredList = itemList.toMutableList()
    var isitFirst: Boolean = false
    //  private var filteredList1: List<EquipmentNew> = itemList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sub_equipment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = filteredList.size

    fun filter(query: String) {
        filteredList.clear()
        if (query.isEmpty()) {
            filteredList.addAll(itemList)
        } else {

            val filtered = itemList.filter {
                System.out.println("dfndf "+it.name )

                it.name!!.contains(query, ignoreCase = true)
            }

            filteredList.addAll(filtered)
        }
        notifyDataSetChanged()
    }

    fun filterList(filterlist: ArrayList<Devices_Equipments>) {
        // below line is to add our filtered
        // list in our course array list.
        filteredList = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val equipmentNameTV = itemView.findViewById<TextView>(R.id.equipmentNameTV)
        val dateTV = itemView.findViewById<TextView>(R.id.dateTV)
        val chipContainerLL = itemView.findViewById<LinearLayout>(R.id.rootContainer)
        val chipContainerLLRelay = itemView.findViewById<LinearLayout>(R.id.relay_container)

        val timeAgoTV = itemView.findViewById<TextView>(R.id.timeAgoTV)

        val context = itemView.context



        fun bind(equipment: Devices_Equipments) {
            equipmentNameTV.text = equipment.name

            //Date
            if(equipment.latestDeviceData!=null){
                if(!equipment.latestDeviceData!!.createdAt.isNullOrEmpty()) {

                    // val timeAgo = getTimeAgo(parseTimestamp(equipment.createdAt!!))
                    val timeAgo = getTimeAgoNew(equipment.latestDeviceData!!.createdAt!!)


                    timeAgoTV.text = timeAgo
                    dateTV.text = DateConvertion().formatDate(equipment.latestDeviceData!!.createdAt!!,"dd MMMM yyyy")




                }
                setupChipGroupDynamically(chipContainerLL, equipment.latestDeviceData!!,context)
                setupChipGroupRelayDynamically(chipContainerLLRelay,equipment.latestDeviceData,context)
            }



            itemView.setOnClickListener {
                cellClickListener.onCellClickListener(equipment)
            }

            System.out.println("sssssss "+equipment.name)


        }
    }

    private fun setupChipGroupRelayDynamically(chipContainerLLRelay: LinearLayout?, equipment: LatestDeviceData_Equipments?, context: Context) {
        System.out.println("abcd "+filteredList.size)
        chipContainerLLRelay!!.removeAllViews()
        val chipGroup = ChipGroup(context)
        chipGroup.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.isSingleSelection = true

        chipGroup.isSingleLine = false


        chipGroup.chipSpacingHorizontal= 20
        chipGroup.chipSpacingVertical = 30
        chipGroup.setSelectionRequired(true)








        /*for(i in equipment.latestStreamData){
            if (i.unitName.equals("Relay")){
                chipGroup.addView(createChipRelay(i.paramName,i.parameterValue,context,equipment.hardwareId, i.parameterShortName))

            }


        }*/

        for(i in equipment!!.details_equipments){


            if(i.dataType.equals("BOOLEAN")){
                chipGroup.addView(createChipRelay(i.parameterName,i.datastreamValue,context,
                    equipment!!.gatewayHardwareId, i.datastream,i.datastreamValueMasking))

            }



        }




        chipContainerLLRelay!!.addView(chipGroup)



    }

    private fun createChipRelay(
        paramName: String?,
        parameterValue: String?,
        context: Context,
        hardwareId: String?,
        parameterShortName: String?,
        datastreamValueMasking: String?
    ): View? {

        //val chip = Chip(context, null, R.attr.CustomChipChoiceStyle)
        val chip = Chip(context)
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )



        chip.setEnsureMinTouchTargetSize(false)


        chip.text = paramName + " "+datastreamValueMasking

        if(parameterValue.equals("1")){
           // chip.text = paramName+" ON"

            chip.setChipStrokeColorResource(R.color.relay_on_border)
            chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.relay_on_solid)));

        }
        else{
         //   chip.text = paramName+" OFF"

            chip.setChipStrokeColorResource(R.color.relay_off_border)
            chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.relay_off_solid)));


        }


        //chip.id = ttt

        chip.isClickable = true





        var chipCommandValue = ""
        var dialogMessage = ""




        chip.setOnClickListener(View.OnClickListener {

            dialogMessage = "Change status of "+paramName+" ?"
            if(parameterValue.equals("0")){
                chipCommandValue = "1"
            }
            else{
                chipCommandValue = "0"
            }

          /*  if (chip.text.contains("ON")){
                chipCommandValue = "0"
                dialogMessage = "Turn OFF "+paramName+" ?"

            }
            else if(chip.text.contains("OFF")){
                chipCommandValue = "1"
                dialogMessage = "Turn ON "+paramName+" ?"

            }*/

            val builder = AlertDialog.Builder(context)

            builder.setMessage(dialogMessage)

            builder.setPositiveButton("Yes") { dialog, which ->
                ChipListener.onChipClickListener(hardwareId,chip,chipCommandValue,paramName,parameterShortName)

            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }


            builder.show()







        })

        return chip


    }


    private fun setupChipGroupDynamically(chipContainerLL: LinearLayout?, equipmentNew: LatestDeviceData_Equipments, context: Context) {
        chipContainerLL!!.removeAllViews()
        val chipGroup = ChipGroup(context)
        chipGroup.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.isSingleSelection = true

        chipGroup.isSingleLine = false


        chipGroup.chipSpacingHorizontal= 20
        chipGroup.chipSpacingVertical = 30
        chipGroup.setSelectionRequired(true)

   /*     for(i in equipmentNew.latestStreamData) {
            if (!i.unitName.equals("Relay")) {
                chipGroup.addView(createChip(i.parameterValue, i.unitShortName, context))

            }
        }*/

        for(i in equipmentNew.details_equipments) {



            if (i.dataType.equals("FLOAT")) {
                chipGroup.addView(createChip(i.datastreamValue, i.unitShortName, context,i.datastreamValueMasking))

            }
        }



        chipContainerLL!!.addView(chipGroup)

    }

    private fun createChip(
        paramName: String?,
        parameterValue: String?,
        context: Context,
        datastreamValueMasking: String?
    ): View? {

        val chip = Chip(context, null, R.attr.CustomChipChoiceStyle)
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        chip.setChipStrokeColorResource(R.color.button_unselect_border)
        chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.chip_unselect)));
        chip.setEnsureMinTouchTargetSize(false)






        chip.text = datastreamValueMasking+ "" +parameterValue
        //chip.id = ttt

        chip.isClickable = true






        chip.setOnClickListener(View.OnClickListener {




        })
        return chip
    }

    interface CellClickListener {
        fun onCellClickListener(data: Devices_Equipments)
    }


    interface chipClickListener{
        fun onChipClickListener(
            hardwareId: String?,
            chip: Chip,
            parameterValue: String?,
            paramName: String?,
            parameterShortName: String?
        )
    }

}
