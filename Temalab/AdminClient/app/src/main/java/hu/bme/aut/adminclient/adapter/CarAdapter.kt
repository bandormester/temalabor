package hu.bme.aut.adminclient.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import hu.bme.aut.adminclient.R
import hu.bme.aut.adminclient.model.Car
import hu.bme.aut.adminclient.model.Costumer
import hu.bme.aut.adminclient.model.EngineType
import kotlinx.android.synthetic.main.activity_costumer_detail.*
import kotlinx.android.synthetic.main.car_row.view.*
import kotlinx.android.synthetic.main.costumer_row.view.*
import kotlinx.android.synthetic.main.costumer_row.view.ivProfilePic
import java.io.File
import javax.xml.transform.Templates

class CarAdapter : RecyclerView.Adapter<CarAdapter.CarHolder>() {

    private val carList = mutableListOf<Car>()

    var itemClickListener : CarItemClickListener? = null

    lateinit var  context : Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarHolder {
        Log.d("recview","on create view holder")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.car_row, parent, false)
        context=parent.context
        return CarHolder(view)
    }



    fun addItem(item : Car) {
        Log.d("recview","add-item")
        val size = carList.size
        carList.add(item)
        notifyItemInserted(size)
    }

    fun clear(){
        val size = carList.size
        carList.clear()
        notifyItemRangeChanged(size,0)
    }

    fun addAll(cars : List<Car>){
        val size = carList.size
        carList += cars
        notifyItemRangeChanged(size, cars.size)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(holder: CarHolder, position: Int) {
        val car = carList[position]

        holder.car = car

        holder.tvCarModel.text = (car.brand+" "+car.model)
        holder.tvCarColor.text = car.color
        holder.tvCarKm.text = (car.currentKm.toString() + " Km")

        when(car.engineType?:EngineType.DIESEL){
            EngineType.DIESEL.toString() -> holder.ivEnginePic.setImageResource(R.mipmap.ic_disel)
            EngineType.ELECTRIC.toString() -> holder.ivEnginePic.setImageResource(R.mipmap.ic_electric)
            EngineType.BENZINE.toString() -> holder.ivEnginePic.setImageResource(R.mipmap.ic_benzine)
            else->holder.ivEnginePic.setImageResource(R.drawable.ic_launcher_background)
        }
        if(position%2==0)holder.rowCarBg.setBackgroundColor(Color.rgb(240,240,240))
    }

    inner class CarHolder(carView : View) : RecyclerView.ViewHolder(carView) {
        val tvCarModel: TextView = carView.tvCarModel
        val tvCarColor: TextView = carView.tvCarColor
        val tvCarKm: TextView = carView.tvKilometer
        val rowCarBg: ConstraintLayout = carView.layoutCarRow
        val ivEnginePic = carView.ivEnginePic

        var car : Car? = null
        init{
            carView.setOnClickListener{
                car?.let{itemClickListener?.onCarSelected(it)}
            }
        }
    }

    interface CarItemClickListener {
        fun onCarSelected(car: Car)
    }
}
