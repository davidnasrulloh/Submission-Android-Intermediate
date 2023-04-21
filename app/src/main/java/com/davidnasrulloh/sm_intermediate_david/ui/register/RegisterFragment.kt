package com.davidnasrulloh.sm_intermediate_david.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.davidnasrulloh.sm_intermediate_david.R
import com.davidnasrulloh.sm_intermediate_david.databinding.FragmentRegisterBinding
import com.davidnasrulloh.sm_intermediate_david.utils.animateVisibility
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private var registerJob: Job = Job()

    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    private fun setActions(){
        binding.apply {
            btnToLogin.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_registerFragment_to_loginFragment)
            )

            btnRegister.setOnClickListener{
                handleRegister()
            }
        }
    }

    private fun handleRegister(){
        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString()
        setLoadingState(true)

        lifecycleScope.launchWhenResumed {
            registerJob = launch {
                viewModel.userRegister(name, email, password).collect{ result ->
                    result.onSuccess {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.registration_success),
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }

                    result.onFailure {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.registration_error_message),
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
            edRegisterEmail.isEnabled = !isLoading
            edRegisterPassword.isEnabled = !isLoading
            edRegisterName.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading

            if(isLoading){
                viewLoading.animateVisibility(true)
            } else {
                viewLoading.animateVisibility(false)
            }
        }
    }


}