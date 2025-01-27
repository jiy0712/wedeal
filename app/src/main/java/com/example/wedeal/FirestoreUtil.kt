package com.example.wedeal

import com.google.firebase.firestore.FirebaseFirestore

//파이어베이스 데이터베이스 관리하는 (관리하기 쉽게 싱글통 패턴 클래스)
//인터페이스 재사용하기위해
object FirestoreUtil {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance() //파이어베이스 반환
    }
}
