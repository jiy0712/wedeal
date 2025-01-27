package com.example.wedeal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

//검색결과
class searchresult : AppCompatActivity() {

    //리사이클뷰/상품리스트
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchresult)

        //리사이클뷰 초기화
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        //리사이클뷰 상품 클릭
        adapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                //클릭한 상품 데이터를 상품 상세 페이지로 이동
                val intent = Intent(this@searchresult, detail::class.java)
                intent.putExtra("productName", product.name)
                intent.putExtra("productPrice", product.price)
                startActivity(intent)
            }
        })

        //파이어베이스 데이터 불러오기
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .get()
            .addOnSuccessListener { documents ->
                productList.clear() //리스트 초기화
                for (document in documents) {
                    val name = document.getString("name") ?: "Unknown"
                    val price = document.getString("price") ?: "0"
                    productList.add(Product(name, price))
                }
                //리사이클뷰 업데이트
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
            }

        //검색어 받기
        val searchQuery = intent.getStringExtra("searchQuery")

        //검색결과 텍스트뷰로 보이게
        val searchResultTextView = findViewById<TextView>(R.id.searchResultTextView)

        //검색어 텍스트뷰로 보이게
        if (!searchQuery.isNullOrEmpty()) {
            searchResultTextView.text = "$searchQuery"
        } else {
            searchResultTextView.text = ""
        }

        //검색
        val button1 = findViewById<Button>(R.id.searchbutton)
        button1.setOnClickListener {
            val intent = Intent(this@searchresult, search::class.java)
            startActivity(intent)
        }

        //마이페이지
        val button2 = findViewById<Button>(R.id.homebutton)
        button2.setOnClickListener {
            val intent = Intent(this@searchresult, home::class.java)
            intent.putExtra("showAlert", false)
            startActivity(intent)
        }

        //상품등록
        val button3 = findViewById<Button>(R.id.registrationbutton)
        button3.setOnClickListener {
            val intent = Intent(this@searchresult, registration::class.java)
            startActivity(intent)
        }
    }
}