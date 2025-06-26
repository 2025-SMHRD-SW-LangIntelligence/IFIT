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
import android.util.Log
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.kakao.sdk.navi.model.CoordType
import java.text.SimpleDateFormat
import java.util.Calendar
import android.widget.ImageButton
import android.widget.VideoView

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

            findViewById<LinearLayout>(R.id.btnTmap).setOnClickListener {
                Log.d("TmapDebug", "전달값 latitude: $latitude, longitude: $longitude, name: ${detail.address}")
                openTmap(latitude, longitude, detail.address)
            }
        }

        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)
        val btnPrevDate = findViewById<ImageButton>(R.id.btnPrevDate)
        val btnNextDate = findViewById<ImageButton>(R.id.btnNextDate)
        val tableLayout = findViewById<TableLayout>(R.id.congestionTableLayout)
        val btnShowMore = findViewById<Button>(R.id.btnShowMore)
        var isExpanded = false

        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val today = Calendar.getInstance()
        var selectedDate = Calendar.getInstance()

        fun updateDateUI() {
            tvSelectedDate.text = dateFormat.format(selectedDate.time)
            btnPrevDate.isEnabled = selectedDate.get(Calendar.DAY_OF_YEAR) > today.get(Calendar.DAY_OF_YEAR)
            btnNextDate.isEnabled = selectedDate.get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR) + 6
        }

        fun updateCongestionTable() {
            while (tableLayout.childCount > 1) {
                tableLayout.removeViewAt(1)
            }
            val maxRows = if (isExpanded) 24 else 10
            val now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            if (isExpanded) {
                for (i in 0 until 24) {
                    val row = TableRow(this)
                    val timeText = TextView(this).apply {
                        text = String.format("%02d:00", i)
                        gravity = Gravity.CENTER
                    }
                    val congestionText = TextView(this).apply {
                        text = ""
                        gravity = Gravity.CENTER
                    }
                    val spacesText = TextView(this).apply {
                        text = ""
                        gravity = Gravity.CENTER
                    }
                    row.addView(timeText)
                    row.addView(congestionText)
                    row.addView(spacesText)
                    tableLayout.addView(row)
                }
            } else {
                for (i in 0 until maxRows) {
                    val hour = (now + i) % 24
                    val row = TableRow(this)
                    val timeText = TextView(this).apply {
                        text = String.format("%02d:00", hour)
                        gravity = Gravity.CENTER
                    }
                    val congestionText = TextView(this).apply {
                        text = ""
                        gravity = Gravity.CENTER
                    }
                    val spacesText = TextView(this).apply {
                        text = ""
                        gravity = Gravity.CENTER
                    }
                    row.addView(timeText)
                    row.addView(congestionText)
                    row.addView(spacesText)
                    tableLayout.addView(row)
                }
            }
            btnShowMore.visibility = if (!isExpanded && 24 > maxRows) View.VISIBLE else if (isExpanded) View.VISIBLE else View.GONE
            btnShowMore.text = if (isExpanded) "접기" else "더보기"
        }

        btnPrevDate.setOnClickListener {
            selectedDate.add(Calendar.DAY_OF_YEAR, -1)
            updateDateUI()
            updateCongestionTable()
        }
        btnNextDate.setOnClickListener {
            selectedDate.add(Calendar.DAY_OF_YEAR, 1)
            updateDateUI()
            updateCongestionTable()
        }

        btnShowMore.setOnClickListener {
            isExpanded = !isExpanded
            updateCongestionTable()
        }

        // 초기화
        updateDateUI()
        updateCongestionTable()

        // 실시간 CCTV 영상 재생 (MediaController 명확히 연결)
        val videoView = findViewById<VideoView>(R.id.videoView)
        val mediaController = android.widget.MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        val cctvUrl = "http://10.0.2.2:8090/uploads/CCTV01.mp4"
        videoView.setVideoURI(Uri.parse(cctvUrl))
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true // 반복재생
            videoView.start()
        }
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

    private fun openTmap(latitude: Double, longitude: Double, name: String) {
        try {
            val encodedName = URLEncoder.encode(name, "UTF-8")
            val url = "tmap://route?goalx=$longitude&goaly=$latitude&goalname=$encodedName&coordType=WGS84"
            Log.d("TmapIntent", "티맵 URI: $url")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "티맵이 설치되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}