package com.example.kkon


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.listlayout.view.*
import kotlin.coroutines.coroutineContext
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.centerCrop
import android.R.transition
import android.util.Log
import com.bumptech.glide.GlideContext
import com.bumptech.glide.load.model.GlideUrl
import com.koushikdutta.ion.Ion


class Myadater(val items:ArrayList<Data>)
    :RecyclerView.Adapter<Myadater.ViewHolder>(){
    interface OnItemClickListener{
        fun OnItemClick(holder: ViewHolder,view: View,data:Data,position:Int)
        fun OnLikeClick(holder: ViewHolder,view: View,data:Data,position:Int)
        fun OnimageClick(holder: ViewHolder,view: View,data:Data,position:Int)
    }
    var itemClickListener:OnItemClickListener?=null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.listlayout,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        p0.name.text = items.get(p1).Id
        p0.age.text = items.get(p1).sta
        when(items.get(p1).like){
            0 -> p0.like.setImageResource(R.drawable.unlikee)
            1 -> p0.like.setImageResource(R.drawable.likee)
        }
        Ion.with(p0.img).load(items.get(p1).img)
    }

    override fun getItemCount(): Int {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return items.size
    }
    inner class ViewHolder(itemView: View) //사용할멤버 만들기
        : RecyclerView.ViewHolder(itemView){
        var name: TextView
        var age: TextView
        var like: ImageButton
        var img: ImageButton
        init{ //초기화를 해줄수있는 init블록
            name = itemView.findViewById(R.id.text1)
            age = itemView.findViewById(R.id.text2)
            like = itemView.findViewById(R.id.imageView)
            img = itemView.findViewById(R.id.photo)
            itemView.setOnClickListener {
                val position=adapterPosition
                itemClickListener?.OnItemClick(this,it,items[position],position)
            }
            itemView.imageView.setOnClickListener {
                val position=adapterPosition
                itemClickListener?.OnLikeClick(this,it,items[position],position)
            }
            itemView.photo.setOnClickListener {
                val position=adapterPosition
                Log.d("account_cnt","adapter쪽")
                itemClickListener?.OnimageClick(this,it,items[position],position)
            }

        }
    }
    inner class MyAppGlideModule : AppGlideModule()
}