package com.example.quanlysinhvien

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter (
    val students: List<StudentModel>,
    val menuListener: StudentMenuListener? = null
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentViewHolder {
        val studentView = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        return StudentViewHolder(studentView, menuListener)
    }

    override fun onBindViewHolder(
        holder: StudentViewHolder,
        position: Int
    ) {
        val student = students[position]
        holder.textHoten.text = student.hoten
        holder.textMssv.text = student.mssv
    }

    override fun getItemCount() = students.size
    class StudentViewHolder(
        studentView: View,
        val menuListener: StudentMenuListener?
    ) : RecyclerView.ViewHolder(studentView) {
        val textHoten: TextView = studentView.findViewById<TextView>(R.id.text_hoten)
        val textMssv: TextView = studentView.findViewById<TextView>(R.id.text_mssv)
        val imageMore: ImageView = studentView.findViewById<ImageView>(R.id.image_more)
        init {
            imageMore.setOnClickListener { view ->
                val popup = PopupMenu(studentView.context, view)
                popup.menuInflater.inflate(R.menu.context_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    menuListener?.onMenuItemClick(adapterPosition, menuItem.itemId)
                    true
                }
                popup.show()
            }
        }
    }
}