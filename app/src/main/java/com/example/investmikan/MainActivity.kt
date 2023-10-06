package com.example.investmikan

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editInitialInvestment = findViewById<EditText>(R.id.editInitialInvestment)
        val editMonthlyContribution = findViewById<EditText>(R.id.editMonthlyContribution)
        val editYears = findViewById<EditText>(R.id.editYears)
        val editInterestRate = findViewById<EditText>(R.id.editInterestRate)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvTotalContribution = findViewById<TextView>(R.id.tvTotalContribution)
        val tvFutureValue = findViewById<TextView>(R.id.tvFutureValue)
        val tvTotalInterest = findViewById<TextView>(R.id.tvTotalInterest)
        val tvMonthlyContribution = findViewById<TextView>(R.id.tvMonthlyContribution)
        val tvMonthlyValues = findViewById<TextView>(R.id.tvMonthlyValues)
        val tvTempTotalInterest = findViewById<TextView>(R.id.tvTempTotalInterest)

        btnCalculate.setOnClickListener(View.OnClickListener {
            val initialInvestment = editInitialInvestment.text.toString().toDouble()
            val monthlyContribution = editMonthlyContribution.text.toString().toDouble()
            val years = editYears.text.toString().toInt()
            val interestRate = editInterestRate.text.toString().toDouble() / 100

            val totalContribution = (initialInvestment + (monthlyContribution * 12 * years)).toInt()
            val futureValue = calculateCompoundInterest(initialInvestment, monthlyContribution, interestRate, years)
            val totalInterest = futureValue - totalContribution

            val monthlyValues = calculateCompoundInterestPerMonth(initialInvestment, monthlyContribution, interestRate, years)
            val formattedTempTotalContribution = StringBuilder("Total Contribution:\n")
            val formattedMonthlyValues = StringBuilder(" Values:\n")
            val formattedTempTotalInterest = StringBuilder(" Total Interest:\n")

            val formattedTotalContribution = formatNumberWithCommas(totalContribution)
            val formattedFutureValue = formatNumberWithCommas(futureValue)
            val formattedTotalInterest = formatNumberWithCommas(totalInterest)

            for ((index, value) in monthlyValues.withIndex()) {
                val month = (index + 1) % 12
                val year = (index + 1) / 12
                val tempTotalContribution = (initialInvestment + monthlyContribution * month).toInt()
                val tempTotalInterest = value - tempTotalContribution
                formattedTempTotalContribution.append("$year year $month month: ¥ ${formatNumberWithCommas(tempTotalContribution.toInt())}\n")
                formattedMonthlyValues.append("¥ ${formatNumberWithCommas(value.toInt())}\n")
                formattedTempTotalInterest.append("¥ ${formatNumberWithCommas(tempTotalInterest.toInt())}\n")
            }

            tvTotalContribution.text = "Total Contribution: ¥ $formattedTotalContribution"
            tvFutureValue.text = "Future Value: ¥ $formattedFutureValue"
            tvTotalInterest.text = "Total Interest: ¥ $formattedTotalInterest"
            tvMonthlyContribution.text = formattedTempTotalContribution.toString()
            tvMonthlyValues.text = formattedMonthlyValues.toString()
            tvTempTotalInterest.text = formattedTempTotalInterest.toString()
        })
    }

    private fun calculateCompoundInterest(initialInvestment: Double, monthlyContribution: Double, interestRate: Double, years: Int): Int {
        var futureValue = initialInvestment
        val monthlyInterestRate = interestRate / 12

        for (i in 1..years * 12) {
            futureValue += monthlyContribution
            futureValue *= (1 + monthlyInterestRate)
        }

        return futureValue.toInt()
    }

    private fun calculateCompoundInterestPerMonth(initialInvestment: Double, monthlyContribution: Double, interestRate: Double, years: Int): List<Int> {
        val monthlyValues = mutableListOf<Int>()
        var futureValue = initialInvestment
        val monthlyInterestRate = interestRate / 12

        for (i in 1..years * 12) {
            futureValue += monthlyContribution
            futureValue *= (1 + monthlyInterestRate)
            monthlyValues.add(futureValue.toInt())
        }

        return monthlyValues
    }

    private fun formatNumberWithCommas(number: Int): String {
        return NumberFormat.getInstance().format(number)
    }

}
