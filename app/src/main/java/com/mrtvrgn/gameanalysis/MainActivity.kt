package com.mrtvrgn.gameanalysis

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mrtvrgn.gameanalysis.screen.basketball.BasketballHeatMapActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        basketball.setOnClickListener {
            startActivity(Intent(this, BasketballHeatMapActivity::class.java))
        }
    }
}
