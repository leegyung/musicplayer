package edu.msu.leegyung.musicplayer

import android.content.Context
import android.media.MediaPlayer
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat
import java.util.*
import kotlin.concurrent.timer

class MusicController (private val context : Context, private val adapter: MusicAdapter){

    private var currentMusic : Int? = null
    private var mPlayer = MediaPlayer.create(context,  adapter.musicList[0].music)
    private lateinit var seekBar : SeekBar
    private lateinit var mTimer : Timer

    val mFormat = DecimalFormat("00")


    private var mProgress = -1
    private var onChanging = false


    fun getCurrentMusic() : Int? { return currentMusic }
    fun isPlaying() : Boolean {return mPlayer.isPlaying}


    private fun setCompletion(){
        mPlayer.setOnCompletionListener {

        }

    }

    fun startAndStop(pos : Int){
        if(pos == -1){
            when {
                currentMusic == null -> {
                    currentMusic = 0
                    mPlayer = MediaPlayer.create(context,  adapter.musicList[0].music)
                    mPlayer.start()
                }
                mPlayer.isPlaying -> {
                    mPlayer.pause()
                }
                else -> {
                    mPlayer.start()
                }
            }
        }
        else{
            if(mPlayer.isPlaying){
                mPlayer.stop()
                mPlayer = MediaPlayer.create(context,  adapter.musicList[pos].music)
                mPlayer.start()
                currentMusic = pos
            }
            else{
                currentMusic = pos
                mPlayer = MediaPlayer.create(context,  adapter.musicList[pos].music)
                mPlayer.start()
            }
        }
    }

    fun initSeekBar(){
        seekBar = (context as MainActivity).seekBar
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (onChanging){ mProgress = progress }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                onChanging = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(currentMusic != null){
                    val dura = (mPlayer.duration * (mProgress.toDouble() / 100)).toInt()
                    mPlayer.seekTo(dura)
                    setDuration(dura / 1000, mPlayer.duration / 1000)
                }
                onChanging = false
            }

        })

    }

    fun setSeekBar(){
        if(currentMusic != null && mPlayer.isPlaying){
            val i = mPlayer.currentPosition / 1000
            val ii = mPlayer.duration / 1000
            val iii = i.toDouble() / ii.toDouble() * 100
            setDuration(i,ii)
            (context as MainActivity).seekBar.progress = iii.toInt()
        }
    }

    private fun setDuration(i : Int, ii : Int){
        val time = "${mFormat.format(i / 60)} : ${mFormat.format(i % 60)}"
        val remain = "${mFormat.format((ii - i) / 60)} : ${mFormat.format((ii - i) % 60)}"
        (context as MainActivity).currentTime.text = time
        context.remainTime.text = remain
    }





}