package com.example.wedeal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)

        //상품 이름 가격 데이터 받기
        val productName = intent.getStringExtra("productName")
        val productPrice = intent.getStringExtra("productPrice")

        //텍스트로 데이터 표시
        val nameTextView: TextView = findViewById(R.id.productNameTextView)
        val priceTextView: TextView = findViewById(R.id.productPriceTextView)

        //상품 이름 설정
        nameTextView.text = productName

        //상품 가격 뒤 '원' 붙이기
        if (productPrice != null) {
            priceTextView.text = "$productPrice 원"
        }

        nameTextView.text = productName
        priceTextView.text = productPrice

        //파이어베이스에서 상품 설명 불러오기
        if (productName != null && productPrice != null) {
            fetchProductDescription(productName, productPrice)
        } else {
            findViewById<TextView>(R.id.descriptionTextView).text = "상품 설명이 없습니다."
        }

        // 검색 버튼
        val button1 = findViewById<Button>(R.id.searchbutton)
        button1.setOnClickListener {
            val intent = Intent(this@detail, search::class.java)
            startActivity(intent)
        }

        // 마이페이지 버튼
        val button2 = findViewById<Button>(R.id.homebutton)
        button2.setOnClickListener {
            val intent = Intent(this@detail, home::class.java)
            intent.putExtra("showAlert", false)
            startActivity(intent)
        }

        // 상품등록 버튼
        val button3 = findViewById<Button>(R.id.registrationbutton)
        button3.setOnClickListener {
            val intent = Intent(this@detail, registration::class.java)
            startActivity(intent)
        }

        // 약속잡기 버튼 (해당 상품 숨기기)
        val button4 = findViewById<Button>(R.id.promisebutton)
        button4.setOnClickListener {
            if (productName != null) {
                deleteProductByName(productName)
            }
            val intent = Intent(this@detail, home::class.java)
            intent.putExtra("showAlert", true)
            startActivity(intent)
        }
    }

    // Firebase에서 상품 설명 가져오는 함수
    private fun fetchProductDescription(productName: String, productPrice: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products") //상품 컬렉션 참조
            //상품 이름과 가격으로 검색
            .whereEqualTo("name", productName)
            .whereEqualTo("price", productPrice)
            //데이터 가져오기
            .get()
            //데이터를 잘 가져왔다면
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val description = document.getString("description") ?: "상품 설명이 없습니다."
                        findViewById<TextView>(R.id.descriptionTextView).text = description
                        break
                    }
                } else {
                    findViewById<TextView>(R.id.descriptionTextView).text = "상품 설명이 없습니다."
                }
            }
            .addOnFailureListener { e ->
                findViewById<TextView>(R.id.descriptionTextView).text = "설명을 가져오는 데 실패했습니다: ${e.message}"
            }
    }

    //약속시 상품숨기는 함수
    private fun deleteProductByName(productName: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .whereEqualTo("name", productName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    db.collection("products").document(document.id)
                        .delete() //삭제
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "숨김 실패 : ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "문서 찾기 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
