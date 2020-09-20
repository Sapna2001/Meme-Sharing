package com.example.memesharing

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentUrl : String ? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme();
    }

    private fun loadMeme() {
//        val url = "https://www.google.com"
//
//        // Request a string response from the provided URL.
//        val stringRequest = StringRequest(Request.Method.GET, url,
//            Response.Listener<String> { response ->
//                // Display the first 500 characters of the response string.
//                Log.d("success",response.substring(0, 500))
//            },
//            Response.ErrorListener {
//                Log.d("Error",it.localizedMessage)
//            })

        progress_bar.visibility = View.VISIBLE;
        currentUrl = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, currentUrl, null,
            Response.Listener { response ->
                val url = response.getString("url");

                Glide.with(this).load(url).listener(object : RequestListener<Drawable>{

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_bar.visibility = View.GONE;
                        return false;
                    }

                }).into(memeImageView)
            },
            Response.ErrorListener { error ->
                progress_bar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }
        )

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND);
        intent.type = "text/plain";
        intent.putExtra(Intent.EXTRA_TEXT,"Hi, checkout this meme $currentUrl");
        val chooser = Intent.createChooser(intent, "Share this meme using ...");
        startActivity(chooser);
    }

    fun nextMeme(view: View) {
        loadMeme();
    }
}

// api -> https://meme-api.herokuapp.com/gimme