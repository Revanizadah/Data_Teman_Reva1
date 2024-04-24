package com.example.datatemanreva

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.datatemanreva.databinding.ActivityMyListDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyListData : AppCompatActivity(), RecyclerViewAdaptor.dataListener {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var dataTemanReva = ArrayList<data_teman_reva>()
    private var auth: FirebaseAuth? = null

    private lateinit var binding: ActivityMyListDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.dataList)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar!!.title = "Data Teman Reva"
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }
    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show()
        val getUserId: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserId).child("Data Teman Reva")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dataTemanReva.clear()
                        for (snapshot in dataSnapshot.children) {
                            val teman = snapshot.getValue(data_teman_reva::class.java)
                            teman?.key = snapshot.key
                            dataTemanReva.add(teman!!)
                        }

                        adapter = RecyclerViewAdaptor(dataTemanReva, this@MyListData)
                        recyclerView?.adapter = adapter
                        (adapter as RecyclerViewAdaptor).notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Data Berhasil Dimuat", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Data Teman Gagal Dimuat", Toast.LENGTH_LONG).show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }
    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: data_teman_reva?, position: Int) {
        val getUserId: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        if (getReference != null) {
            getReference.child("Admin")
                .child(getUserId)
                .child("Data Teman Reva")
                .child(data?.key.toString())
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@MyListData,"Data Berhasil Dihapus",
                    Toast.LENGTH_SHORT).show();
                }
        }
    }

}