package com.example.mapa

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapa.databinding.Item2Binding
import com.example.mapa.databinding.ItemBinding
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SklepyAdapter(val context: Context, val list: ArrayList<Shop>, val ref: DatabaseReference) :
        RecyclerView.Adapter<SklepyAdapter.MyViewHolder>() {

    init {
        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previous: String?) {


                CoroutineScope(IO).launch {
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }

            }
            override fun onChildChanged(snapshot: DataSnapshot, previous: String?) {
                CoroutineScope(IO).launch {
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

                CoroutineScope(IO).launch {
                    withContext(Main) {
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    class MyViewHolder(val binding: Item2Binding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = Item2Binding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val p = list[position]
        holder.binding.rvName.text = p.name.toString()+" "
        holder.binding.latlon.text = p.lat.toString()+" / "+p.lon.toString()+" "
        holder.binding.desc.text = p.desc.toString()+" "
        holder.binding.rad.text = p.radius.toString()+" "

        holder.binding.button6.setOnClickListener {
            var keyList = list[position].key
            ref.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(IO).launch {
                        ref.child(keyList).removeValue()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }



    }


    override fun getItemCount(): Int = list.size

}





