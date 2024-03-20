package com.example.datatemanreva

class data_teman_reva {
    var nama: String? = null
    var alamat: String? = null
    var no_hp: String? = null
    var key: String? = null

    // membuat konstruktur kosong untuk membaca data snapshot
    constructor() {}

    // Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User
    constructor(nama: String?, alamat: String?, no_hp: String?) {
        this.nama = nama
        this.alamat = alamat
        this.no_hp = no_hp
    }
}