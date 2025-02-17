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
import com.btracsolutions.ems.Model.SubCategories_Equipments
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Utils.DateConvertion
import com.btracsolutions.ems.Utils.getTimeAgoNew
import com.btracsolutions.ems.databinding.ItemEquipmentBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class NewEquipmentAdapter (private val mList: List<SubCategories_Equipments>, private val cellClickListener: CellClickListener, private val ChipListener: chipClickListener) : RecyclerView.Adapter<NewEquipmentAdapter.ViewHolder>() {

    var isSelect: Int = 0


    inner class ViewHolder(val binding: ItemEquipmentBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewEquipmentAdapter.ViewHolder {
        val binding = ItemEquipmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NewEquipmentAdapter.ViewHolder, position: Int) {
        with(holder){
            with(mList[position]){

                val context = holder.itemView.context



                binding.textViewHeader.text = this.name
                for (i in mList[position].devices_euipments){
                    binding.mainholderLL.addView(createChild(context,i,this.name))
                }


            }
        }
    }

    private fun createChild(context: Context, equipment: Devices_Equipments, name: String?): View? {


        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_sub_equipment, null)


        // view.setPadding(convertToDp(10,context),convertToDp(15,context),convertToDp(10,context),convertToDp(15,context))


        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 10, 0, 0)

        view.setLayoutParams(layoutParams)

        view.setOnClickListener {
            cellClickListener.onCellClickListener(equipment)

        }

// Find the name text view
        val equipmentNameTV = view.findViewById<TextView>(R.id.equipmentNameTV)
        val dateTV = view.findViewById<TextView>(R.id.dateTV)
        val chipContainerLL = view.findViewById<LinearLayout>(R.id.rootContainer)
        val chipContainerLLRelay = view.findViewById<LinearLayout>(R.id.relay_container)

        val timeAgoTV = view.findViewById<TextView>(R.id.timeAgoTV)

        equipmentNameTV.text = equipment.name

        //Date comment for now

        if(equipment?.latestDeviceData!= null){
           if( !equipment.latestDeviceData?.createdAt?.isNullOrEmpty()!!) {

              //  val timeAgo = getTimeAgo(parseTimestamp(equipment.latestDeviceData!!.createdAt!!))
               val timeAgo = getTimeAgoNew(equipment.latestDeviceData!!.createdAt!!)

                timeAgoTV.text = timeAgo
                dateTV.text = DateConvertion().formatDate(equipment.latestDeviceData!!.createdAt!!,"dd MMMM yyyy")




            }



            setupChipGroupDynamically(chipContainerLL, equipment.latestDeviceData!!,context)
            setupChipGroupRelayDynamically(chipContainerLLRelay, equipment.latestDeviceData!!,context)
        }

        else if(equipment.latestDeviceData == null){
            System.out.println("jdsjdfksdf "+equipment.name)
        }


        return view
    }

    private fun setupChipGroupRelayDynamically(chipContainerLLRelay: LinearLayout?, equipment: LatestDeviceData_Equipments, context: Context) {
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








        for(i in equipment.details_equipments){


            if(i.dataType.equals("BOOLEAN")){
                System.out.println("check oolean value: "+i.parameterName+" : "+i.datastreamValue+ " : "+equipment.gatewayHardwareId+" :"+i.datastream+ ": "+i.datastreamValueMasking)
                chipGroup.addView(createChipRelay(i.parameterName,i.datastreamValue,context,equipment.gatewayHardwareId, i.datastream,i.datastreamValueMasking))

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

        chip.text = paramName+" "+datastreamValueMasking


        System.out.println("check my status for change : "+parameterValue)

        if(parameterValue.equals("1")){
           // chip.text = paramName+" ON"

            chip.setChipStrokeColorResource(R.color.relay_on_border)
            chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.relay_on_solid)));

        }
        else{
          //  chip.text = paramName+" OFF"

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
            /*if (chip.text.contains("ON")){
                chipCommandValue = "0"
                dialogMessage = "Turn OFF "+paramName+" ?"

            }
            else if(chip.text.contains("OFF")){
                chipCommandValue = "1"
                dialogMessage = "Turn ON "+paramName+" ?"

            }
*/
            val builder = AlertDialog.Builder(context)
            // builder.setTitle("Androidly Alert")
            builder.setMessage(dialogMessage)
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton("Yes") { dialog, which ->
/*                Toast.makeText(context,
                    "Yes", Toast.LENGTH_SHORT).show()*/
                /*for (i  in mList){
                    if ()
                }*/
                //System.out.println("get the value ")
                ChipListener.onChipClickListener(hardwareId,chip,chipCommandValue,paramName,parameterShortName)

            }

            builder.setNegativeButton("No") { dialog, which ->
                /*               Toast.makeText(context,
                                   "No", Toast.LENGTH_SHORT).show()*/
                dialog.dismiss()
            }


            builder.show()



            //    ChipListener.onChipClickListener(hardwareId,chip,chipCommandValue,paramName)




        })
        return chip


    }

    private fun setupChipGroupDynamically(chipContainerLL: LinearLayout?, equipmentNew: LatestDeviceData_Equipments, context: Context) {
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

    private fun convertToDp(value:Int,context: Context):Int{
        val density:Float =  context.getResources().getDisplayMetrics().density;
        val valueInDp :Int = (value * density).toInt()

        return valueInDp

    }


    override fun getItemCount(): Int {
        return mList.size
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