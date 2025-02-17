package com.btracsolutions.ems.Activities

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.btracsolutions.ems.Adapters.SearchAdapter
import com.btracsolutions.ems.Model.Category
import com.btracsolutions.ems.Model.Devices
import com.btracsolutions.ems.Model.Devices_Equipments
import com.btracsolutions.ems.Model.EquipmentsNew
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Sessions.SessionManager
import com.btracsolutions.ems.Utils.Constant.equipmentForSearch
import com.btracsolutions.ems.Utils.Constant.equipmentSearch
import com.btracsolutions.ems.Utils.DefaultDispatcherProvider
import com.btracsolutions.ems.ViewModel.AllEquipmentViewModel
import com.btracsolutions.ems.ViewModel.RelayCommandViewModel
import com.btracsolutions.ems.databinding.ActivitySearchBinding
import com.btracsolutions.flowwithapi.Api.ApiHelperImpl
import com.btracsolutions.flowwithapi.Api.RetrofitBuilder
import com.btracsolutions.flowwithapi.Api.UiState
import com.btracsolutions.flowwithapi.ViewModel.ViewModelFactory
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch


class SearchActivity: AppCompatActivity(), SearchAdapter.CellClickListener,SearchAdapter.chipClickListener,View.OnClickListener {
    private lateinit var binding: ActivitySearchBinding

    private lateinit var adapter: SearchAdapter
    var equipment = ArrayList<Devices_Equipments>()
    private lateinit var viewModelRelayCommand: RelayCommandViewModel
    private lateinit var viewModel: AllEquipmentViewModel




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_dashboard)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  equipment = intent .getSerializableExtra( "key" ) as ArrayList<EquipmentNew>
        //  equipment = getSerializable(this, "key", EquipmentNew::class.java) as ArrayList<EquipmentNew>
        //val receivedArrayList = intent.getSerializableExtra("ARRAY_LIST_KEY") as? ArrayList<EquipmentNew>

        // equipment.addAll(equipmentForSearch)

        //  equipment = intent.getSerializableExtra("myList") as ArrayList<EquipmentNew>




        adapter = SearchAdapter(equipment,this,this)
        binding.recyclerViewSearch.adapter = adapter
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(this)



        // binding.recyclerViewSearch.visibility = View.GONE

/*
     binding.customSearch.searchHintIcon.setOnClickListener{

           binding.customSearch.searchView.isIconified = false
           binding.customSearch.searchView.requestFocus()

     }*/


        binding.customSearch.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //  adapter.filter(newText ?: "")
                binding.customSearch.searchHintIcon.visibility = View.GONE
                System.out.println("dfjkdf "+newText)
                equipment.clear()
                equipment.addAll(equipmentSearch)
                filter(newText)
                return false
            }
        })

        binding.backLL.setOnClickListener(this)






    }

    private fun fetchDataforEquipments(){
        val token = "Bearer "+ SessionManager.getToken(this)
        viewModel.fetchUsers(token)


    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {

                            binding.prgbar.visibility = View.GONE
                            Log.d("Equipments","Check list after Request: "+it.data)
                            processEquipmentData(it.data)

                            //  renderList(it.data)
                            // binding.recyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                                binding.prgbar.visibility = View.VISIBLE


                            //  binding.prgbar.visibility = View.VISIBLE

                            /*if(!isitFirstList){
                                binding.prgbar.visibility = View.VISIBLE

                            }*/
                            //  binding.recyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            //Handle Error
                            Log.d("Equipments","Check list: "+it.message)

                            binding.prgbar.visibility = View.GONE

                        }
                    }
                }
            }
        }
    }

    private fun processEquipmentData(data: EquipmentsNew) {
        equipmentSearch.clear()

        for (i in data!!.results_equipments){

            for (j in i.subCategories_equipments){


                for (k in j.devices_euipments){
                    equipmentSearch.add(k)

                }

            }


        }

        adapter.filterList(equipmentSearch)

    }


    private fun setupViewModel() {



        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[AllEquipmentViewModel::class.java]

        fetchDataforEquipments()


    }



    /*
        fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
        {
            return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                activity.intent.getSerializableExtra(name, clazz)!!
            else
                activity.intent.getSerializableExtra(name) as T
        }*/

    private fun filter(text: String?) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Devices_Equipments> = ArrayList()
        System.out.println("text is  "+ text)

        if(text!!.isEmpty() || text.isBlank()){
            binding.customSearch.searchHintIcon.visibility = View.VISIBLE
            binding.noDataTV.visibility = View.VISIBLE

            filteredlist.clear()
            adapter.filterList(filteredlist)
            // binding.recyclerViewSearch.visibility = View.GONE



        }
        else{
            // running a for loop to compare elements.
            System.out.println("ddfdfd "+ filteredlist.size + ": "+text)

            for (item in equipment) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.name!!.toLowerCase().contains(text!!.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    if(filteredlist.contains(item)){


                    }
                    else{
                        //  filteredlist.clear()
                        System.out.println("ddfdfd 1 "+ filteredlist.size + ": "+text+": "+item.name)

                        filteredlist.add(item)

                    }
                }
            }
            if (filteredlist.isEmpty()) {
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
                // Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
                // binding.recyclerViewSearch.visibility = View.GONE
                adapter.filterList(filteredlist)
                binding.noDataTV.visibility = View.VISIBLE

            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                //  binding.recyclerViewSearch.visibility = View.VISIBLE
                binding.noDataTV.visibility = View.GONE


                adapter.filterList(filteredlist)
            }
        }


    }

    override fun onCellClickListener(data: Devices_Equipments) {

        val intent = Intent(this@SearchActivity, DashboardDataActivity::class.java)
        // Passing the data to the
        // EmployeeDetails Activity
        intent.putExtra(DashboardWithFlow.NEXT_SCREEN,data )


        startActivity(intent)

    }

    override fun onChipClickListener(
        hardwareId: String?,
        chip: Chip,
        parameterValue: String?,
        paramName: String?,
        parameterShortName: String?
    ) {

        Log.d("Check my Status", "Hardware ID"+hardwareId)
        //  view.setBackgroundResource(R.drawable.relay_switch_on)
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.relay_on_solid)));
        chip.setChipStrokeColorResource(R.color.relay_on_border)
        chip.setChipStrokeWidthResource(R.dimen.text_margin__5)


        setupViewModelForRelay(hardwareId,parameterValue,paramName,parameterShortName)
        setupObserverForRelay(chip,parameterValue,paramName,parameterShortName)

    }


    private fun setupObserverForRelay(
        chip: Chip,
        paramValue: String?,
        paramName: String?,
        parameterShortName: String?
    ) {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelRelayCommand.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.prgbar.visibility = View.GONE
                            Log.d("Equipments","Check list: "+it.data.status+":::"+paramValue)
                            /*if (it.data.status == 200 && paramValue.equals("0")){

                                for (i in equipmentSearch){

                                    if(i.latestDeviceData != null){
                                        for (k in i.latestDeviceData!!.details_equipments){
                                            System.out.println("fsdfdjfdfg "+paramValue+ "datastream value: "+k.datastreamValue)

                                            if (k.datastream.equals(parameterShortName)){
                                                k.datastreamValue = "0"
                                                System.out.println("fsdfdjfdfg "+k.datastreamValue)
                                            }
                                        }

                                    }


                                }

                                chip.text = paramName+" OFF"

                                chip.setChipStrokeColorResource(R.color.relay_off_border)
                                chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
                                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.relay_off_solid)));


                            }

                            else if (it.data.status == 200 && paramValue.equals("1")){


                                for (i in equipmentSearch){

                                    if(i.latestDeviceData != null){
                                        for (k in i.latestDeviceData!!.details_equipments){
                                            if (k.datastream.equals(parameterShortName)){
                                                k.datastreamValue = "1"
                                            }
                                        }
                                    }



                                }

                                chip.text = paramName+" ON"

                                chip.setChipStrokeColorResource(R.color.relay_on_border)
                                chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
                                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.relay_on_solid)));

                            }*/

                            if(it.data.status == 200){
                                setupViewModel()
                                setupObserver()
                            }

                            else{
                                Toast.makeText(
                                    this@SearchActivity,
                                    it.data.message,
                                    Toast.LENGTH_LONG
                                ).show()                            }

                        }
                        is UiState.Loading -> {
                            binding.prgbar.visibility = View.VISIBLE
                            //  binding.recyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            //Handle Error
                            Log.d("Equipments","Check list: "+it.message)

                            binding.prgbar.visibility = View.GONE
                            Toast.makeText(
                                this@SearchActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }

    private fun setupViewModelForRelay(
        hardwareId: String?,
        paramValue: String?,
        paramName: String?,
        parameterShortName: String?
    ) {

        val token = "Bearer "+ SessionManager.getToken(this)





        viewModelRelayCommand = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[RelayCommandViewModel::class.java]
        viewModelRelayCommand.sendCommand(token, paramValue!!,parameterShortName!!,hardwareId!!)


    }

    override fun onClick(p0: View?) {
        when(p0!!.id){

            R.id.backLL ->
                finish()

        }
    }


}