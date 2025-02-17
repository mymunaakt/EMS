package com.btracsolutions.ems.Activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.btracsolutions.ems.R
import com.btracsolutions.ems.databinding.ActivityAboutBinding


class AboutActivity: AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        OnClicklisteners()
    }

    private fun OnClicklisteners() {
        binding.backLL.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.backLL->{
                finish()
            }
        }
    }

}