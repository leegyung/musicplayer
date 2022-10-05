package edu.msu.leegyung.musicplayer

import android.Manifest
import android.content.ContentUris
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.music_recycle.view.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var permission = 1
    private val permissionArr = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    private lateinit var musicAdapter : MusicAdapter

    private lateinit var mController : MusicController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, permissionArr, permission)
        musicInit()
        initButtons()

        music_title.isSingleLine = true
        music_title.ellipsize = TextUtils.TruncateAt.MARQUEE
        music_title.isSelected = true
        music_title.marqueeRepeatLimit = -1

    }



    private fun musicInit(){
        musicAdapter = MusicAdapter(this)
        MusicList.adapter = musicAdapter



        val cursor : Cursor? = this.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null)

        if (cursor != null){
            while(cursor.moveToNext()){
                val name = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val album = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID)
                val type = cursor.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE)
                val mp3 = cursor.getColumnIndex(MediaStore.Audio.Media._ID)

                if(cursor.getString(type) != "audio/ogg"){
                    val musicNum = cursor.getString(mp3)
                    val albumNum = cursor.getLong(album)


                    val music = Music(
                        cursor.getString(name),
                        cursor.getString(artist),
                        albumURI(albumNum),
                        Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + musicNum)
                    )

                    musicAdapter.musicList.add(music)
                }
            }
            cursor.close()
            musicAdapter.notifyDataSetChanged()
        }

        mController = MusicController(this, musicAdapter)
        mController.initSeekBar()
        timer(period = 1000){
            runOnUiThread{
                mController.setSeekBar()
            }
        }
    }

    private fun albumURI(albumId : Long) : Uri{
        val picUri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(picUri, albumId)
    }

    fun setMusic(pos : Int){
        mController.startAndStop(pos)
        setPlayingUI()
    }

    private fun initButtons(){
        previous.setOnClickListener {  }

        play.setOnClickListener {
            mController.startAndStop(-1)
            setPlayingUI()
        }

        next_btn.setOnClickListener {  }
    }

    private fun setPlayingUI(){

        if (mController.isPlaying()){
            play.setImageResource(R.drawable.pause)
        }
        else{
            play.setImageResource(R.drawable.play)
        }


        val pos : Int? = mController.getCurrentMusic()
        if(pos != null){
            AlbumPic.setImageURI(musicAdapter.musicList[pos].albumArt)
            music_title.text = musicAdapter.musicList[pos].name
            artist.text = musicAdapter.musicList[pos].artist
        }

    }




}