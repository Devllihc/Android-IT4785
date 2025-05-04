package com.example.quanlysinhvien

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UpdateStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_student)
        val editHoten = findViewById<EditText>(R.id.edit_hoten)
        val editMssv = findViewById<EditText>(R.id.edit_mssv)
        val editEmail = findViewById<EditText>(R.id.edit_email)
        val editSdt = findViewById<EditText>(R.id.edit_sdt)
        val buttonAdd = findViewById<Button>(R.id.button_update)

        val hoten = intent.getStringExtra("hoten")
        val mssv = intent.getStringExtra("mssv")
        val email = intent.getStringExtra("email")
        val sdt = intent.getStringExtra("sdt")
        val position = intent.getIntExtra("position", -1)

        editHoten.setText(hoten)
        editMssv.setText(mssv)
        editEmail.setText(email)
        editSdt.setText(sdt)

        buttonAdd.setOnClickListener {
            intent.putExtra("hoten", editHoten.text.toString())
            intent.putExtra("mssv", editMssv.text.toString())
            intent.putExtra("email", editEmail.text.toString())
            intent.putExtra("sdt", editSdt.text.toString())
            intent.putExtra("position", position)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}