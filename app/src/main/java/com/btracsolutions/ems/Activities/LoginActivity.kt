package com.btracsolutions.ems.Activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.btracsolutions.ems.Model.LoginResponse
import com.btracsolutions.ems.NetworkRepositories.BaseResponse
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Sessions.SessionManager
import com.btracsolutions.ems.Utils.DefaultDispatcherProvider
import com.btracsolutions.ems.Utils.KeyboardUtils
import com.btracsolutions.ems.ViewModel.EquipmentViewModelWithFlow
import com.btracsolutions.ems.ViewModel.LoginViewModel
import com.btracsolutions.ems.ViewModel.UserLoginViewModel
import com.btracsolutions.ems.databinding.ActivityLoginBinding
import com.btracsolutions.flowwithapi.Api.ApiHelperImpl
import com.btracsolutions.flowwithapi.Api.RetrofitBuilder
import com.btracsolutions.flowwithapi.Api.UiState
import com.btracsolutions.flowwithapi.ViewModel.ViewModelFactory
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    var isitFirst = true
    private lateinit var viewModel: UserLoginViewModel


   // private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)







      //  removeLinksUnderline(binding.termAndConditionsTV.text)
        binding.termAndConditionsTV.movementMethod = LinkMovementMethod.getInstance()





        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }

        val root = findViewById<ConstraintLayout>(R.id.content_id)
        binding.btnLogin.setOnClickListener(this)

        /*viewModel.loginResult.observe(this) {
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


        fun dpToPx(dp: Int, activity: FragmentActivity?): Int {
            if (activity != null) {
                val displaymetrics = DisplayMetrics()
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics)
                return Math.round(dp * (displaymetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
            }
            return 0
        }
        KeyboardUtils.addKeyboardToggleListener(
            this,
            object : KeyboardUtils.SoftKeyboardToggleListener {
                override fun onToggleSoftKeyboard(isVisible: Boolean) {
                    Log.d("keyboard", "keyboard visible: $isVisible")

                    // Toast.makeText(getContext(), "VISIBLITY : "+isVisible, Toast.LENGTH_SHORT).show();
                    if (isitFirst) {
                        isitFirst = false
                        return
                    } else {
                        if (isVisible) {
                            val metrics = resources.displayMetrics
                            val height = metrics.heightPixels
                            val translationY = ObjectAnimator.ofFloat(
                                root,
                                "translationY",
                                dpToPx(-180, this@LoginActivity).toFloat()
                            ) // metrics.heightPixels or root.getHeight()
                            translationY.duration = 500
                            println("showkeybaord geo 1")
                            translationY.start()
                            translationY.addListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                }
                            })
                        } else {
                            val displayMetrics = resources.displayMetrics
                            getWindowManager().getDefaultDisplay()
                                .getMetrics(displayMetrics)
                            val height = displayMetrics.heightPixels
                            println("showkeybaord geo $height")
                            val translationY = ObjectAnimator.ofFloat(
                                root,
                                "translationY",
                                dpToPx(30, this@LoginActivity).toFloat()
                            )
                            translationY.duration = 500
                            translationY.start()
                            translationY.addListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                }
                            })
                        }
                    }
                }
            })




    }

    private fun Spannable.removeAllUrlSpanUnderline() {
        for (urlSpan in getSpans(0, length, URLSpan::class.java)) {
            setSpan(object : UnderlineSpan() {
                override fun updateDrawState(tp: TextPaint) {
                    tp.isUnderlineText = false
                }
            }, getSpanStart(urlSpan), getSpanEnd(urlSpan), 0)
        }
    }


    fun showLoading() {
        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        binding.prgbar.visibility = View.GONE
    }

    fun processLogin(data: LoginResponse?) {
        showToast("Login Successfully")
        if (!data?.data?.accessToken.isNullOrEmpty()) {
            data?.data?.accessToken?.let { SessionManager.saveAuthToken(this, it) }

            if(!data?.results?.user?.name.isNullOrEmpty()){
                SessionManager.saveUserName(this,data?.results?.user?.name!!)
            }

            if(!data?.results?.user?.email.isNullOrEmpty()){
                SessionManager.saveUserEmail(this,data?.results?.user?.email!!)
            }
            if (!data?.results?.user?.phone.isNullOrEmpty()){
                SessionManager.saveUserPhone(this,data?.results?.user?.phone!!)

            }

            navigateToHome()
        }




    }

    fun processError(msg: String?) {
        showToast("Invalid email and password")
    }

    /*fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }*/

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

    private fun navigateToHome() {
     //Toast.makeText()
        //val intent = Intent(this, Dashboard::class.java)
        val intent = Intent(this, DashBoardEquipment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onClick(p0: View?) {


        when(p0?.id){

            R.id.btn_login -> {
                doLogin()
            }
        }
    }

    fun doLogin() {
        val email = binding.txtInputEmail.text.toString()
        val pwd = binding.txtPass.text.toString()
        //viewModel.loginUser(email = email, pwd = pwd)


        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService),
                DefaultDispatcherProvider()
            )
        )[UserLoginViewModel::class.java]
        viewModel.fetchLoginInfo(email,pwd)

        setupObserver()

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
                                this@LoginActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

    }

}