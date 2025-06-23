package com.example.ch_19_map

import retrofit2.Response
import retrofit2.http.*

// 백엔드 API 인터페이스
interface ApiService {
    
    // 주차장 관련 API
    @GET("api/parkinglots")
    suspend fun getAllParkingLots(): Response<List<ParkingLotResponse>>
    
    @GET("api/parkinglots/search")
    suspend fun searchParkingLots(@Query("keyword") keyword: String): Response<List<ParkingLotResponse>>
    
    @GET("api/parkinglots/{name}")
    suspend fun getParkingLotByName(@Path("name") name: String): Response<ParkingLotResponse>
    
    @POST("api/parkinglots")
    suspend fun registerParkingLot(@Body request: ParkingLotRequest): Response<String>
    
    // 사용자 관련 API
    @POST("api/user/register")
    suspend fun registerUser(@Body request: UserRegisterRequest): Response<UserResponse>
    
    @POST("api/user/login")
    suspend fun loginUser(@Body request: UserLoginRequest): Response<LoginResponse>
    
    // 즐겨찾기 관련 API
    @POST("api/favorites")
    suspend fun addFavorite(@Body favoriteRequest: FavoriteRequest): Response<String>
    
    @HTTP(method = "DELETE", path = "api/favorites", hasBody = true)
    suspend fun removeFavorite(@Body favoriteRequest: FavoriteRequest): Response<String>
    
    @GET("api/favorites/{userId}")
    suspend fun getFavorites(@Path("userId") userId: Long): Response<List<Favorite>>

    // 고객센터(게시판) 관련 API
    @GET("api/board/posts")
    suspend fun getAllBoardPosts(): Response<List<BoardPost>>

    @POST("api/board/posts")
    suspend fun createBoardPost(@Body request: BoardPostRequest): Response<BoardPost>

    @DELETE("api/board/posts/{id}")
    suspend fun deleteBoardPost(@Path("id") id: Long): Response<Void>

    @PUT("api/board/posts/{id}")
    suspend fun updateBoardPost(@Path("id") id: Long, @Body request: BoardPostRequest): Response<BoardPost>

    @POST("api/board/reply")
    suspend fun createBoardReply(@Body request: BoardReplyRequest): Response<BoardReplyResponse>

    @GET("api/board/reply/{postId}")
    suspend fun getBoardReply(@Path("postId") postId: Long): Response<BoardReplyResponse>

    @GET("api/board/posts/user/{userId}")
    suspend fun getBoardPostsByUser(@Path("userId") userId: Long): Response<List<BoardPost>>
}

// 데이터 클래스들
data class ParkingLotResponse(
    val id: Long,
    val name: String,
    val address: String,
    val runtime: String,
    val total_space: Int,
    val fee: Int,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)

data class ParkingLotRequest(
    val name: String,
    val address: String,
    val runtime: String,
    val total_space: Int,
    val fee: Int
)

data class UserRegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class UserLoginRequest(
    val email: String,
    val password: String
)

data class UserResponse(
    val success: Boolean,
    val message: String
)

data class LoginResponse(
    val id: Long,
    val username: String,
    val email: String,
    val role: String
)

data class BoardReplyRequest(
    val postId: Long,
    val userId: Long,
    val content: String
)

data class BoardReplyResponse(
    val id: Long,
    val content: String,
    val createdAt: String,
    val author: Author
) 