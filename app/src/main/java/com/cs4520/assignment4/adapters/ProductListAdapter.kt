package com.cs4520.assignment4.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cs4520.assignment4.models.Product
import com.cs4520.assignment4.databinding.ElementBinding
import com.cs4520.assignment4.R

class ProductListAdapter(private var products: Set<Product>) :
    RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {
    class ProductViewHolder(binding: ElementBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val elementView: View = ElementBinding.bind(binding.root).element
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, )
    : ProductViewHolder {

        val infl = LayoutInflater.from(parent.context)
        val bind = ElementBinding.inflate(infl, parent, false)
        return ProductViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int, ) {

        val binding = ElementBinding.bind(holder.elementView)
        when (val product = products.elementAt(position)) {
            is Product.Food -> {
                binding.elementName.text = product.name
                binding.elementDate.text = product.expiryDate
                binding.elementPrice.text = "$%.2f".format(product.price)

                binding.element.setBackgroundResource(R.color.food_color)
                binding.elementImage.setImageResource(R.drawable.food)
            }

            is Product.Equipment -> {
                binding.elementName.text = product.name
                binding.elementDate.text = ""
                binding.elementPrice.text = "$%.2f".format(product.price)

                binding.element.setBackgroundResource(R.color.equipment_color)
                binding.elementImage.setImageResource(R.drawable.equipment)
            }
        }
    }

    fun updateProducts(newProducts: Set<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
