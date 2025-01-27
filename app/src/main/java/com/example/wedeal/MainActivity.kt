package com.example.wedeal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //로그인 버튼
        val button = findViewById<Button>(R.id.login)
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, login::class.java)
            startActivity(intent)
        }

        //회원가입 버튼
        val button2 = findViewById<Button>(R.id.join)
        button2.setOnClickListener {
            val intent = Intent(this@MainActivity, join::class.java)
            startActivity(intent)
        }

    }
}
