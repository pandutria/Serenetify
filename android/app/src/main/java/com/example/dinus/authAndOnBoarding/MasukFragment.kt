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
    import com.example.dinus.local.SharedPreferencesHelper
    import com.example.dinus.model.User
    import com.example.dinus.network.ApiClient
    import com.example.dinus.network.ApiService
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response

    class MasukFragment : Fragment() {

        private val TAG = "MasukFragment"
        private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            val view = inflater.inflate(R.layout.fragment_masuk, container, false)

            val etEmail = view.findViewById<EditText>(R.id.etEmail)
            val etPassword = view.findViewById<EditText>(R.id.rtPassword)
            val progressBar = view.findViewById<ProgressBar>(R.id.loadingBar)
            val btnSignIn = view.findViewById<Button>(R.id.btnSignIn)

            val tvForgotPassword = view.findViewById<TextView>(R.id.tvLupaKataSandi)
            val tvSignUp = view.findViewById<TextView>(R.id.tvSignUp)

            tvForgotPassword.paintFlags = tvForgotPassword.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            tvSignUp.paintFlags = tvSignUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            view.findViewById<TextView>(R.id.tvSignUp).setOnClickListener {
                findNavController().navigate(R.id.action_masuk_to_daftar)
            }

            etEmail.setText("pandujakarta173@gmail.com")
            etPassword.setText("jakarta123")

            sharedPreferencesHelper = SharedPreferencesHelper(requireContext())

            progressBar.visibility = View.GONE

            btnSignIn.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                progressBar.visibility = View.VISIBLE
                btnSignIn.visibility = View.GONE


                val user = User(email = email, password = password)
                ApiClient.retrofit.create(ApiService::class.java)
                    .getUsers()
                    .enqueue(object : Callback<List<User>> {
                        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                            progressBar.visibility = View.GONE
                            btnSignIn.visibility = View.VISIBLE
                            if (response.isSuccessful) {
                                val users = response.body()
                                Log.d(TAG, "Login successful: $users")

                                val matchedUser = users?.find { it.email == email }

                                if (matchedUser != null) {
                                    sharedPreferencesHelper.saveUserId(matchedUser.id)
                                    Toast.makeText(requireContext(), "Selamat datang, ${matchedUser.name}!", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.action_masuk_to_main)
                                } else {
                                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.e(TAG, "Login failed: $errorBody")
                                progressBar.visibility = View.GONE
                                btnSignIn.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "Login failed: $errorBody", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            Log.e(TAG, "Error: ${t.message}", t)
                            progressBar.visibility = View.GONE
                            btnSignIn.visibility = View.VISIBLE
                            Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })

            }

            return view
        }
    }