package com.example.h2e

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_day.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detailinreview.view.*
import kotlinx.android.synthetic.main.detailinreview.view.dishname
import kotlinx.android.synthetic.main.showweight.view.*
import java.time.LocalDate
import java.time.LocalDateTime

var Gok_dan_sum: Double = 0.0
var Uju_dan_sum: Double = 0.0
var Ujung_dan_sum: Double = 0.0
var Veg_dan_sum: Double = 0.0
var Fat_dan_sum: Double = 0.0
var Milk_dan_sum: Double = 0.0
var Fruit_dan_sum: Double = 0.0
var kcal_sum: Double = 0.0

var gok_gun0 = 0.0
var uju_gun0 = 0.0
var ujung_gun0 = 0.0
var veg_gun0 = 0.0
var fat_gun0 = 0.0
var milk_gun0 = 0.0
var fruit_gun0 = 0.0
var CanEatKcal0 = 0.0

class WatchYourWeight (val loseWeight : String? = null, val RealWeight : String? = null, val loseDate : String? = null )

class DayActivity : AppCompatActivity() {

    lateinit var time: String
    lateinit var Meal: Intent

    val firestore = FirebaseFirestore.getInstance()

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    var user = mAuth.currentUser?.uid.toString()

    var onlyDate: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)

        DayRecycler.adapter = loseViewAdapter(this)
        DayRecycler.layoutManager = LinearLayoutManager(this).also { it.orientation = LinearLayoutManager.HORIZONTAL }
    }

    inner class loseViewAdapter(val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var WeightArray: ArrayList<WatchYourWeight> = arrayListOf()

        init {
            firestore?.collection(user + "DayWeight")
                ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    WeightArray.clear()

                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(WatchYourWeight::class.java)
                        WeightArray.add(item!!)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.showweight, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            viewHolder.predictweight.text = WeightArray[position].loseWeight
            viewHolder.realweight.text = WeightArray[position].RealWeight
            viewHolder.DietDate.text = WeightArray[position].loseDate
        }

        override fun getItemCount(): Int {
            return WeightArray.size
        }
    }

    override fun onStart() {
        super.onStart()

        FillProgressBar()

//        FillUserData()

        btnDetailMorning.setOnClickListener {
            var DetailView = (Intent(this, DetailActivity::class.java))
            DetailView.putExtra("whichtime", "morning")

            startActivity(DetailView)
        }

        btnDetailLunch.setOnClickListener {
            var DetailView = (Intent(this, DetailActivity::class.java))
            DetailView.putExtra("whichtime", "lunch")

            startActivity(DetailView)
        }

        btnDetailDinner.setOnClickListener {
            var DetailView = (Intent(this, DetailActivity::class.java))
            DetailView.putExtra("whichtime", "dinner")

            startActivity(DetailView)
        }
    }



    fun FillProgressBar() {
        GetData("morning", morningKcal_text, morninggram_text)
        GetData("lunch", lunchKcal_text, lunchgram_text)
        GetData("dinner",dinnerKcal_text, dinnergram_text )

        Gok_dan_sum = 0.0
        Ujung_dan_sum = 0.0
        Veg_dan_sum = 0.0
        kcal_sum = 0.0
    }

    fun get_morning(v: View) {
        time = "morning"
        Meal = Intent(this, CheckMealActivity::class.java)
        Meal.putExtra("meal", time)

        startActivity(Meal)
    }

    fun get_lunch(v: View) {
        time = "lunch"
        Meal = Intent(this, CheckMealActivity::class.java)
        Meal.putExtra("meal", time)

        startActivity(Meal)
    }

    fun get_dinner(v: View) {
        time = "dinner"
        Meal = Intent(this, CheckMealActivity::class.java)
        Meal.putExtra("meal", time)

        startActivity(Meal)
    }

    private fun GetData(Time: String, KcalText : TextView, DanTextView: TextView) {
        firestore.collection(user)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        if (document.id == "profile_ingre_adopted") {
                            var gok_gun = document.data["gok"]
                            var uju_gun = document.data["uju"]
                            var ujung_gun = document.data["ujung"]
                            var ugo_gun = document.data["ugo"]
                            var veg_gun = document.data["veg"]
                            var fat_gun = document.data["fat"]
                            var milk_gun = document.data["milk"]
                            var fruit_gun = document.data["fruit"]
                            var CanEatKcal = document.data["canEatKcal"]
                            var dietSystem = document.data["dietSystem"]
                            var username = document.data["name"]

                            gokdetailtext_adopted.setText(gok_gun.toString())
                            ujudetailtext_adopted.setText(uju_gun.toString())
                            ujungdetailtext_adopted.setText(ujung_gun.toString())
                            ugodetailtext_adopted.setText(ugo_gun.toString())
                            vegdetailtext_adopted.setText(veg_gun.toString())
                            milkdetailtext_adopted.setText(milk_gun.toString())
                            fruitdetailtext_adopted.setText(fruit_gun.toString())
                            fatdetailtext_adopted.setText(fat_gun.toString())
                            kcalDetailtext.setText(CanEatKcal.toString())
                            dishname_adopted.setText(dietSystem.toString())
                            UserName.setText(username.toString())

                            if (gok_gun is String) {
                                gok_gun0 = gok_gun.toDouble()
                            }
                            if (uju_gun is String) {
                                uju_gun0 = uju_gun.toDouble()
                            }
                            if (ujung_gun is String) {
                                ujung_gun0 = ujung_gun.toDouble()
                            }
                            if (veg_gun is String) {
                                veg_gun0 = veg_gun.toDouble()
                            }
                            if (fat_gun is String) {
                                fat_gun0 = fat_gun.toDouble()
                            }
                            if (milk_gun is String) {
                                milk_gun0 = milk_gun.toDouble()
                            }
                            if (fruit_gun is String) {
                                fruit_gun0 = fruit_gun.toDouble()
                            }
                            if (CanEatKcal is String) {
                                CanEatKcal0 = CanEatKcal.toDouble()
                            }

                            firestore.collection(user)
                                .get()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        for (document in task.result!!) {
                                            if (document.id == onlyDate.toString() + Time) {
                                                var Gok_dan0 = document.data["gok_data"]
                                                var Uju_dan0 = document.data["uju_data"]
                                                var Ujung_dan0 = document.data["ujung_data"]
                                                var Veg_dan0 = document.data["veg_data"]
                                                var Fat_dan0 = document.data["fat_data"]
                                                var Milk_dan0 = document.data["milk_data"]
                                                var Fruit_dan0 = document.data["fruit_data"]

                                                var kcal_frag = document.data["kcal"]

                                                if (Gok_dan0 is String) {
                                                    Gok_dan0 = Gok_dan0.toDouble()
                                                    Gok_dan_sum = Gok_dan_sum + Gok_dan0 / gok_gun0 * 100
                                                }
                                                if (Uju_dan0 is String) {
                                                    Uju_dan0 = Uju_dan0.toDouble()
                                                    Ujung_dan_sum = Ujung_dan_sum + Uju_dan0 / ujung_gun0 * 100
                                                }
                                                if (Ujung_dan0 is String) {
                                                    Ujung_dan0 = Ujung_dan0.toDouble()
                                                    Ujung_dan_sum = Ujung_dan_sum + Ujung_dan0 / ujung_gun0 * 100
                                                }
                                                if (Veg_dan0 is String) {
                                                    Veg_dan0 = Veg_dan0.toDouble()
                                                    Veg_dan_sum = Veg_dan_sum + Veg_dan0 / veg_gun0 * 100
                                                }
                                                if (Fat_dan0 is String) {
                                                    Fat_dan0 = Fat_dan0.toDouble()
                                                    Fat_dan_sum = Fat_dan_sum + Fat_dan0 / fat_gun0 * 100
                                                }
                                                if (Milk_dan0 is String) {
                                                    Milk_dan0 = Milk_dan0.toDouble()
                                                    Milk_dan_sum = Milk_dan_sum + Milk_dan0 / milk_gun0 * 100
                                                }
                                                if (Fruit_dan0 is String) {
                                                    Fruit_dan0 = Fruit_dan0.toDouble()
                                                    Fruit_dan_sum = Fruit_dan_sum + Fruit_dan0 / fruit_gun0 * 100
                                                }
                                                if (kcal_frag is String) {
                                                    kcal_frag = kcal_frag.toDouble()
                                                    kcal_sum = kcal_sum + kcal_frag / CanEatKcal0 * 100
                                                }

                                                var IngreSum = Gok_dan0.toString().toDouble() + Uju_dan0.toString().toDouble() +
                                                        Ujung_dan0.toString().toDouble()  + Veg_dan0.toString().toDouble()  +
                                                        Fat_dan0.toString().toDouble()  + Milk_dan0.toString().toDouble()  +
                                                        Fruit_dan0.toString().toDouble()

                                                ProgressBar_gok.setProgress(Gok_dan_sum.toInt())
                                                ProgressBar_uju.setProgress(Uju_dan_sum.toInt())
                                                ProgressBar_ujung.setProgress(Ujung_dan_sum.toInt())
                                                ProgressBar_veg.setProgress(Veg_dan_sum.toInt())
                                                ProgressBar_fat.setProgress(Fat_dan_sum.toInt())
                                                ProgressBar_milk.setProgress(Milk_dan_sum.toInt())
                                                ProgressBar_fruit.setProgress(Fruit_dan_sum.toInt())
                                                ProgressBar_Kcal.setProgress(kcal_sum.toInt())

                                                KcalText.setText(kcal_frag.toString())
                                                DanTextView.setText(IngreSum.toString())
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





