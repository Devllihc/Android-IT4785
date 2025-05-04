package com.example.quanlysinhvien

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var adapter: StudentAdapter
    lateinit var addStudentLauncher: ActivityResultLauncher<Intent>
    lateinit var updateStudentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Student Manager"

        val students = mutableListOf<StudentModel>()
        repeat(3){
            students.add(StudentModel(
                "Student $it",
                "No.$it",
                "stu$it@example.com",
                String.format("%010d", it)
            ))
        }

        adapter = StudentAdapter(students, object : StudentMenuListener {
            override fun onMenuItemClick(position: Int, menuId: Int) {
                val student = students[position]
                when(menuId) {
                    R.id.action_update -> {
                        val intent = Intent(this@MainActivity, UpdateStudentActivity::class.java)
                        intent.putExtra("hoten", student.hoten)
                        intent.putExtra("mssv", student.mssv)
                        intent.putExtra("email", student.email)
                        intent.putExtra("sdt", student.sdt)
                        intent.putExtra("position", position)
                        updateStudentLauncher.launch(intent)
                    }
                    R.id.action_delete -> {
                        val dialog = AlertDialog.Builder(this@MainActivity)
                            .setIcon(R.drawable.round_warning_24)
                            .setTitle("Delete")
                            .setMessage("Do you want to delete?")
                            .setPositiveButton("Yes") {dialog, which ->
                                students.removeAt(position)
                                adapter.notifyItemRemoved(position)
                            }
                            .setNegativeButton("No") {dialog, which -> dialog.dismiss() }
                            .create()
                        dialog.show()
                    }
                    R.id.action_call -> {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${student.sdt}"))
                        startActivity(intent)
                    }
                    R.id.action_email -> {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:${student.email}" +
                                    "?subject=${Uri.encode("Hello")}" +
                                    "&body=${Uri.encode("Hello")}")
                        }
                        startActivity(intent)
                    }
                }
            }

        })

        val recyclerView =findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK) {
                val hoten = it.data?.getStringExtra("hoten")
                val mssv = it.data?.getStringExtra("mssv")
                val email = it.data?.getStringExtra("email")
                val sdt = it.data?.getStringExtra("sdt")

                if(!hoten.isNullOrEmpty() && !mssv.isNullOrEmpty() && !email.isNullOrEmpty() && !sdt.isNullOrEmpty()){
                    val newStudent = StudentModel(hoten, mssv, email, sdt)
                    students.add(0, newStudent)
                    adapter.notifyItemInserted(0)
                }
            }
        }

        updateStudentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK) {
                val hoten = it.data?.getStringExtra("hoten")
                val mssv = it.data?.getStringExtra("mssv")
                val email = it.data?.getStringExtra("email")
                val sdt = it.data?.getStringExtra("sdt")
                val position = it.data?.getIntExtra("position", -1) ?: -1

                if(!hoten.isNullOrEmpty() && !mssv.isNullOrEmpty() && !email.isNullOrEmpty() && !sdt.isNullOrEmpty() && position >= 0){
                    val updateStudent = StudentModel(hoten, mssv, email, sdt)
                    students[position] = updateStudent
                    adapter.notifyItemChanged(position)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                addStudentLauncher.launch(intent)
            }
        }
        return true
    }
}