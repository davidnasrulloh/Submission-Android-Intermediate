package com.davidnasrulloh.sm_intermediate_david.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.davidnasrulloh.sm_intermediate_david.R
import com.davidnasrulloh.sm_intermediate_david.databinding.FragmentLoginBinding
import com.davidnasrulloh.sm_intermediate_david.ui.main.MainActivity
import com.davidnasrulloh.sm_intermediate_david.ui.main.MainActivity.Companion.EXTRA_TOKEN
import com.davidnasrulloh.sm_intermediate_david.utils.animateVisibility
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding : FragmentLoginBinding
    private var loginJob: Job = Job()
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        setActions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setActions(){
        binding.apply {
            btnToRegister.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
            )

            btnLogin.setOnClickListener {
                handleLogin()
            }
        }
    }

    private fun handleLogin(){
        val email = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()
        setLoadingState(true)

        lifecycleScope.launchWhenResumed {
            if(loginJob.isActive) loginJob.cancel()

            loginJob = launch {
                viewModel.userLogin(email, password).collect{ result ->
                    result.onSuccess { credentials ->
                        credentials.loginResult?.token?.let { token ->
                            viewModel.saveAuthToken(token)
                            Intent(requireContext(), MainActivity::class.java).also { intent ->
                                intent.putExtra(EXTRA_TOKEN, token)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }

                        Toast.makeText(
                            requireContext(),
                            getString(R.string.login_success_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    result.onFailure {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.login_error_message),
                            Snackbar.LENGTH_SHORT
                        ).show()

                        setLoadingState(false)
                    }
                }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean){
        binding.apply {
            edLoginEmail.isEnabled = !isLoading
            edLoginPassword.isEnabled = !isLoading
            btnLogin.isEnabled = !isLoading

            if(isLoading){
                viewLoading.animateVisibility(true)
            } else {
                viewLoading.animateVisibility(false)
            }
        }
    }

}