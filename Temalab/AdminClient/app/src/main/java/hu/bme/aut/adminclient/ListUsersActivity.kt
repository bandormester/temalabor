package hu.bme.aut.adminclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.gson.GsonBuilder
import hu.bme.aut.adminclient.adapter.CostumerAdapter
import hu.bme.aut.adminclient.model.Costumer
import hu.bme.aut.adminclient.retrofit.RetroListUsers
import kotlinx.android.synthetic.main.activity_list_users.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListUsersActivity : AppCompatActivity(), CostumerAdapter.CostumerItemClickListener {

    private lateinit var costumerAdapter: CostumerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_users)
        costumerAdapter = CostumerAdapter()
        RecyclerViewUsers.adapter = costumerAdapter

       /* var gson = GsonBuilder().setLenient().create()

        var builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://penzfeldobas.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))

        var retrofit : Retrofit = builder.build()

        val retroListUsers = retrofit.create(RetroListUsers::class.java)

        val loginDetails = "admin:admin"

        val header : String = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val call = retroListUsers.getUserList(header)

        call.enqueue(object : Callback<List<Costumer>> {
            override fun onFailure(call: Call<List<Costumer>>, t: Throwable) {
                Log.d("retrofit", "Listing failed")
            }

            override fun onResponse(
                call: Call<List<Costumer>>,
                response: Response<List<Costumer>>
            ) {
                Log.d("retrofit", "Listing succeeded")
                var Users = response.body()?: listOf<Costumer>()

                setupRecyclerView(Users)

                Log.d("retrofit", Users[1].firstName)
            }

        })*/

    }

    lateinit var username : String
    lateinit var password : String

    override fun onStart() {
        super.onStart()

        val gson = GsonBuilder().setLenient().create()

        val builder : Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://ec2-3-14-28-216.us-east-2.compute.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit : Retrofit = builder.build()

        val retroListUsers = retrofit.create(RetroListUsers::class.java)

         username = intent.getStringExtra("username")?:""
         password = intent.getStringExtra("password")?:""
        val loginDetails = "$username:$password"

        val header : String = "Basic " + Base64.encodeToString(loginDetails.toByteArray(), Base64.NO_WRAP)

        val call = retroListUsers.getUserList(header)

        call.enqueue(object : Callback<List<Costumer>> {
            override fun onFailure(call: Call<List<Costumer>>, t: Throwable) {
                Log.d("retrofit", "Listing failed")
            }

            override fun onResponse(
                call: Call<List<Costumer>>,
                response: Response<List<Costumer>>
            ) {
                Log.d("retrofit", "Listing succeeded")
                var Users = response.body()?: listOf<Costumer>()
                setupRecyclerView(Users)

                Log.d("retrofit", Users[1].firstName)
            }

        })
    }

    private fun setupRecyclerView(UserList : List<Costumer>) {

        costumerAdapter.itemClickListener = this
        costumerAdapter.clear()
        costumerAdapter.addAll(UserList)
        RecyclerViewUsers.adapter = costumerAdapter
    }

    override fun onCostumerSelected(costumer: Costumer) {
        Log.d("detview","user clicked")
        val intent = Intent(this, CostumerDetailActivity::class.java)
        intent.putExtra(CostumerDetailActivity.DETAIL_ID,costumer.customerId)
        intent.putExtra(CostumerDetailActivity.USER_NAME,username)
        intent.putExtra(CostumerDetailActivity.USER_PASS,password)
        Log.d("retrofit",costumer.customerId.toString())
        intent.putExtra(CostumerDetailActivity.DETAIL_EMAIL,costumer.emailAddress)
        intent.putExtra(CostumerDetailActivity.DETAIL_PHONE,costumer.phone)
        intent.putExtra(CostumerDetailActivity.DETAIL_LICENCE,costumer.licenceCardNumber)
        intent.putExtra(CostumerDetailActivity.DETAIL_FIRST,costumer.firstName)
        intent.putExtra(CostumerDetailActivity.DETAIL_LAST,costumer.lastName)
        intent.putExtra(CostumerDetailActivity.DETAIL_EXPIRATION,costumer.expirationDate)
        intent.putExtra(CostumerDetailActivity.DETAIL_ENABLED,costumer.enabled)
        startActivity(intent)
    }
}
