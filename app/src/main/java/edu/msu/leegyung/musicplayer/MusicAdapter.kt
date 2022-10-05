package edu.msu.leegyung.musicplayer

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.music_recycle.view.*
import java.util.*


class MusicAdapter(private val context : Context) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    val musicList = ArrayList<Music>()
    var playingMusic = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.music_recycle,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicAdapter.ViewHolder, position: Int) {
        holder.bind(musicList[position], position)
    }

    override fun getItemCount(): Int = musicList.size



    inner class ViewHolder(view : View): RecyclerView.ViewHolder(view){
        fun bind(item : Music, pos : Int){
            itemView.music_name.text = item.name ?: ""
            itemView.artist_name.text = item.artist ?: ""
            Glide.with(itemView).load(item.albumArt).into(itemView.album_view)

            itemView.music_name.isSingleLine = true
            itemView.music_name.ellipsize = TextUtils.TruncateAt.MARQUEE
            itemView.music_name.isSelected = true
            itemView.music_name.marqueeRepeatLimit = -1



            itemView.setOnClickListener {
                (context as MainActivity).setMusic(adapterPosition)
                playingMusic = pos
            }
        }
    }


}