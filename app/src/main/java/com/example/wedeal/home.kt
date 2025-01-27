package com.example.wedeal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class home: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        //SharedPreferences에서 email 가져옴
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val nickname = intent.getStringExtra("nickname")

        //초기 사용자 이름 지정
        val name: String
        if (email == "jiyoung@gmail.com") {
            name = "박지영"
        } else if (email == "jiyoon@gmail.com") {
            name = "박지윤"
        } else {
            name = nickname ?: "User"
        }

        //인삿말 형식
        val greetingText = findViewById<TextView>(R.id.name)
        greetingText.text = "$name 님\n오늘도 환영합니다"

        //약속 등록 확인 알림
        val Appointment_confirmation = intent.getBooleanExtra("showAlert", false)
        if (Appointment_confirmation) {
            // 알림창 표시
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("알림창")
                .setMessage("약속이 성공적으로 등록되었습니다.")
                .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                .create()
            alertDialog.show()
        }

        //상품 등록 확인 알림
        val Confirm_product_registration = intent.getBooleanExtra("Confirm_product_registration", false)
        if (Confirm_product_registration) {
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("알림창")
                .setMessage("상품 등록이 성공적으로 되었습니다.")
                .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                .create()
            alertDialog.show()
        }

        // 검색 버튼
        val button1 = findViewById<Button>(R.id.searchbutton)
        button1.setOnClickListener {
            val intent = Intent(this@home, search::class.java)
            startActivity(intent)
        }

        // 마이페이지 버튼
        val button2 = findViewById<Button>(R.id.homebutton)
        button2.setOnClickListener {
            val intent = Intent(this@home, home::class.java)
            intent.putExtra("showAlert", false)
            startActivity(intent)
        }

        // 상품등록 버튼
        val button3 = findViewById<Button>(R.id.registrationbutton)
        button3.setOnClickListener {
            val intent = Intent(this@home, registration::class.java)
            startActivity(intent)
        }

        // 로그아웃 버튼
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("email") //임시 저장 이메일 삭제
            editor.apply()

            //하단메세지 표시
            Toast.makeText(this, "안녕히가세요", Toast.LENGTH_SHORT).show()

            //첫화면으로 이동
            val intent = Intent(this@home, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
