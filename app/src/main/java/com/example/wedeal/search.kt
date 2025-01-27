package com.example.wedeal

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//검색
class search : AppCompatActivity() {

    //추천 키워드 리스트
    private val recommendation_keyword = listOf(
        "가전제품", "냉장고", "세탁기", "에어컨", "가구", "소파", "침대", "책상",
        "의류", "남성복", "여성복", "아동복", "스포츠 용품", "자전거", "헬스기구",
        "전자기기", "스마트폰", "태블릿", "노트북", "패션 아이템", "신발", "가방",
        "액세서리", "뷰티 제품", "화장품", "스킨케어", "생활용품", "주방용품", "청소용품",
        "게임기", "피규어", "취미 용품", "중고차", "자전거 용품", "캠핑 용품", "악세서리",
        "디지털 기기", "카메라", "모니터", "키보드", "마우스", "헤드폰", "스피커", "가전",
        "인테리어 소품", "조명", "식물", "책", "음악", "영화", "게임", "레고", "퍼즐",
        "DIY 용품", "아트 용품", "스포츠 의류", "운동화", "등산 용품", "수영 용품",
        "자전거 헬멧", "스케이트보드", "스노우보드", "캠핑 텐트", "낚시 용품", "바베큐 그릴",
        "주방 가전", "커피 머신", "믹서기", "에어프라이어", "전자레인지", "청소기", "다리미",
        "온수기", "가습기", "제습기", "냉동고", "식기세척기", "세탁기 건조기", "침대 매트리스",
        "소파 베드", "책장", "옷장", "서랍장", "화장대", "거울", "벽걸이 TV", "스탠드", "탁자",
        "의자", "바닥재", "커튼", "침구", "수납장", "가전 리모컨", "전기포트", "식탁", "의자 세트",
        "가전 케이블", "전기장판", "온열기"
    )

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        //파이어베이스 초기화
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //입력과 버튼 연결
        val searchText = findViewById<EditText>(R.id.searchtext)
        val searchButton = findViewById<ImageButton>(R.id.searchiconbutton)

        //SharedPreferences에 저장된 텍스트 불러오기 (검색결과에서 다시 검색으로 되돌아갈때)
        val sharedPreferences = getSharedPreferences("search_data", MODE_PRIVATE)
        val savedSearchText = sharedPreferences.getString("searchText", "")
        searchText.setText(savedSearchText)

        //검색 버튼 클릭했다면
        searchButton.setOnClickListener {
            val searchQuery = searchText.text.toString()
            //검색어저장
            val editor = sharedPreferences.edit()
            editor.putString("searchText", searchQuery)
            editor.apply()

            //검색결과 화면으로 이동
            val intent = Intent(this, searchresult::class.java)
            intent.putExtra("searchQuery", searchQuery) // 전달
            startActivity(intent)
        }

        //추천키워드 랜덤 5개 출력
        val keywordText: TextView = findViewById(R.id.keywordTextView)
        val refreshButton: ImageButton = findViewById(R.id.refreshbutton)
        keywordText.text = randomkeyword().joinToString(",")
        refreshButton.setOnClickListener {
            keywordText.text = randomkeyword().joinToString(", ")
        }

        //검색결과
        val button1 = findViewById<Button>(R.id.searchbutton)
        button1.setOnClickListener {
            val intent = Intent(this@search, searchresult::class.java)
            startActivity(intent)
        }

        //마이페이지
        val button2 = findViewById<Button>(R.id.homebutton)
        button2.setOnClickListener {
            val intent = Intent(this@search, home::class.java)
            intent.putExtra("showAlert", false)
            startActivity(intent)
        }

        //상품등록
        val button3 = findViewById<Button>(R.id.registrationbutton)
        button3.setOnClickListener {
            val intent = Intent(this@search, registration::class.java)
            startActivity(intent)
        }
    }
    //랜덤으로 추천하는 키워드 5개 선택
    private fun randomkeyword(): List<String> {
        return recommendation_keyword.shuffled().take(5)
    }

    //화면 일시중지
    override fun onPause() {
        super.onPause()
        //검색값을 저장
        val sharedPreferences = getSharedPreferences("search_data", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val searchText = findViewById<EditText>(R.id.searchtext)
        editor.putString("searchText", searchText.text.toString())
        editor.apply()
    }

    //화면 종료
    override fun onStop() {
        super.onStop()
        val sharedPreferences = getSharedPreferences("search_data", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("searchText") // 검색값 삭제
        editor.apply()
    }
}
