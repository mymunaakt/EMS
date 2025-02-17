package com.btracsolutions.ems.Activities

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.ems.Adapters.TimeRangeAdapter
import com.btracsolutions.ems.Model.*
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Sessions.SessionManager
import com.btracsolutions.ems.Utils.DateConvertion
import com.btracsolutions.ems.Utils.DefaultDispatcherProvider
import com.btracsolutions.ems.Utils.getTimeAgoNew
import com.btracsolutions.ems.ViewModel.EquipmentAllDataViewModel
import com.btracsolutions.ems.databinding.ActivityDashboardDataBinding
import com.btracsolutions.flowwithapi.Api.ApiHelperImpl
import com.btracsolutions.flowwithapi.Api.RetrofitBuilder
import com.btracsolutions.flowwithapi.Api.UiState
import com.btracsolutions.flowwithapi.ViewModel.ViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable
import kotlin.collections.ArrayList

class DashboardDataActivity: AppCompatActivity(),View.OnClickListener, TimeRangeAdapter.CellClickListener {
    private lateinit var binding: ActivityDashboardDataBinding
    // private val viewModel by viewModels<EquipmentDetailsViewModel>()
    private lateinit var viewModelEquipmentsAllData: EquipmentAllDataViewModel

    private lateinit var timeRangeAdapter: RecyclerView.Adapter<*>
    val timeRangeList = ArrayList<Time>()
    var selectedTime : String = "60"
    val equipmentParameters = ArrayList<Details_Equipments>()
    val paramdata = ArrayList<ResultsData>()
    var selectedChip : Int = 252
    var selectedItem : Int = -1
    var selectedParam: String=""
    var isSelect: Int = 0
    var firstCall : Boolean = false
    private lateinit var equipData: Devices_Equipments


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_data)
        binding = ActivityDashboardDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        initData()
        clickListeners()





    }

    private fun setupViewModel() {
        //setupviewModel
        viewModelEquipmentsAllData = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[EquipmentAllDataViewModel::class.java]
        setupObserver()


    }

    private fun getDetailsData(selectedTime: String){
        if(equipData!=null){

            Log.d("TEXT", "EXcess Submit Value id:"+equipData.id+ "::time:  "+selectedTime)
            val equip_id = equipData.id


            val token = "Bearer "+ SessionManager.getToken(this)

            viewModelEquipmentsAllData.fetchallEquipmentsData(equip_id!!,token,selectedTime)

            //  viewModel.equipmentDetailsData(equip_id!!,token = token,selectedTime)
/*
            viewModel.equipmentDetailsResult.observe(this) {
                when (it) {
                    is BaseResponse.Loading -> {
                        showLoading()
                    }

                    is BaseResponse.Success -> {
                        stopLoading()
                        processLogin(it.data)
                    }

                    is BaseResponse.Error -> {
                        processError(it.msg)
                    }
                    else -> {
                        stopLoading()
                    }
                }
            }*/

        }

    }
/*
* Test SAzib
*
* */

/*
    private fun setupObserver() {

        lifecycleScope.launch {
                viewModelEquipmentsAllData.uiState.collectLatest {

                    Log.e("Lifecycle", "State: $it")

                    when (it) {
                        is UiState.Success -> {
                            // binding.prgbar.visibility = View.GONE
                            stopLoading()
                            Log.d("Equipments","Check list: "+it.data)
                            processLogin(it.data)
                            //  renderList(it.data)
                            // binding.recyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            // binding.prgbar.visibility = View.VISIBLE
                            showLoading()
                            //  binding.recyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            //Handle Error
                            Log.d("Equipments","Check list: "+it.message)

                            // binding.prgbar.visibility = View.GONE
                            stopLoading()
                            Toast.makeText(
                                this@DashboardDataActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {}
                    }
            }
        }

    }

 */


    private fun setupObserver() {



        lifecycleScope.launch {
            // repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModelEquipmentsAllData.uiState.collectLatest {

                //   if (!firstCall){
                when (it) {
                    is UiState.Success -> {
                        // binding.prgbar.visibility = View.GONE
                        stopLoading()
                        Log.d("Equipments","Check list data: "+it.data)
                        processLogin(it.data)



                        //  renderList(it.data)
                        // binding.recyclerView.visibility = View.VISIBLE
                    }
                    is UiState.Loading -> {
                        // binding.prgbar.visibility = View.VISIBLE
                        showLoading()
                        //  binding.recyclerView.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        //Handle Error
                        Log.d("Equipments","Check list: "+it.message)

                        // binding.prgbar.visibility = View.GONE
                        stopLoading()
                        Toast.makeText(
                            this@DashboardDataActivity,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
                }


                // }
            }
            //}
        }
    }

    private fun clickListeners() {
        binding.backLL.setOnClickListener(this)
    }

    private fun initData() {

        timeRangeList.add(Time("1hr","60",true))
        timeRangeList.add(Time("3hr","180"))
        timeRangeList.add(Time("12hr","720"))
        timeRangeList.add(Time("1d","1440"))
        timeRangeList.add(Time("1w","10080"))





        //setup time range array
        timeRangeAdapter = TimeRangeAdapter(this,timeRangeList,this)
        //  recyclerview.layoutManager = LinearLayoutManager(this)
        // recyclerview.adapter = adapter
        // recyclerview.setHasFixedSize(true)


        binding.timeRangeRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = timeRangeAdapter
        }


        // checking if the intent has extra
        if(intent.hasExtra(DashBoardEquipment.NEXT_SCREEN)){
            // get the Serializable data model class with the details in it
            equipData =
                    //  intent.getSerializableExtra(Dashboard.NEXT_SCREEN) as EquipmentSubCategoryList
                getSerializable(this, DashBoardEquipment.NEXT_SCREEN, Devices_Equipments::class.java) as Devices_Equipments
        }

        binding.tvDetailsTitle.text = "Details"
       // binding.paramNameTV.text = "Temperature"
        selectedParam = "Temperature"

        binding.equipmentNameTV.text = equipData.name

        //date time comment for now



        if(equipData.latestDeviceData != null){
            if(!equipData.latestDeviceData!!.createdAt.isNullOrEmpty()) {


               // val timeAgo = getTimeAgo(parseTimestamp(equipData.createdAt!!))
                val timeAgo = getTimeAgoNew(equipData.latestDeviceData!!.createdAt!!)

                binding.timeAgoTV.text = timeAgo
                binding.dateTV.text = DateConvertion().formatDate(equipData.latestDeviceData!!.createdAt!!,"dd MMMM yyyy")



            }
        }


        //date time





        //binding.dateTV.text = equipData.createdAt
        //binding.timeAgoTV.text = equipData.createdAt
        getDetailsData(selectedTime)

        if(equipData.latestDeviceData != null){

            for (j in equipData.latestDeviceData!!.details_equipments){
                equipmentParameters.add(j)

            }

            if(equipmentParameters.size>0){
                setupChipGroupDynamically()
                setUpChipGroupForRelay()

            }


            Log.d("mamsmasn ","fhsdjf "+equipmentParameters.size)
        }


    }

    private fun setUpChipGroupForRelay() {
        val chipGroup = ChipGroup(this)
        chipGroup.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.isSingleSelection = true

        chipGroup.isSingleLine = false
        // chipGroup.setChipSpacing(0)
        //chipGroup.chipSpacingHorizontal= 0
        //chipGroup.chipSpacingVertical = 0

        chipGroup.chipSpacingHorizontal= 20
        chipGroup.chipSpacingVertical = 30
        chipGroup.setSelectionRequired(true)

        for (i in equipmentParameters){
            if(i.dataType.equals("BOOLEAN")){
                chipGroup.addView(createChipRelay(i.datastreamValue,i.id.toString(),i.parameterName,i.datastreamValueMasking))


            }
        }

        binding.relayContainer.addView(chipGroup)

    }

    private fun setupChipGroupDynamically() {
        val chipGroup = ChipGroup(this)
        chipGroup.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipGroup.isSingleSelection = true

        chipGroup.isSingleLine = false
        // chipGroup.setChipSpacing(0)
        //chipGroup.chipSpacingHorizontal= 0
        //chipGroup.chipSpacingVertical = 0

        chipGroup.chipSpacingHorizontal= 20
        chipGroup.chipSpacingVertical = 30
        chipGroup.setSelectionRequired(true)



        val drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.CustomChipChoice)
        //  chipGroup.setChipDrawable(drawable)


        // chipGroup.setChipSpacingHorizontalResource(0)
/*
        for (i in equipmentParameters){


            if(i.dataType.equals("BOOLEAN")&& i.param_data.equals("0")){

                chipGroup.addView(createChip(i.param_data+" "+"Off",i.paramId,i.param_name))


            }
            else if(i.dataType.equals("BOOLEAN")&& i.param_data.equals("1")){
                chipGroup.addView(createChip(i.param_data+" "+"On",i.paramId,i.param_name))


            }
            else{
                chipGroup.addView(createChip(i.param_data+" "+i.unit_short_name,i.paramId,i.param_name))


            }




        }*/



        for (i in equipmentParameters){
            if(i.dataType.equals("FLOAT")){
                chipGroup.addView(createChip(i.datastreamValueMasking+" "+i.unitShortName,i.id.toString(),i.parameterName))


            }
        }

        binding.rootContainer.addView(chipGroup)

    }


    private fun createChipRelay(
        paramValue: String?,
        id: String?,
        paramName: String?,
        datastreamValueMasking: String?
    ): Chip {




        // val chip = inflater.inflate(R.layout.chip_in_list,chipgrp, false) as Chip


        // val chip = Chip(this,null, R.style.Cus)
        val chip = Chip(this, null, R.attr.CustomChipChoiceStyle)
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )





        if(isSelect == 0){
            isSelect = 1
            chip.isChecked = true
            // chip.setChipBackgroundColor(AppCompatResources.getColorStateList(this, R.color.button_filled_color));

        }

        //chip.setChipStrokeColorResource(R.color.button_unselect_border)
        //chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
        chip.setEnsureMinTouchTargetSize(false)





        val ttt:Int = id?.toIntOrNull()!!

        chip.text = paramName+" "+datastreamValueMasking

        if(paramValue.equals("1")){
        //    chip.text = paramName+" ON"

            chip.setChipStrokeColorResource(R.color.relay_on_border)
            chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.relay_on_solid)));

        }
        else{
         //   chip.text = paramName+" OFF"

            chip.setChipStrokeColorResource(R.color.relay_off_border)
            chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.relay_off_solid)));


        }
        //   chip.text = chipName
        chip.id = ttt

        chip.isClickable = true


        //  val drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.CustomChipChoice)
        // chip.setChipDrawable(drawable)
        // chip.setChipBackgroundColor(AppCompatResources.getColorStateList(this, R.color.background_color_chip_state_list));



        /*chip.setOnClickListener(View.OnClickListener {


            selectedChip = chip.id
            selectedParam = paramName!!
            showData()

        })*/
        return chip

    }



    private fun createChip(
        chipName: String?,
        id: String?,
        paramName: String?
    ): Chip {



        // val chip = inflater.inflate(R.layout.chip_in_list,chipgrp, false) as Chip


        // val chip = Chip(this,null, R.style.Cus)
        val chip = Chip(this, null, R.attr.CustomChipChoiceStyle)
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )





        if(isSelect == 0){
            isSelect = 1
            chip.isChecked = true
            // chip.setChipBackgroundColor(AppCompatResources.getColorStateList(this, R.color.button_filled_color));

        }

        chip.setChipStrokeColorResource(R.color.button_unselect_border)
        chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
        chip.setEnsureMinTouchTargetSize(false)





        val ttt:Int = id?.toIntOrNull()!!
        chip.text = chipName
        chip.id = ttt
        selectedChip = chip.id


        //   chip.isClickable = true


        //  val drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.CustomChipChoice)
        // chip.setChipDrawable(drawable)
        // chip.setChipBackgroundColor(AppCompatResources.getColorStateList(this, R.color.background_color_chip_state_list));



        chip.setOnClickListener(View.OnClickListener {


            selectedChip = chip.id
            selectedParam = paramName!!
            showData()

        })
        return chip
    }



    fun processError(msg: String?) {
        Log.d("ACtivity : ","EXcess Error equipment: "+msg)
        showToast("Some errors have been found. Please Try again Later!" )


    }

    fun showToast( message: String) {
        Log.d("ACtivity : ","EXcess Toast Message!! : "+message)
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()


        /*   val parent: ViewGroup? = null
           val toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
           val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
           val toastView = inflater.inflate(R.layout.custom_toast, parent)

           val messageTV = toastView.findViewById<TextView>(R.id.successMessage)

           messageTV.text = message
           toast.view = toastView
           toast.show()*/
    }



    private fun processLogin(data: EquipmentsData?) {

        //  Log.d("ACtivity : ","EXcess Response"+data!!.results[0].equipmentId)

        //  val equip_param_data = ArrayList<String>()
        if(data!!.success!!){
            // firstCall = true

            paramdata.clear()


            if(data!!.results.size>0){
                for (i in data!!.results){
                    paramdata.add(i)

                }
            }




            showData()


        }
        else{
            showToast("Some issues have been occurred. Please try again later!")
        }

        /*   for (i in equip_param_data){
               Log.d("ACtivity : ","equipmentData: "+i+": "+selectedChip.toString())

           }

           if(equip_param_data.size>0){
               binding.paramNameTV.text = selectedParam
               binding.ourLineChart.visibility = View.VISIBLE
               populateLineChart(equip_param_data)

           }
           else{
               binding.paramNameTV.text = ""
               binding.ourLineChart.visibility = View.GONE

           }
   */




    }

    private fun showData(){

       // binding.progressbar.visibility = View.VISIBLE

        System.out.println("id parameter abcddd chip id "+selectedChip.toString())


        val equip_param_data = ArrayList<String>()
        val equip_param_time = ArrayList<String>()

        var dataType: String = "FLOAT"



        for (i in paramdata){
            for (j in i.details){


                if(j.datastream.equals("t1")){
                    equip_param_data.add(j.datastreamValue!!)
                    equip_param_time.add(i.createdAt!!)
                }
            }
        }



       // System.out.println("abcd : dd "+paramdata.size+ "parameterdata "+equip_param_data.size)

           Log.d("teststtsd","Check list data 123:"+paramdata.size+" equipdata "+equip_param_data.size)





        if(equip_param_data.size>0){



            binding.graphLL.visibility = View.VISIBLE

            binding.paramNameTV.text = selectedParam
            binding.ourLineChart.visibility = View.VISIBLE

            if(dataType.equals("FLOAT")){

                binding.ourLineChart.visibility=View.VISIBLE
                binding.ourBarChart.visibility = View.GONE

              //  binding.ourLineChart.clear()
              //  populateLineChart(equip_param_data,equip_param_time)
                populate(equip_param_data,equip_param_time)

            }
            else if(dataType.equals("BOOLEAN")){

                binding.ourLineChart.visibility=View.GONE
                binding.ourBarChart.visibility = View.VISIBLE
                populateBarChart(equip_param_data,equip_param_time)
            }


        }
        else{
            binding.graphLL.visibility = View.GONE
            binding.paramNameTV.text = ""
            binding.ourLineChart.visibility = View.GONE
            showToast("No graph data is available to show")

        }


    }



    private fun populateBarChart(values: ArrayList<String>, equip_param_time: ArrayList<String>) {
        //adding values
        val ourBarEntries: ArrayList<BarEntry> = ArrayList()
        var i = 0

        for (entry in values) {
            var value = values[i].toFloat()
            ourBarEntries.add(BarEntry(i.toFloat(), value))
            i++
        }


        val barDataSet = BarDataSet(ourBarEntries, "")
        //set a template coloring
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = BarData(barDataSet)
        binding.ourBarChart.data = data
        //setting the x-axis
        val xAxis: XAxis = binding.ourBarChart.xAxis
        //calling methods to hide x-axis gridlines
        binding.ourBarChart.axisLeft.setDrawGridLines(false)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remove legend
        binding.ourBarChart.legend.isEnabled = false

        //remove description label
        binding.ourBarChart.description.isEnabled = false

        //add animation
        binding.ourBarChart.animateY(3000)
        //refresh the chart
        binding.ourBarChart.invalidate()
    }
    private fun populateLineChart(values: ArrayList<String>, equip_param_time: ArrayList<String>) {

        binding.progressbar.visibility = View.GONE

        // Save current zoom and visible X range before updating the chart
        val currentScaleX = binding.ourLineChart.viewPortHandler.scaleX
        val currentScaleY = binding.ourLineChart.viewPortHandler.scaleY
        val lowestVisibleX = binding.ourLineChart.lowestVisibleX
        val highestVisibleX = binding.ourLineChart.highestVisibleX

        val ourLineChartEntries: ArrayList<Entry> = ArrayList()

        for ((i, entry) in values.withIndex()) {
            val value = entry.toFloat()
            ourLineChartEntries.add(Entry(i.toFloat(), value))
        }

        val lineDataSet = LineDataSet(ourLineChartEntries, "").apply {
            color = Color.BLUE // Set the color of the line
        }

        val data = LineData(lineDataSet)
        binding.ourLineChart.axisLeft.setDrawGridLines(false)

        val xAxis: XAxis = binding.ourLineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(equip_param_time)
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -45f
        xAxis.textSize = 8f

        binding.ourLineChart.extraLeftOffset = 10f
        binding.ourLineChart.extraBottomOffset = 10f

        // Set data
        binding.ourLineChart.data = data

        // Set fixed visible X range to show the first 5 entries
        val xMax = 5f
        binding.ourLineChart.setVisibleXRangeMaximum(xMax)

        // Adjust zoom if the dataset is smaller than 5 entries
        val xTotal = values.size.toFloat()
        if (xTotal < xMax) {
            binding.ourLineChart.resetZoom() // Reset zoom for small datasets
            binding.ourLineChart.setVisibleXRangeMaximum(xTotal) // Show all if less than 5
        } else {
            // Restore zoom and view position for larger datasets
            binding.ourLineChart.viewPortHandler.setZoom(currentScaleX, currentScaleY)
            binding.ourLineChart.moveViewToX(lowestVisibleX)
        }

        // Move the view to the first entry
        binding.ourLineChart.moveViewToX(0f)

        binding.ourLineChart.legend.isEnabled = false

        // Remove description label
        binding.ourLineChart.description.isEnabled = true
        binding.ourLineChart.description.textColor = Color.WHITE

        // Set fixed y-axis limits if needed
        binding.ourLineChart.axisLeft.axisMinimum = 0f // Set your desired min value
        binding.ourLineChart.axisLeft.axisMaximum = values.maxOf { it.toFloat() } // Set max based on data

        // Add animation
        binding.ourLineChart.animateX(1000, Easing.EaseInSine)

        // Refresh the chart after all transformations
        binding.ourLineChart.invalidate()
    }

/*    fun populate(values: ArrayList<String>, equip_param_time: ArrayList<String>){
        binding.progressbar.visibility = View.GONE

        val ourLineChartEntries: ArrayList<Entry> = ArrayList()

        var i = 0

        Log.d("teststtsd","Check list data 456:"+values.size)

        for (entry in values) {
            var value = values[i].toFloat()
            ourLineChartEntries.add(Entry(i.toFloat(), value))
            i++
        }
        val lineDataSet = LineDataSet(ourLineChartEntries, "")
        lineDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        val data = LineData(lineDataSet)
        binding.ourLineChart.axisLeft.setDrawGridLines(false)

        val xAxis: XAxis = binding.ourLineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(equip_param_time)
        //  xAxis.labelRotationAngle = 120F

        //new change for date
        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -45f
        binding.ourLineChart.extraLeftOffset=10f
        binding.ourLineChart.extraBottomOffset = 10f
        //xAxis.labelCount = equip_param_time.size // Set the number of labels to be displayed
        //   xAxis.setAvoidFirstLastClipping(true)
        xAxis.textSize = 8f
        //new change for date

        // Show the right X-axis without labels
        binding.ourLineChart.axisRight.isEnabled = true
        binding.ourLineChart.axisRight.setDrawLabels(false) // Disable labels on the right X-axis
        binding.ourLineChart.axisRight.setDrawGridLines(true)








        binding.ourLineChart.legend.isEnabled = false




        //remove description label
        binding.ourLineChart.description.isEnabled = true
        binding.ourLineChart.description.textColor= (Color.WHITE)

        //add animation
        binding.ourLineChart.animateX(1000, Easing.EaseInSine)
        binding.ourLineChart.data = data
        //refresh
        binding.ourLineChart.invalidate()
    }*/
    fun populate(values: ArrayList<String>, equip_param_time: ArrayList<String>) {
        binding.progressbar.visibility = View.GONE

        val ourLineChartEntries: ArrayList<Entry> = ArrayList()

        var i = 0
        Log.d("teststtsd", "Check list data 456:" + values.size)

        // Populate the chart with values
        for (entry in values) {
            val value = values[i].toFloat()
            ourLineChartEntries.add(Entry(i.toFloat(), value))
            i++
        }

        // Create the dataset
        val lineDataSet = LineDataSet(ourLineChartEntries, "")
      //  lineDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        //lineDataSet.color = Color.BLUE
        lineDataSet.lineWidth = 1f

        lineDataSet.setDrawFilled(true) // Enable fill
        lineDataSet.setDrawCircles(true)
        lineDataSet.setDrawCircleHole(false)
       // lineDataSet.fillColor = Color.BLUE // Set fill color
      //  lineDataSet.fillAlpha = 80 // Set fill transparency
       // lineDataSet.color = ContextCompat.getColor(this, R.color.graph_cicrcle) // Line color

        lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.graph_cicrcle)) // Circle color at data points

    // Use a drawable resource for gradient fill
      val drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue)
       lineDataSet.setFillDrawable(drawable)


    // Set the data and refresh
        val data = LineData(lineDataSet)
        binding.ourLineChart.data = data

        // Set the X-Axis labels (equip_param_time)
        val xAxis: XAxis = binding.ourLineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(equip_param_time)



        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -45f
        xAxis.textSize = 8f
        binding.ourLineChart.extraLeftOffset = 10f
        binding.ourLineChart.extraBottomOffset = 10f

      //  binding.ourLineChart.moveViewToX(0f)
    binding.ourLineChart.moveViewToX(values.size.toFloat())
        binding.ourLineChart.setVisibleXRange(5f,5f)



/*        val xMax = 5f // Number of values to display at a time
        val xTotal = values.size.toFloat()



        // Set the initial zoom level to show the first 5 values
        val scaleX = xTotal / xMax

        binding.ourLineChart.zoom(scaleX, 1f, 0f, 0f)*/

        // Show the right X-axis without labels
        binding.ourLineChart.axisRight.isEnabled = true
        binding.ourLineChart.axisRight.setDrawLabels(false)
        binding.ourLineChart.axisRight.setDrawGridLines(true)

        // Remove legend and description label
        binding.ourLineChart.legend.isEnabled = false
        binding.ourLineChart.description.isEnabled = false
      //  binding.ourLineChart.description.textColor = Color.WHITE

        // Add animation
        binding.ourLineChart.animateX(1000, Easing.EaseInSine)


        binding.ourLineChart.invalidate()
    }



    fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            activity.intent.getSerializableExtra(name, clazz)!!
        else
            activity.intent.getSerializableExtra(name) as T
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){

            R.id.backLL ->
                finish()

        }

    }

    override fun onCellClickListener(time: String,position: Int) {




        val clicked_item:Time = timeRangeList[position]
        // clicked_item.categoryName = "clicked"
        if(!clicked_item.isSelected){
            if(position != 0 && timeRangeList[0].isSelected ){
                timeRangeList[0].isSelected = false
            }


            clicked_item.isSelected = !clicked_item.isSelected
            if(selectedItem != -1){
                val last_item = timeRangeList[selectedItem]
                last_item.isSelected = false
            }
            selectedItem = position
            timeRangeAdapter.notifyDataSetChanged()
            //  timeRangeAdapter.notifyDataSetChanged()


            selectedTime = time
            //  firstCall = false
          //  binding.ourLineChart.clear()
            binding.progressbar.visibility = View.VISIBLE

            getDetailsData(selectedTime)
        }


    }

}