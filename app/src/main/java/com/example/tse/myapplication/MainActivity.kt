package com.example.tse.myapplication


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Explode
import android.transition.Slide
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var adapter: ProductAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        setSupportActionBar(app_bar)
        
        val products = readProductsList()

        val imageRequester = ImageRequester.getInstance(this)

        // this code is for headerProduct
        val headerProduct = getHeaderPorduct(products)
        imageRequester.setImageFromUrl(app_bar_image, headerProduct.url)

        product_list.setHasFixedSize(true)
        product_list.layoutManager = GridLayoutManager(this, resources.getInteger(R.integer.shr_column_count))
        adapter = ProductAdapter(products, imageRequester)
        product_list.adapter = adapter


        bottom_navigation.setOnNavigationItemSelectedListener {
            val layoutManager = product_list.layoutManager as LinearLayoutManager
            layoutManager.scrollToPositionWithOffset(0,0)
            shuffleProducts()
            true

        }


        bottom_navigation.setOnNavigationItemReselectedListener {
            val layoutManager = product_list.layoutManager as LinearLayoutManager
            layoutManager.scrollToPositionWithOffset(0,0)
        }


        if (savedInstanceState == null) {
            bottom_navigation.selectedItemId = R.id.category_home
        }



    }


    private fun getHeaderPorduct(products: List<ProductEntry>): ProductEntry {

        if (products.isEmpty()) {
            throw IllegalArgumentException("There must be at least one product")
        }

        for (i in products.indices) {
            if ("Perfect Goldfish Bowl" == products[i].title){
                return products[i]
            }
        }
        return products[0]
    }

    private fun  readProductsList(): ArrayList<ProductEntry> {
        val inputStream = resources.openRawResource(R.raw.products)
        val productListType = object : TypeToken<ArrayList<ProductEntry>>() {
            
        }.type
        try {
            return JsonReader.readJsonStream<ArrayList<ProductEntry>>(inputStream, productListType)
        } catch (e: IOException) {
            Log.e(TAG, "Error reading Json product list", e)
            return ArrayList()
        }
    }



    private class ProductAdapter internal constructor(private var products: List<ProductEntry>, private val imageRequester: ImageRequester) : RecyclerView.Adapter<ProductViewHolder>(){
        internal fun setProducts(products: List<ProductEntry>) {
            this.products = products
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(viewGrop: ViewGroup, i: Int): ProductViewHolder {
            return ProductViewHolder(viewGrop)
        }

        override fun onBindViewHolder(viewHolder: ProductViewHolder, i: Int) {
            viewHolder.bind(products[i], imageRequester)
        }


        override fun getItemCount(): Int {
            return products.size
        }

    }


    private class ProductViewHolder internal constructor(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
      R.layout.shr_product_entry, parent, false)){
        private val imageView: NetworkImageView
        private val priceView: TextView

        private val clickListener = View.OnClickListener {
            v -> val product = v.getTag(R.id.tag_product_entry) as ProductEntry

            //Log.e("Producto:  ", "" + product)
           // val intent: Intent = Intent(parent.context, ProductDetail::class.java)
           // startActivity(parent.context,intent,null)

            val intent = Intent(parent.context, ProductDetail::class.java).apply {
                putExtra(ProductDetail.EXTRA_PRODUCT_TITLE, product.title)
                putExtra(ProductDetail.EXTRA_PRODUCT_URL, product.url)
                putExtra(ProductDetail.EXTRA_PRODUCT_PRICE, product.price)
                putExtra(ProductDetail.EXTRA_PRODUCT_DESCRIPTION, product.description)
            }

            val activity: Activity

            activity = parent.context as Activity

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                val explode: Explode = Explode()



                explode.duration = 1000



                activity.window.exitTransition = explode


                activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(activity,v, activity.getString(R.string.shareImage)).toBundle())



            }else{
                startActivity(parent.context,intent,null)
            }






        }

        init {
            imageView = itemView.findViewById(R.id.image) as NetworkImageView
            priceView = itemView.findViewById(R.id.price) as TextView
            itemView.setOnClickListener(clickListener)
        }

        internal fun bind(product: ProductEntry, imageRequester: ImageRequester) {
            itemView.setTag(R.id.tag_product_entry, product)
            imageRequester.setImageFromUrl(imageView, product.url)
            priceView.text = product.price
        }


    }


    private fun shuffleProducts(){
        val products = readProductsList()
        Collections.shuffle(products)
        adapter?.setProducts(products)
    }


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
