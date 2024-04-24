package com.example.datatemanreva

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.datatemanreva.databinding.ActivityUpdateDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateData : AppCompatActivity() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat: String? = null
    private var cekNoHp: String? = null
    private lateinit var binding: ActivityUpdateDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Update Data"

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data
        binding.update.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                cekNama = binding.newNama.getText().toString()
                cekAlamat = binding.newAlamat.getText().toString()
                cekNoHp = binding.newNohp.getText().toString()

                if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekNoHp!!)) {
                    Toast.makeText(this@UpdateData, "Data tidak boleh Kosong",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val setTeman = data_teman_reva()
                    setTeman.nama = binding.newNama.getText().toString()
                    setTeman.alamat = binding.newAlamat.getText().toString()
                    setTeman.no_hp = binding.newNohp.getText().toString()
                    updateTeman(setTeman)
                }
            }
        })
    }
    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private val data: Unit
        private get() {
            val getNama = intent.extras!!.getString("dataNama")
            val getAlamat = intent.extras!!.getString("dataAlamat")
            val getNohp = intent.extras!!.getString("dataNoHP")
            binding.newNama!!.setText(getNama)
            binding.newAlamat!!.setText(getAlamat)
            binding.newNohp!!.setText(getNohp)
        }
    private fun updateTeman(teman: data_teman_reva) {
        val userId = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        database!!.child("Admin")
            .child(userId!!)
            .child("Data Teman Reva")
            .child(getKey!!)
            .setValue(teman)
            .addOnSuccessListener {
                binding.newNama!!.setText("")
                binding.newAlamat!!.setText("")
                binding.newNohp!!.setText("")
                Toast.makeText(this@UpdateData, "Data berhasil diubah",
                    Toast.LENGTH_SHORT).show()

                data

                finish()
            }
    }
}