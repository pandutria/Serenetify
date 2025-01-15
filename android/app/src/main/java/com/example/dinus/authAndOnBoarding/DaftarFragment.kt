package com.example.dinus.authAndOnBoarding

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dinus.R
import com.example.dinus.model.User
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarFragment : Fragment() {

    private val TAG = "DaftarFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daftar, container, false)

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etPhoneNumber = view.findViewById<EditText>(R.id.etPhoneNumber)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)

        val progressBar = view.findViewById<ProgressBar>(R.id.loadingBar)
        val btnRegist = view.findViewById<Button>(R.id.btnRegist)

        val tvSignIn = view.findViewById<TextView>(R.id.tvSignIn)

        tvSignIn.paintFlags = tvSignIn.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        view.findViewById<TextView>(R.id.tvSignIn).setOnClickListener {
            findNavController().navigate(R.id.action_daftar_to_masuk)
        }

        progressBar.visibility = View.GONE

        btnRegist.setOnClickListener {
            val email = etEmail.text.toString()
            val name = etName.text.toString()
            val phoneNumber = etPhoneNumber.text.toString()
            val password = etPassword.text.toString()

            // Create User object with role set to "user"
            val user = User(
                email = email,
                name = name,
                phone_number = phoneNumber,
                password = password,
                role = "user"  // Set role as "user"
            )
            progressBar.visibility = View.VISIBLE
            btnRegist.visibility = View.GONE

            ApiClient.retrofit.create(ApiService::class.java)
                .register(user)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            progressBar.visibility = View.GONE
                            btnRegist.visibility = View.VISIBLE
                            Log.d(TAG, "Registration successful: ${response.body()}")
                            Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_daftar_to_masuk)
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e(TAG, "Registration failed: $errorBody")
                            progressBar.visibility = View.GONE
                            btnRegist.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), "Registration failed: $errorBody", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e(TAG, "Error: ${t.message}", t)
                        progressBar.visibility = View.GONE
                        btnRegist.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }


        return view
    }
}