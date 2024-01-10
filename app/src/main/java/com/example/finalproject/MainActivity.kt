package com.example.finalproject


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import java.io.Serializable

data class TravelMemory(
    val id:Long = -1,
    val placeName: String,
    val dateOfTravel: String,
    val travelType: String,
    val travelMood: String,
    var isFavorite: Boolean = false
):Serializable
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var adapter: TravelMemoryAdapter
    private lateinit var dbHelper: TravelMemoryDbHelper
    private var showFavoritesOnly: Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //DRAWER
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        setupDrawer()

        //RECYCLER VIEW
        recyclerView = findViewById(R.id.recyclerView)
        dbHelper = TravelMemoryDbHelper(this)

        updateRecyclerView()
        updateMemoriesCount()

        findViewById<Button>(R.id.btnAddMemory).setOnClickListener {//add memory button
            // Start AddMemoryActivity to add a new memory
            val addMemoryIntent = Intent(this, AddMemoryActivity::class.java)
            startActivity(addMemoryIntent)
        }
        findViewById<Button>(R.id.toggleButtonFavorite).setOnClickListener {//favorite button
            showFavoritesOnly = !showFavoritesOnly
            updateRecyclerView()
        }

    }

    private fun setupDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawer(GravityCompat.START)//close drawer and return home
                    true
                }
                R.id.nav_contact_us -> {
                    val emailIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("TravelJournalTeam@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Support Ticket")
                    }
                    if (emailIntent.resolveActivity(packageManager) != null) {
                        startActivity(emailIntent)//go to compose screen
                    }
                    else{
                        Log.d("NOEMAIL", "Rip")
                    }
                    true
                }
                R.id.nav_share -> {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out this great TravelJournalApp")
                    }
                    startActivity(Intent.createChooser(shareIntent, "Share via")) // go to share screen
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_about_us -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    val aboutUsIntent = Intent(this, AboutUsActivity::class.java)
                    startActivity(aboutUsIntent)//go to aboutUs activity
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_settings -> {
                    val settingsIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(settingsIntent)//go to settings activity
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }
    override fun onStart() {
        super.onStart()
        updateRecyclerView()
        updateMemoriesCount()
    }

    private fun updateRecyclerView() {
        val travelMemories = if (showFavoritesOnly) {//only take favorite memories if button is pressed
            dbHelper.getFavoriteTravelMemories()
        } else {
            dbHelper.getAllTravelMemories()
        }
        adapter = TravelMemoryAdapter(travelMemories,
            { selectedMemory ->//updates database if you favorite a memory
                dbHelper.updateTravelMemory(selectedMemory)
                updateRecyclerView()//also update the recycler View
            },
            {selectedMemory ->//opens detailsActivity if details is pressed
                val detailsIntent = Intent(this, DetailsActivity::class.java).apply {
                    putExtra("MEMORY_DETAILS", selectedMemory)//selected Memory will be passed
                }
                startActivity(detailsIntent)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }
    private fun updateMemoriesCount() {
        val textViewMemoryCount = findViewById<TextView>(R.id.textViewMemoryCount)

        val countOfMemories = dbHelper.getAllTravelMemories().size

        textViewMemoryCount.text = "Number of Memories: $countOfMemories"
    }
}