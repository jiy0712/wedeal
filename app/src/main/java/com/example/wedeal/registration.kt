package com.example.wedeal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

//상품 등록
class registration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        setupNavigationButtons()
        setupProductRegistration()
    }

    //네비게이션 버튼
    private fun setupNavigationButtons() {
        //홈
        findViewById<Button>(R.id.homebutton).setOnClickListener {
            navigateToHome(showAlert = false)
        }

        //검색
        findViewById<Button>(R.id.searchbutton).setOnClickListener {
            val intent = Intent(this, search::class.java)
            startActivity(intent)
        }

        //상품등록
        findViewById<Button>(R.id.registrationbutton).setOnClickListener {
            val intent = Intent(this, registration::class.java)
            startActivity(intent)
        }
    }

    //상품 등록 버튼
    private fun setupProductRegistration() {
        val nameInput = findViewById<EditText>(R.id.priceInput)
        val priceInput = findViewById<EditText>(R.id.nameInput)
        val descriptionInput = findViewById<EditText>(R.id.descriptionInput)

        findViewById<Button>(R.id.promisebutton).setOnClickListener {
            val name = nameInput.text.toString()
            val price = priceInput.text.toString()
            val description = descriptionInput.text.toString()

            //모든 칸이 채워졌는지
            if (name.isNotBlank() && price.isNotBlank() && description.isNotBlank()) {
                addProduct(name, price, description) //상품 등록 함수 호출
            } else {
                Toast.makeText(this, "모든 칸을 채워주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //상품 등록 함수
    private fun addProduct(name: String, price: String, description: String) {
        val product = hashMapOf(
            "name" to name,
            "price" to price,
            "description" to description,
            "isHidden" to false
        )

        FirestoreUtil.db.collection("products")
            .add(product)
            .addOnSuccessListener {
                Toast.makeText(this, "상품이 등록되었습니다!", Toast.LENGTH_SHORT).show()
                navigateToDetail(description)
                navigateToHome(showAlert = false, confirmProductRegistration = true)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "등록 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    //상품 설명을 상품 상세 페이지로 연결
    private fun navigateToDetail(description: String, isHidden: Boolean = false) {
        val intent = Intent(this, detail::class.java).apply {
            putExtra("product_description", description)
            putExtra("isHidden", isHidden)
        }
        startActivity(intent)
    }

    //상품 등록 후 홈화면으로 이동하며 데이터 보내기
    private fun navigateToHome(showAlert: Boolean, confirmProductRegistration: Boolean = false) {
        val intent = Intent(this, home::class.java).apply {
            putExtra("showAlert", showAlert)
            putExtra("Confirm_product_registration", confirmProductRegistration)
        }
        startActivity(intent)
        finish()
    }
}
