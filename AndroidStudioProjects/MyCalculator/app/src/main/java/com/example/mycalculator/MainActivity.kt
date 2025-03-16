package com.example.mycalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycalculator.ui.theme.MyCalculatorTheme
import java.math.BigDecimal
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCalculatorTheme {
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Kết quả hiển thị
        Text(
            text = expression.ifEmpty { "0" },
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            maxLines = 1
        )

        // Lưới các nút bấm
        val buttons = listOf(
            listOf("C", "⌫", "%", "/"),
            listOf("7", "8", "9", "*"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("+/-", "0", ".", "=")
        )

        buttons.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { button ->
                    CalculatorButton(
                        text = button,
                        onClick = {
                            when (button) {
                                "C" -> {
                                    expression = ""
                                    result = ""
                                }
                                "⌫" -> {
                                    if (expression.isNotEmpty()) expression = expression.dropLast(1)
                                }
                                "=" -> {
                                    result = try {
                                        val finalExpression = expression.replace("x", "*")
                                        expression = evalExpression(finalExpression)
                                        expression
                                    } catch (e: Exception) {
                                        "Error"
                                    }
                                }
                                else -> {
                                    expression += button
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

// Nút bấm của máy tính
@Composable
fun CalculatorButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
    ) {
        Text(text, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

// Hàm tính toán biểu thức toán học
fun evalExpression(expression: String): String {
    return try {
        val tokens = tokenize(expression)
        val postfix = infixToPostfix(tokens)
        val result = evaluatePostfix(postfix)
        result.toPlainString()  // Trả về kết quả dưới dạng chuỗi
    } catch (e: Exception) {
        "Error"
    }
}

// Tách biểu thức thành các token (số, toán tử)
fun tokenize(expression: String): List<String> {
    val tokens = mutableListOf<String>()
    var number = ""

    for (char in expression) {
        when {
            char.isDigit() || char == '.' -> number += char  // Gặp số thì gom lại
            char in "+-*/" -> {
                if (number.isNotEmpty()) {
                    tokens.add(number)
                    number = ""
                }
                tokens.add(char.toString())  // Thêm toán tử
            }
        }
    }
    if (number.isNotEmpty()) tokens.add(number)
    return tokens
}

// Chuyển đổi từ biểu thức trung tố (Infix) sang hậu tố (Postfix)
fun infixToPostfix(tokens: List<String>): List<String> {
    val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)
    val output = mutableListOf<String>()
    val operators = Stack<String>()

    for (token in tokens) {
        when {
            token.toDoubleOrNull() != null -> output.add(token)  // Nếu là số, thêm vào output
            token in precedence.keys -> {
                while (operators.isNotEmpty() && precedence[operators.peek()]!! >= precedence[token]!!) {
                    output.add(operators.pop())  // Đưa toán tử vào output
                }
                operators.push(token)
            }
        }
    }

    while (operators.isNotEmpty()) {
        output.add(operators.pop())  // Đưa toán tử còn lại vào output
    }

    return output
}

// Tính toán giá trị từ biểu thức hậu tố (Postfix)
fun evaluatePostfix(tokens: List<String>): BigDecimal {
    val stack = Stack<BigDecimal>()

    for (token in tokens) {
        when {
            token.toDoubleOrNull() != null -> stack.push(BigDecimal(token))
            token in "+-*/" -> {
                val b = stack.pop()
                val a = stack.pop()
                val result = when (token) {
                    "+" -> a.add(b)
                    "-" -> a.subtract(b)
                    "*" -> a.multiply(b)
                    "/" -> a.divide(b, 2, BigDecimal.ROUND_HALF_UP)
                    else -> throw IllegalArgumentException("Unknown operator")
                }
                stack.push(result)
            }
        }
    }
    return stack.pop()
}
