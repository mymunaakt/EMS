package com.btracsolutions.ems.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.withStarted
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.ems.Adapters.CategoryAdapter
import com.btracsolutions.ems.Adapters.EquipmentAdapter
import com.btracsolutions.ems.Model.*
import com.btracsolutions.ems.NetworkRepositories.BaseResponse
import com.btracsolutions.ems.NetworkRepositories.RetrofitService
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Sessions.SessionManager
import com.btracsolutions.ems.ViewModel.EquipmentViewModelT
import com.btracsolutions.ems.databinding.ActivityDashboardBinding




class Dashboard : AppCompatActivity(),CategoryAdapter.OnItemClickListener,EquipmentAdapter.CellClickListener,View.OnClickListener{

    private lateinit var binding: ActivityDashboardBinding
  //  lateinit var viewModel: EquipmentViewModel
    private val TAG = "MainActivity"

    var selectedItem : Int = -1
    var isFirst: Int = 0

    private val retrofitService = RetrofitService.getInstance()
    private lateinit var equipmentAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    val categoryList = ArrayList<Category>()
    val equipmentList = ArrayList<Results>()
    private val viewModel by viewModels<EquipmentViewModelT>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val token = "Bearer "+SessionManager.getToken(this)

        viewModel.equipmentList(token = token)

        binding.userImage.setOnClickListener(this)

      //  binding.recyclerView.adapter = equipmentAdapter
        viewManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)

        viewModel.equipmentResult.observe(this) {
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
        }




        //for testing purpose
   /*     for (i in 1..20) {
            categoryList.add(Category("Item " + i,false))
        }
        setupRecyclerView()*/

        //for testing purpose

    }

    fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    fun processLogin(baseResponse: Equipments?) {
       Log.d("ACtivity : ","EXcess Response equipment: "+baseResponse.toString())



        if(baseResponse?.results?.size == 0){
            showToast("No data is available!")
        }
        else{

            for (i in baseResponse!!.results){

                categoryList.add(Category(i.name!!))
                equipmentList.add(i)

            }

            if(isFirst == 0){
                isFirst == 1
                categoryList[0].isSelected = true
            }

          //  equipmentAdapter = EquipmentAdapterNew(baseResponse.results[0].equ,this)
            binding.recyclerView.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = equipmentAdapter
            }


            //    addButton()
            setupRecyclerView()




        }


        // binding.recyclerView.addItemDecoration(StickyHeaderItemDecoration(binding.recyclerView, equipmentAdapter as EquipmentsAdapter))

    }

    private fun setupRecyclerView() {
       val categoryAdapter = CategoryAdapter(this,categoryList,this)
      //  recyclerview.layoutManager = LinearLayoutManager(this)
       // recyclerview.adapter = adapter
       // recyclerview.setHasFixedSize(true)


        binding.categoryRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

/*
    private fun addButton() {
        val horizontalScrollView = HorizontalScrollView(this)
        //setting height and width
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        horizontalScrollView.layoutParams = layoutParams

        val linearLayout = LinearLayout(this)
        //setting height and width
        val linearParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)



        linearLayout.layoutParams = linearParams

        //adding horizontal scroll view to the layout
        horizontalScrollView.addView(linearLayout)





        for (item in categoryList){

            println(item)


            val button = Button(this)
            val layoutPArams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val margin = resources.getDimension(R.dimen.text_margin).toInt()

            layoutPArams.setMargins(margin, margin, margin, margin)
            button.layoutParams = layoutPArams
          //
            button.gravity = Gravity.CENTER
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)




            button.text = item
            button.setTag(item)


            button.setBackgroundResource(R.drawable.btn_shape_round)


            linearLayout.addView(button)
            button.setOnClickListener(View.OnClickListener {

                it.setBackgroundResource(R.drawable.btn_shape_round_filled)

                generateArrary(button.getTag().toString())


            })
        }

        binding.rootLayout.addView(horizontalScrollView)


    }*/
/*
    private fun generateArrary(value: String) {


        for (i in equipmentList){
            if(i.name.equals(value)){
                equipmentAdapter = EquipmentAdapter(i.equipmentSubCategoryList,this)
                binding.recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = equipmentAdapter
                }
            }

        }



    }*/

    fun processError(msg: String?) {
        Log.d("ACtivity : ","EXcess Error equipment: "+msg)
        showToast("Some errors have been found. Please Try again Later!" )


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

    override fun onItemClick(position: Int, adapter: CategoryAdapter, v: View) {
       // categoryList[position].isSelected = true
       // adapter.notifyDataSetChanged()

     /*   val clicked_item:Category = categoryList[position]
        clicked_item.isSelected = !clicked_item.isSelected
      if(clicked_item.isSelected){
            binding.categoryRV.getChildAt(binding.categoryRV.indexOfChild(v)).setBackgroundColor(
                Color.YELLOW)
            binding.categoryRV.getChildAt(binding.categoryRV.indexOfChild(v)).findViewById<ConstraintLayout>(R.id.constraintCategoryItem).setBackgroundColor(Color.YELLOW)
        }else{
            binding.categoryRV.getChildAt( binding.categoryRV.indexOfChild(v)).setBackgroundColor(Color.WHITE)
            binding.categoryRV.getChildAt( binding.categoryRV.indexOfChild(v)).findViewById<ConstraintLayout>(R.id.constraintCategoryItem).setBackgroundColor(Color.WHITE)
        }


                if(lastSelectedItem != -1){
            val last_item = categoryList[lastSelectedItem]
            last_item.isSelected = false;
        }
        lastSelectedItem = position;
        adapter.notifyDatasetChanged();


*/





        val clicked_item:Category = categoryList[position]
        // clicked_item.categoryName = "clicked"

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

            //generateArrary(clicked_item.categoryName)



        }






    }

    override fun onCellClickListener(data: Equipment) {

        val intent = Intent(this@Dashboard, DashboardDataActivity::class.java)
        // Passing the data to the
        // EmployeeDetails Activity
        intent.putExtra(NEXT_SCREEN,data )


        startActivity(intent)

    }

    companion object{
        val NEXT_SCREEN="details_screen"
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.userImage ->{

                val intent = Intent(this@Dashboard, UserProfile::class.java)
                // Passing the data to the
                // EmployeeDetails Activity



                startActivity(intent)
            }


        }

    }

}