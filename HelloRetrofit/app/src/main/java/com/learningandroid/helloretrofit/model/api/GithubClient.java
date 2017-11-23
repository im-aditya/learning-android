package com.learningandroid.helloretrofit.model.api;

import com.learningandroid.helloretrofit.model.data.GithubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Aditya on 11/23/2017.
 */

public interface GithubClient
{
    @GET("/users/{user}/repos")
    Call<List<GithubRepo>> reposForUser(@Path("user") String user);
}
