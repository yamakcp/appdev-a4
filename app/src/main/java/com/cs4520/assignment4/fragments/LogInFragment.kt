package com.cs4520.assignment4.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.cs4520.assignment4.R
import com.cs4520.assignment4.viewmodels.LogInViewModel
import com.cs4520.assignment4.databinding.LoginFragmentBinding


import com.google.android.material.snackbar.Snackbar


class LogInFragment : Fragment(R.layout.login_fragment) {

    private lateinit var viewModel: LogInViewModel
    private lateinit var bind: LoginFragmentBinding

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        bind = LoginFragmentBinding.bind(view)
        viewModel = LogInViewModel()

        bind.loginButton.setOnClickListener {
            viewModel.tryLogin(bind.username.text.toString(), bind.password.text.toString())
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            Navigation.findNavController(bind.root).navigate(R.id.login_action)
            bind.username.text.clear()
            bind.password.text.clear()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->

            val snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
            snackBar.setAnchorView(bind.username)
            snackBar.show()
        }

    }
}
