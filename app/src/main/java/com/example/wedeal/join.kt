package com.example.wedeal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class join : AppCompatActivity() {
    //파이어베이스 객체
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        //파이어베이스 초기화
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //입력받는
        val emailInput = findViewById<EditText>(R.id.Emailtext)
        val passwordInput = findViewById<EditText>(R.id.Passwordtext)
        val nicknameInput = findViewById<EditText>(R.id.nametext)  // 닉네임 입력
        val regionInput = findViewById<EditText>(R.id.editTextTextPostalAddress)  // 지역 입력
        val registerButton = findViewById<Button>(R.id.joinbutton)

        //회원가입 버튼 누르면
        registerButton.setOnClickListener {
            //4가지 정보 가져오기
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            var nickname = nicknameInput.text.toString()
            var region = regionInput.text.toString()

            //닉네임 비어있다면 기본값 지정
            if (nickname.isEmpty()) {
                nickname = "User"
            }

            //이메일/비밀번호 이용해 사용자 등록
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    //사용자 등록 성공시
                    if (task.isSuccessful) {
                        //파이어베이스에 사용자 정보 저장
                        val user = auth.currentUser
                        val userMap = hashMapOf(
                            "nickname" to nickname,
                            "region" to region,
                            "email" to email
                        )
                        //users 컬렉션에 사용자 정보 저장
                        firestore.collection("user").document(user?.uid ?: "")
                            .set(userMap)
                            .addOnSuccessListener {
                                //데이터 저장 후 홈화면으로 이동
                                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@join, home::class.java)
                                intent.putExtra("nickname", nickname)
                                intent.putExtra("showAlert", false)
                                startActivity(intent)
                                finish()
                            }
                            //데이터 저장 실패시
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    //사용자 등록 실패시
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error"
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        Log.e("RegistrationError", errorMessage)
                    }
                }
        }
    }
}
