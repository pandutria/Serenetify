package com.example.dinus.bottomnavbar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.dinus.R
import com.example.dinus.authAndOnBoarding.AuthActivity

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            val i = Intent(requireContext(), AuthActivity::class.java)
            startActivity(i)
        }

        return view

    }
}