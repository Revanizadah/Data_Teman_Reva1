package com.example.datatemanreva

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RecyclerViewAdaptor (private val dataTemanReva: ArrayList<data_teman_reva>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdaptor.ViewHolder>() {
        private val context: Context

        val database = FirebaseDatabase.getInstance()
        private var auth: FirebaseAuth? = null

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val Nama: TextView
            val Alamat: TextView
            val NoHP: TextView
            val ListItem: LinearLayout


            init {
                Nama = itemView.findViewById(R.id.namax)
                Alamat = itemView.findViewById(R.id.alamatx)
                NoHP = itemView.findViewById(R.id.no_hpx)
                ListItem = itemView.findViewById(R.id.list_item)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V: View = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.view_design, parent, false)
        return ViewHolder(V)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val Nama: String? = dataTemanReva.get(position).nama
        val Alamat: String? = dataTemanReva.get(position).alamat
        val No_HP: String? = dataTemanReva.get(position).no_hp

        holder.Nama.text = "Nama: $Nama"
        holder.Alamat.text = "Alamat: $Alamat"
        holder.NoHP.text = "No_HP: $No_HP"
        holder.ListItem.setOnLongClickListener(object: View.OnLongClickListener {
                override fun onLongClick(V: View?): Boolean {
                    holder.ListItem.setOnLongClickListener { view ->
                        val action = arrayOf("Update", "Delete")
                        val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
                        alert.setItems(action, DialogInterface.OnClickListener { dialog, i ->
                            when (i) {
                                0 -> {
                                    val bundle = Bundle ()
                                    bundle.putString("dataNama", dataTemanReva[position].nama)
                                    bundle.putString("dataAlamat", dataTemanReva[position].alamat)
                                    bundle.putString("dataNoHP", dataTemanReva[position].no_hp)
                                    bundle.putString("getPrimaryKey", dataTemanReva[position].key)
                                    val intent = Intent(view.context, UpdateData::class.java)
                                    intent.putExtras(bundle)
                                    context.startActivity(intent)
                                }
                                1 -> {
                                    listener?.onDeleteData(dataTemanReva.get(position),position)
                                }
                            }
                        })
                        alert.create()
                        alert.show()
                        true
                    }
                    return true
                }
            })
    }

    override fun getItemCount(): Int {
        return dataTemanReva.size
    }

    init {
        this.context = context
        (context as MyListData).also { this.listener = it }
    }
    interface dataListener {
        fun onDeleteData(data: data_teman_reva?, position: Int)
    }

    var listener: dataListener? = null

}