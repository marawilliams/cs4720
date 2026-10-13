package edu.virginia.cs4720.collegefootballscores

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{division}/{year}/{week}")
    suspend fun getGames(
        @Path("division") division: String,
        @Path("year") year: Int,
        @Path("week") week: String
    ): ScoreBoardResponse
}