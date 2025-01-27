package com.example.wedeal

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import androidx.appcompat.app.AlertDialog

class login : AppCompatActivity() {
    //파이어베이스 인증 객체
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //파이어베이스 객체 초기화
        auth = FirebaseAuth.getInstance()

        //입력 객체
        val emailInput = findViewById<EditText>(R.id.Emailtext)
        val passwordInput = findViewById<EditText>(R.id.Passwordtext)
        val loginButton = findViewById<Button>(R.id.loginbutton)

        //로그인 버튼 클릭시
        loginButton.setOnClickListener {
            //이메일과 비밀번호 값 받아오기
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            //파이어베이스 인증 이용해 로그인
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    //로그인 성공
                    if (task.isSuccessful) {
                        //SharedPreferences로 이메일 저장
                        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("email", email)  // email은 로그인 성공 시 받은 값
                        editor.apply()

                        //홈화면으로 이동
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@login, home::class.java)
                        intent.putExtra("showAlert", false)
                        intent.putExtra("email", email)
                        startActivity(intent)
                        finish()
                    } else { //로그인 실패
                        val errorMessage = task.exception?.message ?: "Unknown error"
                        AlertDialog.Builder(this)
                            .setTitle("로그인 실패")
                            .setMessage("로그인 실패 : $errorMessage")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
        }
    }
}
