package com.btracsolutions.ems.Notification

import NotificationRepository
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.btracsolutions.ems.R
import com.btracsolutions.ems.Sessions.SessionManager
import com.btracsolutions.ems.databinding.ActivityDashboardWithFlowBinding
import com.btracsolutions.ems.databinding.ActivityNotificationListBinding
import com.btracsolutions.flowwithapi.Api.RetrofitBuilder
import com.btracsolutions.lazyloading.NotificationViewModelFactory
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationListActivity: AppCompatActivity(),View.OnClickListener {
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var binding: ActivityNotificationListBinding


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_dashboard)
        binding = ActivityNotificationListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backLL.setOnClickListener(this)

        val repository = NotificationRepository(RetrofitBuilder.apiService)
        val factory = NotificationViewModelFactory(repository)
        notificationViewModel = ViewModelProvider(this, factory).get(NotificationViewModel::class.java)

       // val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = NotificationAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addItemDecoration(RecyclerViewItemDecoration(this, R.drawable.divider))

        binding.progressBar2.visibility = View.VISIBLE

        val token = "Bearer "+ SessionManager.getToken(this)

        lifecycleScope.launch {
            notificationViewModel.notif(token).collectLatest {

                binding.progressBar2.visibility = View.GONE

                adapter.submitData(it)
                System.out.println("testst xcxc "+it)

                // Add a LoadStateListener to check for empty data
                adapter.addLoadStateListener { loadState ->
                    // Show message and hide RecyclerView if data is empty
                    val isListEmpty = adapter.itemCount == 0
                    if (isListEmpty) {
                        binding.recyclerView.visibility = View.GONE
                        binding.noDataTextView.visibility = View.VISIBLE // Show "No data available" message
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.noDataTextView.visibility = View.GONE // Hide message when data is available
                    }

                    // Handle error state if necessary
                    if (loadState.refresh is LoadState.Error) {
                        val error = (loadState.refresh as LoadState.Error).error
                        Toast.makeText(this@NotificationListActivity, R.string.error_msg, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }




    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.backLL ->{
                finish()
            }
        }

    }
}