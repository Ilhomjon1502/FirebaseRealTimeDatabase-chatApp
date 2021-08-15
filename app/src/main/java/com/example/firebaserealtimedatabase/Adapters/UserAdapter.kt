package com.example.firebaserealtimedatabase.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaserealtimedatabase.Models.User
import com.example.firebaserealtimedatabase.databinding.ItemUserBinding
import com.squareup.picasso.Picasso

class UserAdapter(val list: List<User>, var onItemCLickListener: OnItemCLickListener) :RecyclerView.Adapter<UserAdapter.Vh>(){
    inner class Vh(var itemRv:ItemUserBinding):RecyclerView.ViewHolder(itemRv.root){

        fun onBind(user: User, position: Int){
            itemRv.emailTv.text = user.email
            itemRv.nameTv.text = user.displayName

            Picasso.get().load(user.photoUrl).into(itemRv.imageItemUser)

            itemRv.root.setOnClickListener {
                onItemCLickListener.onCLick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemCLickListener{
        fun onCLick(user: User)
    }
}