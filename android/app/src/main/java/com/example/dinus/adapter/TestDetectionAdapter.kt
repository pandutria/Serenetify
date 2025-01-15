package com.example.dinus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dinus.R
import com.example.dinus.model.TestDetection

class TestDetectionAdapter(
    private val testList: List<TestDetection>,
    private val onAnswerSelected: (position: Int, points: Int) -> Unit
) : RecyclerView.Adapter<TestDetectionAdapter.ViewHolder>() {

    private val selectedAnswers = mutableMapOf<Int, Int>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.TvQuestion)
        val rgAnswers: RadioGroup = itemView.findViewById(R.id.rgAnswers)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detection_test, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val test = testList[position]
        holder.tvQuestion.text = test.question

        holder.rgAnswers.removeAllViews()
        test.answers.forEach { (key, answer) ->
            val radioButton = RadioButton(holder.itemView.context).apply {
                text = answer.text
                id = View.generateViewId()
                isChecked = selectedAnswers[position] == answer.points
                setOnClickListener {
                    selectedAnswers[position] = answer.points
                    onAnswerSelected(position, answer.points)
                }
            }
            holder.rgAnswers.addView(radioButton)
        }
    }

    override fun getItemCount(): Int = testList.size
}
