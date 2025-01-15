package com.example.dinus.feature.quotes

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.adapter.QuoteAdapter
import com.example.dinus.model.Quote
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuoteActivity : AppCompatActivity() {

    private lateinit var quoteAdapter: QuoteAdapter
    private var quoteList: MutableList<Quote> = mutableListOf()

    private lateinit var databaseReferenceQuote: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quote)
        enableEdgeToEdge()

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val rvQuote: RecyclerView = findViewById(R.id.rvQuotes)
        rvQuote.layoutManager = GridLayoutManager(this, 2)
        quoteAdapter = QuoteAdapter(quoteList)
        rvQuote.adapter = quoteAdapter

        databaseReferenceQuote = FirebaseDatabase.getInstance("https://dinus-eecd8-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("quote")

        fetchDataQuote()
    }

    private fun fetchDataQuote() {
        databaseReferenceQuote.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                quoteList.clear()
                for (quoteSnapshot in dataSnapshot.children) {
                    val quote = quoteSnapshot.getValue(Quote::class.java)
                    quote?.let { quoteList.add(it) }
                }
                quoteAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
