package com.btracsolutions.ems.Activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.ems.Adapters.CategoryAdapter
import com.btracsolutions.ems.Adapters.EquipmentAdapterWithChip
import com.btracsolutions.ems.Model.*
import com.btracsolutions.ems.Notification.NotificationListActivity
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Sessions.SessionManager
import com.btracsolutions.ems.Utils.Constant.deviceSubCategoryList
import com.btracsolutions.ems.Utils.Constant.equipmentForSearch
import com.btracsolutions.ems.Utils.DefaultDispatcherProvider
import com.btracsolutions.ems.ViewModel.*
import com.btracsolutions.ems.databinding.ActivityDashboardWithFlowBinding
import com.btracsolutions.flowwithapi.Api.ApiHelperImpl
import com.btracsolutions.flowwithapi.Api.RetrofitBuilder
import com.btracsolutions.flowwithapi.Api.UiState
import com.btracsolutions.flowwithapi.ViewModel.ViewModelFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.chip.Chip
import com.google.firebase.messaging.FirebaseMessaging.*
import kotlinx.coroutines.launch


class DashboardWithFlow: AppCompatActivity(),EquipmentAdapterWithChip.CellClickListener,EquipmentAdapterWithChip.chipClickListener,CategoryAdapter.OnItemClickListener,View.OnClickListener {
    private lateinit var binding: ActivityDashboardWithFlowBinding
    private lateinit var viewModel: EquipmentViewModelWithFlow
    private lateinit var viewModelRelayCommand: RelayCommandViewModel
    private lateinit var viewModelDeviceCount: DeviceCountViewModel
    private lateinit var viewModelLogout: UserLogoutViewModel
    private lateinit var viewModelPushNotification: PushNotificationViewModel


    var categoryName: String = ""

    val categoryList = ArrayList<Category>()
    val equipmentList = ArrayList<EquipResults>()
    var isFirst: Int = 0
    private lateinit var equipmentAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var selectedItem : Int = -1
    var isitFirstCount: Boolean = false
    var isitFirstList: Boolean = false





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_dashboard)
        binding = ActivityDashboardWithFlowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



    //    setupBadge(binding.badgeLayout1)
        System.out.println("hddggfgffgfg")
        setupViewModelDeviceCount()
        setupObserverDeviceCount()

        setupViewModel()
        setupObserver()
        onClicckListeners()

        // push notification
        setupViewmodelForPush()
        getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {

                val pushToken = task.result
                // Send token to server
                val token = "Bearer "+ SessionManager.getToken(this)
                Log.d("DashboardWithFliow", "pushToken: $pushToken"+ "Token: "+token)


                viewModelPushNotification.sendPusToken(token,pushToken!!)
            } else {
                Log.w("DashboardWithFliow", "Failed to get token")
                return@OnCompleteListener

            }


        })


        setupNotificationCount()


    }

    private fun setupNotificationCount() {
        val count = SessionManager.getNotificationCount(this)
        System.out.println("tteteye: "+count)
        if(count.equals("0") || count.isNullOrEmpty()){
            binding.badgeNotificationTv.visibility = View.GONE
        }
        else{
            binding.badgeNotificationTv.visibility = View.VISIBLE
            binding.badgeNotificationTv.text = count
        }
    }

    private fun setupViewmodelForPush() {
        viewModelPushNotification = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[PushNotificationViewModel::class.java]
        setupObserverForPush()
    }

    private fun setupObserverForPush() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelPushNotification.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            isitFirstCount = true
                            binding.prgbarCount.visibility = View.GONE
                            Log.d("Equipments","Check list push: "+it.data)

                            //  renderList(it.data)
                            // binding.recyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            if(!isitFirstCount){
                                binding.prgbarCount.visibility = View.VISIBLE

                            }
                            //  binding.recyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            //Handle Error
                            Log.d("Equipments","Check list: "+it.message)

                            binding.prgbarCount.visibility = View.GONE
                            Toast.makeText(
                                this@DashboardWithFlow,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }

    private fun setupObserverDeviceCount() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelDeviceCount.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            isitFirstCount = true
                            binding.prgbarCount.visibility = View.GONE
                            Log.d("Equipments","Check list: "+it.data)
                            setupDeviceCount(it.data)

                            //  renderList(it.data)
                            // binding.recyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            if(!isitFirstCount){
                                binding.prgbarCount.visibility = View.VISIBLE

                            }
                            //  binding.recyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            //Handle Error
                            Log.d("Equipments","Check list: "+it.message)

                            binding.prgbarCount.visibility = View.GONE
                            Toast.makeText(
                                this@DashboardWithFlow,
                                R.string.error_msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }

    private fun setupDeviceCount(data: DeviceCount) {

        if(data.status.equals("success")){
            binding.countTV.text = data.results!!.total.toString()
            binding.activeEquipTV.text = data.results!!.active.toString()+ " Active"
            binding.percentageTV.text = data.results!!.activePercentage.toString() + "%"
            binding.simpleProgressBar.setProgress(data.results!!.activePercentage!!)
        }

    }

    private fun setupViewModelDeviceCount() {

        val token = "Bearer "+ SessionManager.getToken(this)



        viewModelDeviceCount = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[DeviceCountViewModel::class.java]
        viewModelDeviceCount.fetchTotalDeviceCount(token)


    }

    private fun onClicckListeners() {
        binding.searchLL.setOnClickListener(this)
        binding.logoutLL.setOnClickListener(this)
        binding.notificationRL.setOnClickListener(this)
    }


    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            isitFirstList = true
                            binding.prgbar.visibility = View.GONE
                            Log.d("Equipments","Check list: "+it.data)
                            processLogin(it.data)

                            //  renderList(it.data)
                            // binding.recyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            if(!isitFirstList){
                                binding.prgbar.visibility = View.VISIBLE

                            }
                            //  binding.recyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            //Handle Error
                            Log.d("Equipments","Check list: "+it.message)

                            binding.prgbar.visibility = View.GONE
                            Toast.makeText(
                                this@DashboardWithFlow,
                                R.string.error_msg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    fun processLogin(baseResponse: Equip?) {
        Log.d("ACtivity : ","EXcess Response equipment: "+baseResponse.toString())


        categoryList.clear()
        equipmentList.clear()
        equipmentForSearch.clear()

        if(baseResponse?.results?.size == 0){
            showToast("No data is available!")
        }
        else{

            for (i in baseResponse!!.results){
                //   equipmentForSearch.clear()

                categoryList.add(Category(i.name!!))
                equipmentList.add(i)

                for (j in i.subCategories){
                    /*for (k in j.equipment){
                        equipmentForSearch.add(k)
                    }*/

                    for (k in j.devices){
                        equipmentForSearch.add(k)

                    }

                }


            }


            System.out.println("checking categoryname: "+categoryName)
            if(isFirst == 0){
                isFirst = 1
                //  categoryList[0].isSelected = true
                categoryName = categoryList[0].categoryName
            }

            generateArrary(categoryName)
            /*equipmentAdapter = EquipmentAdapterWithChip(baseResponse.results[0].equipmentSubCategoryList,this,this)
            binding.recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = equipmentAdapter
            }*/

            for (i in categoryList){
                if(i.categoryName.equals(categoryName)){
                    i.isSelected=true
                }
            }


            //    addButton()
            setupRecyclerView()




        }


        // binding.recyclerView.addItemDecoration(StickyHeaderItemDecoration(binding.recyclerView, equipmentAdapter as EquipmentsAdapter))

    }

    private fun setupRecyclerView() {
        val categoryAdapter = CategoryAdapter(this,categoryList,this)



        binding.categoryRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    fun showToast( message: String) {
        val parent: ViewGroup? = null
        val toast = Toast.makeText(this, "", Toast.LENGTH_LONG)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val toastView = inflater.inflate(R.layout.custom_toast, parent)

        val messageTV = toastView.findViewById<TextView>(R.id.successMessage)

        messageTV.text = message
        toast.view = toastView
        toast.show()
    }


    private fun setupViewModel() {
        val token = "Bearer "+ SessionManager.getToken(this)



        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[EquipmentViewModelWithFlow::class.java]
        viewModel.fetchUsers(token)

    }

    override fun onCellClickListener(data: Devices) {

        val intent = Intent(this@DashboardWithFlow, DashboardDataActivity::class.java)
        // Passing the data to the
        // EmployeeDetails Activity
        intent.putExtra(DashboardWithFlow.NEXT_SCREEN,data )


        startActivity(intent)

    }


    companion object{
        val NEXT_SCREEN="details_screen"
    }

    override fun onItemClick(position: Int, adapter: CategoryAdapter, v: View) {


        val clicked_item:Category = categoryList[position]
        // clicked_item.categoryName = "clicked"
        categoryName = clicked_item.categoryName

        if(!clicked_item.isSelected){

            if(position != 0 && categoryList[0].isSelected ){
                categoryList[0].isSelected = false
            }

            clicked_item.isSelected = !clicked_item.isSelected
            if(selectedItem != -1){
                val last_item = categoryList[selectedItem]
                last_item.isSelected = false
            }
            selectedItem = position
            adapter.notifyDataSetChanged()

            generateArrary(clicked_item.categoryName)



        }






    }

    private fun generateArrary(value: String) {


        //  equipmentSubList.clear()
        deviceSubCategoryList.clear()

        for (i in equipmentList){
            if(i.name.equals(value)){
                //  equipmentSubList.addAll(i.equipmentSubCategoryList)
                deviceSubCategoryList.addAll(i.subCategories)
                equipmentAdapter = EquipmentAdapterWithChip(deviceSubCategoryList,this,this)
                binding.recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = equipmentAdapter
                }
            }

        }



    }

    override fun onClick(p0: View?) {

        when(p0!!.id){

            R.id.searchLL ->{
                //  val json = Gson().toJson(equipmentForSearch)


                System.out.println("sdjfhksdf"+equipmentForSearch.size)
                val intent = Intent(this@DashboardWithFlow, SearchActivity::class.java)
                // Passing the data to the
                // EmployeeDetails Activity
                //   intent.putExtra("myList", equipmentForSearch)




                startActivity(intent)

            }

            R.id.notificationRL -> {
                SessionManager.saveNotificationCount(this,"0")
                binding.badgeNotificationTv.visibility = View.GONE

                val intent = Intent(this@DashboardWithFlow, NotificationListActivity::class.java)
                startActivity(intent)



            }

            R.id.logoutLL ->{

                val builder = AlertDialog.Builder(this)
                // builder.setTitle("Androidly Alert")
                builder.setMessage("Do yo want to logout?")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton("Yes") { dialog, which ->
/*                Toast.makeText(context,
                    "Yes", Toast.LENGTH_SHORT).show()*/
                    doLogOut()


                }

                builder.setNegativeButton("No") { dialog, which ->
                    /*               Toast.makeText(context,
                                       "No", Toast.LENGTH_SHORT).show()*/
                    dialog.dismiss()
                }


                builder.show()




            }


        }
    }

    private fun doLogOut() {
        val token = "Bearer "+ SessionManager.getToken(this)

        viewModelLogout = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[UserLogoutViewModel::class.java]
        viewModelLogout.fetchLogoutInfo(token)

        setupObserverForLogout()


    }

    private fun setupObserverForLogout() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelLogout.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.prgbar.visibility = View.GONE
                            Log.d("Equipments","Check list: "+it.data)
                            processLogout(it.data)

                            //  renderList(it.data)
                            // binding.recyclerView.visibility = View.VISIBLE
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
                                this@DashboardWithFlow,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }

    private fun processLogout(data: Logout) {
        if(data?.success!!){

            //finish()
            SessionManager.clearData(this)

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }


    }


    override fun onChipClickListener(
        hardwareId: String?,
        chip: Chip,
        paramValue: String?,
        paramName: String?,
        parameterShortName: String?
    ) {

        Log.d("Check my Status", "Hardware ID"+hardwareId)
        //  view.setBackgroundResource(R.drawable.relay_switch_on)
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.relay_on_solid)));
        chip.setChipStrokeColorResource(R.color.relay_on_border)
        chip.setChipStrokeWidthResource(R.dimen.text_margin__5)


        setupViewModelForRelay(hardwareId, paramValue,paramName,parameterShortName)
        setupObserverForRelay(chip, paramValue,paramName,parameterShortName)



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
                            Log.d("Equipments 123","Check list: "+it.data.status+":::"+paramValue)
                            if (it.data.status == 200 && paramValue.equals("0")){

                                  for (i in deviceSubCategoryList ){
                                       for (j in i.devices){
                                           for (k in j.latestStreamData){
                                               if (k.paramName.equals(paramName)){
                                                   k.parameterValue = "0"
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


                                for (i in deviceSubCategoryList ){
                                    for (j in i.devices){
                                        for (k in j.latestStreamData){
                                            if (k.paramName.equals(paramName)){
                                                k.parameterValue = "1"
                                            }
                                        }
                                    }
                                }

                                chip.text = paramName+" ON"

                                chip.setChipStrokeColorResource(R.color.relay_on_border)
                                chip.setChipStrokeWidthResource(R.dimen.text_margin__5)
                                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.relay_on_solid)));

                            }

                            else{
                                Toast.makeText(
                                    this@DashboardWithFlow,
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
                                this@DashboardWithFlow,
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




        System.out.println("dsfjhsdfd "+token+"dd :"+paramValue+"gg "+parameterShortName+" ff "+hardwareId)

        viewModelRelayCommand = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[RelayCommandViewModel::class.java]
        viewModelRelayCommand.sendCommand(token, paramValue!!,parameterShortName!!,hardwareId!!)


    }

    // notification count
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val menuItem = menu?.findItem(R.id.action_notifications)

        // Inflate your custom layout and set it as the action view for the menu item
        menuItem?.setActionView(R.layout.notification_badge)

        // Get the action view associated with the menu item
        val actionView = menuItem?.actionView

        // Set up the badge with the notification count
        setupBadge(actionView)

        return true
    }

    private fun setupBadge(actionView: View?) {
        actionView?.let {
            val badgeTextView = it.findViewById<TextView>(R.id.badge_text_view)
            val notificationCount = 5 // Replace with your dynamic count

            if (notificationCount > 0) {
                badgeTextView.text = notificationCount.toString()
                badgeTextView.visibility = View.VISIBLE
            } else {
                badgeTextView.visibility = View.GONE
            }

            // Add a click listener if you want the user to navigate to the notification screen
            it.setOnClickListener {
                // Handle notification icon click
                Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }





}