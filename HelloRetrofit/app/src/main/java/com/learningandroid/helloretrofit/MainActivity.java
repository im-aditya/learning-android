package com.learningandroid.helloretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.learningandroid.helloretrofit.model.api.GithubClient;
import com.learningandroid.helloretrofit.model.data.GithubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
{
    private static final String BASE_URL = "https://api.github.com";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = this.findViewById(R.id.tv_field);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubClient githubClient = retrofit.create(GithubClient.class);
        Call<List<GithubRepo>> githubRepoListCall = githubClient.reposForUser("octocat");
        githubRepoListCall.enqueue(new Callback<List<GithubRepo>>()
        {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response)
            {
                Toast.makeText(MainActivity.this, "Got it:" + response.body().size(), Toast.LENGTH_SHORT).show();
                List<GithubRepo> repos = response.body();
                int len = repos.size();
                for (int i=0; i<len; i++)
                {
                    //Log.d(MainActivity.class.getSimpleName(), "Repo " + i + " : " + repos.get(i).getName());
                    textView.append("\nRepo " + i + " : " + repos.get(i).getName());
                }
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t)
            {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
