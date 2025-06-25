package com.example.ch_19_map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.net.URLEncoder
import android.widget.TableLayout
import android.widget.TableRow
import android.view.Gravity
import java.util.*
import android.view.View

class ParkingDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_parking_detail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val parkingDetail = intent.getParcelableExtra<ParkingDetail>("parking_detail_key")
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)

        parkingDetail?.let { detail ->
            findViewById<TextView>(R.id.detail_name).text = detail.name
            findViewById<TextView>(R.id.detail_address).text = detail.address

            // 자리 정보 바인딩
            findViewById<TextView>(R.id.detail_total_spaces).text = "전체 자리수: ${detail.totalSpaces}"
            findViewById<TextView>(R.id.detail_available_spaces).text = "실시간 남은 자리수: ${detail.availableSpaces}"

            // 요금 정보 바인딩
            findViewById<TextView>(R.id.detail_basic_free_time).text = "기본 무료 시간: ${detail.basicFreeTime}"
            findViewById<TextView>(R.id.detail_basic_charge_time).text = "기본 요금 시간: ${detail.basicChargeTime}"
            findViewById<TextView>(R.id.detail_basic_charge).text = "기본 요금: ${detail.basicCharge}"
            findViewById<TextView>(R.id.detail_additional_unit_time).text = "추가 단위 시간: ${detail.additionalUnitTime}"
            findViewById<TextView>(R.id.detail_additional_unit_charge).text = "추가 단위 요금: ${detail.additionalUnitCharge}"
            findViewById<TextView>(R.id.detail_one_day_charge).text = "1일 요금: ${detail.oneDayCharge}"
            findViewById<TextView>(R.id.detail_monthly_charge).text = "월 정액 요금: ${detail.monthlyCharge}"

            // 운영 시간 정보 바인딩
            findViewById<TextView>(R.id.detail_sunday_oper_time).text = "일요일: ${detail.sundayOperTime}"
            findViewById<TextView>(R.id.detail_monday_oper_time).text = "월요일: ${detail.mondayOperTime}"
            findViewById<TextView>(R.id.detail_tuesday_oper_time).text = "화요일: ${detail.tuesdayOperTime}"
            findViewById<TextView>(R.id.detail_wednesday_oper_time).text = "수요일: ${detail.wednesdayOperTime}"
            findViewById<TextView>(R.id.detail_thursday_oper_time).text = "목요일: ${detail.thursdayOperTime}"
            findViewById<TextView>(R.id.detail_friday_oper_time).text = "금요일: ${detail.fridayOperTime}"
            findViewById<TextView>(R.id.detail_saturday_oper_time).text = "토요일: ${detail.saturdayOperTime}"
            findViewById<TextView>(R.id.detail_holiday_oper_time).text = "공휴일: ${detail.holidayOperTime}"

            // 내비게이션 버튼 설정
            findViewById<LinearLayout>(R.id.btnNaverMap).setOnClickListener {
                openNaverMap(latitude, longitude, detail.address)
            }

            findViewById<LinearLayout>(R.id.btnKakaoNavi).setOnClickListener {
                openKakaoNavi(latitude, longitude, detail.address)
            }

            findViewById<LinearLayout>(R.id.btnTmap).setOnClickListener {
                openTmap(latitude, longitude, detail.address)
            }
        }

        setupCongestionTable()
    }

    private fun openNaverMap(latitude: Double, longitude: Double, name: String) {
        try {
            val encodedName = URLEncoder.encode(name, "UTF-8")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("nmap://route/car?dlat=$latitude&dlng=$longitude&dname=$encodedName"))
            startActivity(intent)
        } catch (e: Exception) {
            try {
                // 네이버 지도가 설치되어 있지 않은 경우 웹 버전으로 열기
                val encodedName = URLEncoder.encode(name, "UTF-8")
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://map.naver.com/v5/directions/car?destination=$encodedName&destinationLat=$latitude&destinationLng=$longitude"))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "네이버 지도를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openKakaoNavi(latitude: Double, longitude: Double, name: String) {
        try {
            val encodedName = URLEncoder.encode(name, "UTF-8")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("kakaonavi://route?ep=$latitude,$longitude&ep_name=$encodedName"))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "카카오내비가 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTmap(latitude: Double, longitude: Double, name: String) {
        try {
            val encodedName = URLEncoder.encode(name, "UTF-8")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tmap://route?goalname=$encodedName&goalx=$longitude&goaly=$latitude"))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "티맵이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCongestionTable() {
        val tableLayout = findViewById<TableLayout>(R.id.congestionTableLayout)
        val btnShowMore = findViewById<Button>(R.id.btnShowMore)
        
        // 현재 시간 가져오기
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        
        // 초기에 보여줄 행의 수
        val initialRows = 5
        // 남은 시간 계산 (24시까지)
        val remainingHours = 24 - currentHour
        
        // 시간대별 행 추가
        for (i in 0 until minOf(initialRows, remainingHours)) {
            val hour = (currentHour + i) % 24
            addTimeRow(tableLayout, hour)
        }
        
        // 더보기 버튼 처리
        if (remainingHours > initialRows) {
            btnShowMore.visibility = View.VISIBLE
            var isExpanded = false
            
            btnShowMore.setOnClickListener {
                if (!isExpanded) {
                    // 나머지 시간대 추가
                    for (i in initialRows until remainingHours) {
                        val hour = (currentHour + i) % 24
                        addTimeRow(tableLayout, hour)
                    }
                    btnShowMore.text = "접기"
                } else {
                    // 초기 상태로 되돌리기
                    while (tableLayout.childCount > initialRows + 1) { // +1은 헤더 row
                        tableLayout.removeViewAt(tableLayout.childCount - 1)
                    }
                    btnShowMore.text = "더보기"
                }
                isExpanded = !isExpanded
            }
        }
    }
    
    private fun addTimeRow(tableLayout: TableLayout, hour: Int) {
        val row = TableRow(this).apply {
            setPadding(8, 8, 8, 8)
        }
        
        // 시간
        val timeText = TextView(this).apply {
            text = String.format("%02d:00", hour)
            gravity = Gravity.CENTER
        }
        
        // 혼잡도 (임시 데이터)
        val congestionText = TextView(this).apply {
            text = "-"
            gravity = Gravity.CENTER
        }
        
        // 예상 주차면 (임시 데이터)
        val spacesText = TextView(this).apply {
            text = "-"
            gravity = Gravity.CENTER
        }
        
        row.addView(timeText)
        row.addView(congestionText)
        row.addView(spacesText)
        
        tableLayout.addView(row)
    }
}