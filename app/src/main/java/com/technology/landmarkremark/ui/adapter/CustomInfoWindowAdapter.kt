package com.technology.landmarkremark.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.technology.landmarkremark.databinding.LayoutCustomInfoWindowBinding

class CustomInfoWindowAdapter(context: Context) : GoogleMap.InfoWindowAdapter {
    private val binding = LayoutCustomInfoWindowBinding.inflate(LayoutInflater.from(context))

    private val displayMetrics = context.resources.displayMetrics// Get screen width dynamically
    private val maxWidth: Int = (displayMetrics.widthPixels * 0.75).toInt() // 75% of screen width

    private fun renderInfoWindow(marker: Marker) {
        // Set the title and snippet dynamically
        val title = marker.title
        val snippet = marker.snippet
        binding.tvTitle.text = title
        binding.tvSnippet.text = snippet
        binding.root.maxWidth = maxWidth
    }

    override fun getInfoContents(marker: Marker): View {
        renderInfoWindow(marker)
        return binding.root
    }

    override fun getInfoWindow(p0: Marker): View? {
        return null// Default window frame, customize the content only
    }
}