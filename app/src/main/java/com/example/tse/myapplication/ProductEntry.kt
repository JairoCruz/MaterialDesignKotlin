package com.example.tse.myapplication

/**
 * Created by TSE on 24/5/2017.
 */
class ProductEntry(val title: String, val url: String, val price: String, val description: String) {
    override fun toString(): String {
        return "ProductEntry(title='$title', url='$url', price='$price', description='$description')"
    }
}