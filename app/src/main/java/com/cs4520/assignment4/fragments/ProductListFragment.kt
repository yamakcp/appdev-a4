package com.cs4520.assignment4.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4520.assignment4.R
import com.cs4520.assignment4.database.ProductDatabase
import com.cs4520.assignment4.databinding.ProductListFragmentBinding
import com.cs4520.assignment4.adapters.ProductListAdapter
import com.cs4520.assignment4.viewmodels.ProductsListsViewModel
import com.cs4520.assignment4.models.ProductsRepository

class ProductListFragment : Fragment(R.layout.product_list_fragment) {
    private lateinit var binding: ProductListFragmentBinding
    private lateinit var adapter: ProductListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ProductsListsViewModel

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProductListFragmentBinding.bind(view)
        viewModel =
            ProductsListsViewModel(ProductsRepository(
                ProductDatabase.getInstance(requireContext()).productDao(),),)

        adapter = ProductListAdapter(emptySet())
        recyclerView = binding.productList
        binding.productList.adapter = adapter
        binding.productList.layoutManager = LinearLayoutManager(view.context)

        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateProducts(products)
            binding.productList.visibility = View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            binding.noProductsMessage.text = message
            binding.noProductsMessage.visibility = View.VISIBLE
            binding.reloadButton.visibility = View.VISIBLE
        }

        viewModel.isFetching.observe(viewLifecycleOwner) { isFetching ->
            if (isFetching) {
                binding.productList.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.reloadButton.visibility = View.GONE
                binding.noProductsMessage.visibility = View.GONE
            }
            binding.progressBar.visibility = View.GONE

        }

        viewModel.hasPrev.observe(viewLifecycleOwner) { hasPrev ->
            binding.paginationControls.previousButton.isEnabled = hasPrev
        }

        viewModel.pageNumber.observe(viewLifecycleOwner) { page ->
            binding.paginationControls.pageNumber.text = page.toString()
        }

        viewModel.hasNext.observe(viewLifecycleOwner) { hasNext ->
            binding.paginationControls.nextButton.isEnabled = hasNext
        }

        binding.paginationControls.nextButton.setOnClickListener {
            viewModel.nextPage()
        }

        binding.paginationControls.previousButton.setOnClickListener {
            viewModel.prevPage()
        }


        binding.reloadButton.setOnClickListener {
            viewModel.fetchProducts()
        }

        viewModel.fetchProducts()
    }
}
