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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.btracsolutions.ems.Model.Logout
import com.btracsolutions.ems.NetworkRepositories.BaseResponse
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Sessions.SessionManager
import com.btracsolutions.ems.Utils.DefaultDispatcherProvider
import com.btracsolutions.ems.ViewModel.EquipmentViewModelT
import com.btracsolutions.ems.ViewModel.LogoutViewModel
import com.btracsolutions.ems.ViewModel.UserLoginViewModel
import com.btracsolutions.ems.ViewModel.UserLogoutViewModel
import com.btracsolutions.ems.databinding.ActivityDashboardBinding
import com.btracsolutions.ems.databinding.ActivityUserProfileBinding
import com.btracsolutions.flowwithapi.Api.ApiHelperImpl
import com.btracsolutions.flowwithapi.Api.RetrofitBuilder
import com.btracsolutions.flowwithapi.Api.UiState
import com.btracsolutions.flowwithapi.ViewModel.ViewModelFactory
import kotlinx.coroutines.launch

class UserProfile: AppCompatActivity(),View.OnClickListener {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var viewModel: UserLogoutViewModel

    //  private val viewModel by viewModels<LogoutViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        OnClicklisteners()
    }

    private fun initData() {
        binding.userNameTV.text = SessionManager.getUserName(this)
        binding.userEmailTV.text = "Email: "+SessionManager.getUserEmail(this)
        binding.userMobileTV.text = "Phone No: "+SessionManager.getUserPhone(this)
    }

    private fun OnClicklisteners() {
        binding.logoutRL.setOnClickListener(this)
        binding.backLL.setOnClickListener(this)
        binding.aboutRL.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.logoutRL ->{
                doLogOut()
            }
            R.id.backLL ->{
                finish()
            }
            R.id.aboutRL ->{

                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun doLogOut() {
        val token = "Bearer "+ SessionManager.getToken(this)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[UserLogoutViewModel::class.java]
        viewModel.fetchLogoutInfo(token)

        setupObserver()


        /* viewModel.userLogout(token = token)

         viewModel.logoutResult.observe(this) {
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

 */
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.prgbar.visibility = View.GONE
                            Log.d("Equipments","Check list: "+it.data)
                            processLogin(it.data)

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
                                this@UserProfile,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }


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







    private fun processLogin(data: Logout?) {

        if(data?.success!!){

            //finish()
            SessionManager.clearData(this)

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }

    }

    fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }


}