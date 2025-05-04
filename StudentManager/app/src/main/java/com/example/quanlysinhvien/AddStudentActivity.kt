package com.example.quanlysinhvien

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_student)

        val editHoten = findViewById<EditText>(R.id.edit_hoten)
        val editMssv = findViewById<EditText>(R.id.edit_mssv)
        val editEmail = findViewById<EditText>(R.id.edit_email)
        val editSdt = findViewById<EditText>(R.id.edit_sdt)
        val buttonAdd = findViewById<Button>(R.id.button_add)

        buttonAdd.setOnClickListener {
            intent.putExtra("hoten", editHoten.text.toString())
            intent.putExtra("mssv", editMssv.text.toString())
            intent.putExtra("email", editEmail.text.toString())
            intent.putExtra("sdt", editSdt.text.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}