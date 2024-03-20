package com.example.datatemanreva

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.datatemanreva.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showData.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.save -> {
                // mendapatkan userID dari pengguna yang Terautentikasi
                val getUserId = auth!!.currentUser!!.uid

                // mendapatkan Instance dari Database
                val database = FirebaseDatabase.getInstance()

                // menyimpan data yang diinputkan user kedalam variable
                val getNama: String = binding.nama.getText().toString()
                val getAlamat: String = binding.alamat.getText().toString()
                val getNoHP: String = binding.noHp.getText().toString()

                // mendapatkan referensi dari database
                val getReference: DatabaseReference
                getReference = database.reference

                // mengecek apakah ada data yang kosong
                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNoHP)) {
                    Toast.makeText(this@MainActivity, "Data tidak boleh kosong",
                        Toast.LENGTH_SHORT).show()
                }else {
                    getReference.child("Admin").child(getUserId).child("Data Teman Reva").push()
                        .setValue(data_teman_reva(getNama,getAlamat,getNoHP))
                        .addOnCompleteListener(this) {
                            binding.nama.setText("")
                            binding.alamat.setText("")
                            binding.noHp.setText("")
                            Toast.makeText(this@MainActivity, "Data teman tersimpan",
                                Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.logout -> {
                AuthUI.getInstance().signOut(this@MainActivity).addOnCompleteListener(object :
                    OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {
                        Toast.makeText(this@MainActivity, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                        intent = Intent(this@MainActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Menambahkan flag untuk membersihkan tumpukan aktivitas
                        startActivity(intent)
                        finish()
                    }
                })
            }
            R.id.show_data -> {}
        }
    }

}