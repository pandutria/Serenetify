package com.example.dinus.bottomnavbar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.DoctorAdapter
import com.example.dinus.model.Doctor
import com.example.dinus.network.ApiClient
import com.example.dinus.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultationFragment : Fragment() {

    private lateinit var rvDoctor: RecyclerView
    private lateinit var doctorAdapter: DoctorAdapter
    private val doctorList = mutableListOf<Doctor>()
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_consultation, container, false)

        rvDoctor = view.findViewById(R.id.rvDoctor)
        doctorAdapter = DoctorAdapter(doctorList)

        rvDoctor.layoutManager = LinearLayoutManager(requireContext())
        rvDoctor.adapter = doctorAdapter

        fetchDoctorsFromApi()

        return view
    }

    private fun fetchDoctorsFromApi() {
        Log.d("FetchData", "Fetching doctors from API...")
        apiService.getDoctors().enqueue(object : Callback<List<Doctor>> {
            override fun onResponse(call: Call<List<Doctor>>, response: Response<List<Doctor>>) {
                Log.d("FetchData", "Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val doctors = response.body() ?: emptyList()
                    Log.d("FetchData", "Doctors fetched: $doctors")

                    if (doctors.isNotEmpty()) {
                        doctorList.clear()
                        doctorList.addAll(doctors)
                        doctorAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("FetchData", "No doctors found")
                        Toast.makeText(requireContext(), "No doctors found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("FetchData", "Failed to load doctors: ${response.code()}")
                    Toast.makeText(requireContext(), "Failed to load doctors", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Doctor>>, t: Throwable) {
                Log.e("FetchData", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Failed to fetch data: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
