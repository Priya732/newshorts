package com.example.newsshorts

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.newsshorts.databinding.ActivityMainBinding
import org.json.JSONObject


class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter= NewsListAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
        fetchData()

    }
    private fun fetchData() {
        val url="https://newsapi.org/v2/top-headlines?country=in&category=business"
        val queue = Volley.newRequestQueue(this)
        val req = object : StringRequest(
            Method.GET, url,
            Response.Listener { response -> // response
                Log.d("Response", response)
                val adapterList = ArrayList<News>()
                val obj = JSONObject(response)
                val list = obj.getJSONArray("articles")
                for (i in 0 until list.length()) {
                    val res = list[i] as? JSONObject
                    adapterList.add(News(title = res?.getString("title") ?: "", author = res?.getString("author") ?: "", url = res?.getString("url") ?: "", imageUrl = res?.getString("urlToImage") ?: ""))
                }
                Log.d("Response List", adapterList.toString())
                mAdapter.updateNews(adapterList)
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", "error => $error")

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["X-Api-Key"] = "ecd4bb4b801f4f2ab18ca91ce364e7e3"
                params["User-Agent"]="Mozilla/5.0"
                return params
            }
        }
        queue.add(req)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }


}