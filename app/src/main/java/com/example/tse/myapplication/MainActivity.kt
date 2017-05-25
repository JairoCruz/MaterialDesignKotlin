package com.example.tse.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
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
        product_list.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(products, imageRequester)
        product_list.adapter = adapter



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


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
