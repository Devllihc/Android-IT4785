package com.example.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    private lateinit var sourceAmount: EditText
    private lateinit var destinationAmount: EditText
    private lateinit var sourceCurrency: Spinner
    private lateinit var destinationCurrency: Spinner

    private val currencyRates = mapOf(
        "USD" to 1.0, "VND" to 25355.0, "SGD" to 1.32, "EUR" to 0.92, "JPY" to 153.26
    )

    private val currencyMapping = mapOf(
        "USD" to "United States - USD",
        "VND" to "Vietnam - VND",
        "SGD" to "Singapore - SGD",
        "EUR" to "Europe - EUR",
        "JPY" to "Japan - JPY"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sourceAmount = findViewById(R.id.sourceAmount)
        destinationAmount = findViewById(R.id.destinationAmount)
        sourceCurrency = findViewById(R.id.sourceCurrency)
        destinationCurrency = findViewById(R.id.destinationCurrency)

        val currencies = resources.getStringArray(R.array.currency_array)
        val displayCurrencies = currencies.map { currencyMapping[it] ?: it }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, displayCurrencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        sourceCurrency.adapter = adapter
        destinationCurrency.adapter = adapter

        sourceCurrency.setSelection(displayCurrencies.indexOf("Vietnam - VND"))
        destinationCurrency.setSelection(displayCurrencies.indexOf("United States - USD"))

        sourceAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convertCurrency()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                convertCurrency()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        sourceCurrency.onItemSelectedListener = listener
        destinationCurrency.onItemSelectedListener = listener
    }

    private fun convertCurrency() {
        val selectedSource = sourceCurrency.selectedItem.toString()
        val selectedDestination = destinationCurrency.selectedItem.toString()

        val sourceCurrencyCode = currencyMapping.entries.find { it.value == selectedSource }?.key ?: "USD"
        val destinationCurrencyCode = currencyMapping.entries.find { it.value == selectedDestination }?.key ?: "USD"

        val sourceText = sourceAmount.text.toString()
        val sourceValue = if (sourceText.isEmpty()) 0.0 else sourceText.toDoubleOrNull() ?: 0.0

        val sourceRate = currencyRates[sourceCurrencyCode] ?: 1.0
        val destinationRate = currencyRates[destinationCurrencyCode] ?: 1.0

        val rawConvertedValue = (sourceValue / sourceRate) * destinationRate

        var formattedValue = BigDecimal(rawConvertedValue)
            .setScale(10, RoundingMode.HALF_UP)
            .stripTrailingZeros()

        if (formattedValue.precision() > 16) {
            formattedValue = formattedValue.round(MathContext(16, RoundingMode.HALF_UP))
        }

        destinationAmount.setText(formattedValue.toPlainString())
    }
}
