package com.example.tse.myapplication

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetail : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // set up action bar
        val toolbar = findViewById(R.id.app_bar_detail) as Toolbar
        setSupportActionBar(toolbar)
        // set up navigation up
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setTitle(intent.getStringExtra(EXTRA_PRODUCT_TITLE))
        }

        // Get the product
        val product_title = intent.getStringExtra(EXTRA_PRODUCT_TITLE)
        val product_url = intent.getStringExtra(EXTRA_PRODUCT_URL)
        val product_price = intent.getStringExtra(EXTRA_PRODUCT_PRICE)
        val product_description = intent.getStringExtra(EXTRA_PRODUCT_DESCRIPTION)


        val imageView: NetworkImageView = findViewById(R.id.app_bar_image_detail) as NetworkImageView

        val imageRequester = ImageRequester.getInstance(this)
        imageRequester.setImageFromUrl(imageView, product_url)


        val priceView =  findViewById(R.id.txtPrice) as TextView
        val descriptionView = findViewById(R.id.txtDescription) as TextView
        priceView.text = product_price
        descriptionView.text = product_description




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //val slide: Slide = Slide()
            //slide.duration = 500
            //window.enterTransition = slide

            window.returnTransition = Fade()
        }








    }

    // navigation up work with supportActionBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    companion object {
        val EXTRA_PRODUCT_TITLE = "TITLE"
        val EXTRA_PRODUCT_URL = "URL"
        val EXTRA_PRODUCT_PRICE = "PRICE"
        val EXTRA_PRODUCT_DESCRIPTION = "DESCRIPTION"

        val NAME_ACTIVITY = ProductDetail::class.java.simpleName
    }
}
