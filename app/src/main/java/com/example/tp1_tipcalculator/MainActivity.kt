package com.example.tp1_tipcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.text.NumberFormat

class MainActivity : AppCompatActivity(), View.OnFocusChangeListener,
    SeekBar.OnSeekBarChangeListener {

    private lateinit var totalPorPessoa: EditText
    private lateinit var txtSubtotal: EditText
    private lateinit var serviceTaxSeekBar: SeekBar
    private lateinit var valorTotal: valorTotal
    val formatter = NumberFormat.getCurrencyInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalPorPessoa = this.findViewById<EditText>(R.id.inputQtdPessoas)
        totalPorPessoa.setOnFocusChangeListener(this)

        txtSubtotal = this.findViewById<EditText>(R.id.inputSubTotal)
        txtSubtotal.setOnFocusChangeListener(this)

        serviceTaxSeekBar = this.findViewById<SeekBar>(R.id.serviceTaxSeekBar)
        serviceTaxSeekBar.progress = 10
        serviceTaxSeekBar.setOnSeekBarChangeListener(this)
    }

    private fun updateBill() {
        val data: List<String> = getData()

        if (validateData(data[0], data[1])) {
            valorTotal = valorTotal(data[0].toInt(), data[1].toDouble())
            val bill = calculateBill(valorTotal)
            writevalorTotalTotalBill(bill.total)
            writeBillForEach(bill.totalForEach)
        } else {
            printNoDataMessage()
        }
    }

    private fun getData(): List<String> {
        val customers = totalPorPessoa.text.toString()
        val subtotal = txtSubtotal.text.toString()

        return listOf<String>(customers, subtotal)
    }

    private fun printNoDataMessage() {
        Toast.makeText(this, "Dados inválidos", Toast.LENGTH_SHORT).show()
    }

    private fun validateData(customers: String, subtotal: String): Boolean {
        return (customers.isNotEmpty() && subtotal.isNotEmpty()) && (customers.toInt() > 0 && subtotal.toDouble() > 0.00)
    }

    private fun calculateBill(valorTotal: valorTotal): valorTotal {
        val subtotal = valorTotal.subtotal
        valorTotal.total = subtotal + (subtotal * (serviceTaxSeekBar.progress.toDouble() / 100))
        valorTotal.totalForEach = valorTotal.total / valorTotal.customers
        return valorTotal
    }

    private fun writevalorTotalTotalBill(total: Double) {
        val valorTotalTotal = this.findViewById<TextView>(R.id.valorTotal)
        valorTotalTotal.text = formatter.format(total)
    }

    private fun writeBillForEach(total: Double) {
        val totalPerPerson = this.findViewById<TextView>(R.id.totalPorPessoa)
        totalPerPerson.text = formatter.format(total)
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        // this.updateBill()
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        val servicePercent = this.findViewById<TextView>(R.id.lblServicePercent)
        servicePercent.text = serviceTaxSeekBar.progress.toString() + " %"
        this.updateBill()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
    }
}

class valorTotal(var customers: Int, var subtotal: Double) {
    var total: Double = 0.0
    var totalForEach: Double = 0.0 }