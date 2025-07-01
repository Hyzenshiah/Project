package com.hyzen.multipagecomposeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.hyzen.multipagecomposeapp.ui.theme.MultiPageComposeAppTheme
import androidx.core.net.toUri
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Text


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiPageComposeAppTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppDestinations.HOME_ROUTE
    )
    {
        composable(AppDestinations.HOME_ROUTE) {
            HomeScreen(navController = navController)
        }

        composable(AppDestinations.ANDROID_ROUTE) {
            Android_basics(navController = navController)
        }

        composable(AppDestinations.INTERNAL_ROUTE) {
            Internal_content(navController = navController)
        }
        composable(AppDestinations.EXTERNAL_ROUTE) {
            External_content(navController = navController)
        }
        composable(AppDestinations.YOUTUBE_ROUTE) {
            Youtube(navController = navController)
        }
        composable(AppDestinations.LEARNMORE_ROUTE) {
            Learn_More(navController = navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            background(Color.white)
        ) {
            Text(
                text = "Hello Welcome!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold.fontColor(color=Color.black),
                modifier = Modifier.padding(bottom = 30.dp)
            )
            Button(onClick = { navController.navigate(AppDestinations.ANDROID_ROUTE) }) {
                Text("Android basics ")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(AppDestinations.INTERNAL_ROUTE) }) {
                Text("Internal content")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(AppDestinations.EXTERNAL_ROUTE) }) {
                Text("External content")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(AppDestinations.YOUTUBE_ROUTE) }) {
                Text("Youtube channel view")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate(AppDestinations.LEARNMORE_ROUTE) }) {
                Text("Learn More")
            }
        }
    }

}

@Composable
fun Android_basics(navController: NavController) {
    val firebase = Firebase.database
    val ref = firebase.getReference("basics")
    val context = LocalContext.current

    var dataItems by remember { mutableStateOf<List<Item>>(emptyList()) }

    LaunchedEffect(Unit) {
        val items = mutableListOf<Item>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear() // Clear existing data to avoid duplicates on updates
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(Item::class.java)
                    user?.let {
                        items.add(it)
                    }
                }
                dataItems = items
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize())
            {
                items(dataItems) { item ->
                    Card(onClick = {
                        val url = item.url
                        val webpage = url?.toUri()
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        context.startActivity(intent)
                    }) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "${item.title}",
                                modifier = Modifier.padding(vertical = 10.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Text("${item.description}")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }


            Button(onClick = {
                navController.popBackStack()
                val i = Item(
                    title = "Android Basics with Compose",
                    description = " Android Basics with Compose is a self-paced, online course on how to build Android apps using the latest best practices. It covers the basics of building apps with Jetpack Compose, the recommended toolkit for building user interfaces on Android.",
                    url = "https://developer.android.com/courses/android-basics-compose/course"
                )
                ref.push().setValue(i)
                val i2 = Item(
                    title = "Training courses",
                    description = "Whether a new developer, just new to Android, or an experienced professional, grow your skills with training created by Google's Android development experts. Then get certified as an Android developer to grow your career. ",
                    url = "https://developer.android.com/courses"
                )
                ref.push().setValue(i2)
                val i3 = Item(
                    title = "Learn the Basics of Android",
                    description = "Get started developing Android Apps! Get to know the Android programming environment and skills needed to build basic Android apps",
                    url = "https://www.codecademy.com/learn/learn-the-basics-of-android"
                )
                ref.push().setValue(i3)

                val i4 = Item(
                    title = "Android Basics",
                    description = "\n" +
                            "\n" +
                            "Use this free Android tutorial to get started with your device, manage your privacy and settings, add and delete contacts, and keep it running smoothly.\n",
                    url = "https://edu.gcfglobal.org/en/androidbasics/"
                )
                ref.push().setValue(i3)

                val i5 = Item(
                    title = "Learn the Basics of Android",
                    description = "Get started developing Android Apps! Get to know the Android programming environment and skills needed to build basic Android apps",
                    url = "https://www.codecademy.com/learn/learn-the-basics-of-android"
                )
                ref.push().setValue(i3)

                val i6 = Item(
                    title = "Learn the Basics of Android",
                    description = "Get started developing Android Apps! Get to know the Android programming environment and skills needed to build basic Android apps",
                    url = "https://www.codecademy.com/learn/learn-the-basics-of-android"
                )
                ref.push().setValue(i3)
            }
            ) {
                Text("Back To Home")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(AppDestinations.ANDROID_ROUTE) {
                    popUpTo(AppDestinations.ANDROID_ROUTE)
                }
            }) {
                Text("View Next")
            }
        }
    }
}

@Composable
fun Internal_content(navController: NavController) {
    val firebase = Firebase.database
    val ref = firebase.getReference("basics")
    val context = LocalContext.current

    var dataItems by remember { mutableStateOf<List<Item>>(emptyList()) }

    LaunchedEffect(Unit) {
        val items = mutableListOf<Item>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear() // Clear existing data to avoid duplicates on updates
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(Item::class.java)
                    user?.let {
                        items.add(it)
                    }
                }
                dataItems = items
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize())
            {
                items(dataItems) { item ->
                    Card(onClick = {
                        val url = item.url
                        val webpage = url?.toUri()
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        context.startActivity(intent)
                    }) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "${item.title}",
                                modifier = Modifier.padding(vertical = 10.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Text("${item.description}")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }


            Button(onClick = {
                navController.popBackStack()
                val i = Item(
                    title = "Android Basics with Compose",
                    description = " Android Basics with Compose is a self-paced, online course on how to build Android apps using the latest best practices. It covers the basics of building apps with Jetpack Compose, the recommended toolkit for building user interfaces on Android.",
                    url = "https://developer.android.com/courses/android-basics-compose/course"
                )
                ref.push().setValue(i)
                val i2 = Item(
                    title = "Training courses",
                    description = "Whether a new developer, just new to Android, or an experienced professional, grow your skills with training created by Google's Android development experts. Then get certified as an Android developer to grow your career. ",
                    url = "https://developer.android.com/courses"
                )
                ref.push().setValue(i2)
                val i3 = Item(
                    title = "Learn the Basics of Android",
                    description = "Get started developing Android Apps! Get to know the Android programming environment and skills needed to build basic Android apps",
                    url = "https://www.codecademy.com/learn/learn-the-basics-of-android"
                )
                ref.push().setValue(i3)
            }
            ) {
                Text("Back To Home")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(AppDestinations.ANDROID_ROUTE) {
                    popUpTo(AppDestinations.ANDROID_ROUTE)
                }
            }) {
                Text("View Next")
            }
        }
    }
}

@Composable
fun External_content(navController: NavController) {
    val firebase = Firebase.database
    val ref = firebase.getReference("basics")
    val context = LocalContext.current

    var dataItems by remember { mutableStateOf<List<Item>>(emptyList()) }

    LaunchedEffect(Unit) {
        val items = mutableListOf<Item>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear() // Clear existing data to avoid duplicates on updates
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(Item::class.java)
                    user?.let {
                        items.add(it)
                    }
                }
                dataItems = items
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize())
            {
                items(dataItems) { item ->
                    Card(onClick = {
                        val url = item.url
                        val webpage = url?.toUri()
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        context.startActivity(intent)
                    }) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "${item.title}",
                                modifier = Modifier.padding(vertical = 10.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Text("${item.description}")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }


            Button(onClick = {
                navController.popBackStack()
                val i = Item(
                    title = "Android Basics with Compose",
                    description = " Android Basics with Compose is a self-paced, online course on how to build Android apps using the latest best practices. It covers the basics of building apps with Jetpack Compose, the recommended toolkit for building user interfaces on Android.",
                    url = "https://developer.android.com/courses/android-basics-compose/course"
                )
                ref.push().setValue(i)
                val i2 = Item(
                    title = "Training courses",
                    description = "Whether a new developer, just new to Android, or an experienced professional, grow your skills with training created by Google's Android development experts. Then get certified as an Android developer to grow your career. ",
                    url = "https://developer.android.com/courses"
                )
                ref.push().setValue(i2)
                val i3 = Item(
                    title = "Learn the Basics of Android",
                    description = "Get started developing Android Apps! Get to know the Android programming environment and skills needed to build basic Android apps",
                    url = "https://www.codecademy.com/learn/learn-the-basics-of-android"
                )
                ref.push().setValue(i3)
            }
            ) {
                Text("Back To Home")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(AppDestinations.ANDROID_ROUTE) {
                    popUpTo(AppDestinations.ANDROID_ROUTE)
                }
            }) {
                Text("View Next")
            }
        }
    }
}

@Composable
fun Youtube(navController: NavController){
    val firebase = Firebase.database
    val ref = firebase.getReference("basics")
    val context = LocalContext.current

    var dataItems by remember { mutableStateOf<List<Item>>(emptyList()) }

    LaunchedEffect(Unit) {
        val items = mutableListOf<Item>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear() // Clear existing data to avoid duplicates on updates
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(Item::class.java)
                    user?.let {
                        items.add(it)
                    }
                }
                dataItems = items
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize())
            {
                items(dataItems) { item ->
                    Card(onClick = {
                        val url = item.url
                        val webpage = url?.toUri()
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        context.startActivity(intent)
                    }) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "${item.title}",
                                modifier = Modifier.padding(vertical = 10.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Text("${item.description}")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }


            Button(onClick = {
                navController.popBackStack()
                val i = Item(
                    title = "Android Basics with Compose",
                    description = " Android Basics with Compose is a self-paced, online course on how to build Android apps using the latest best practices. It covers the basics of building apps with Jetpack Compose, the recommended toolkit for building user interfaces on Android.",
                    url = "https://developer.android.com/courses/android-basics-compose/course"
                )
                ref.push().setValue(i)
                val i2 = Item(
                    title = "Training courses",
                    description = "Whether a new developer, just new to Android, or an experienced professional, grow your skills with training created by Google's Android development experts. Then get certified as an Android developer to grow your career. ",
                    url = "https://developer.android.com/courses"
                )
                ref.push().setValue(i2)
                val i3 = Item(
                    title = "Learn the Basics of Android",
                    description = "Get started developing Android Apps! Get to know the Android programming environment and skills needed to build basic Android apps",
                    url = "https://www.codecademy.com/learn/learn-the-basics-of-android"
                )
                ref.push().setValue(i3)
            }
            ) {
                Text("Back To Home")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(AppDestinations.ANDROID_ROUTE) {
                    popUpTo(AppDestinations.ANDROID_ROUTE)
                }
            }) {
                Text("View Next")
            }
        }
    }
}

@Composable
fun Learn_More(navController: NavController) {
    val firebase = Firebase.database
    val ref = firebase.getReference("basics")
    val context = LocalContext.current

    var dataItems by remember { mutableStateOf<List<Item>>(emptyList()) }

    LaunchedEffect(Unit) {
        val items = mutableListOf<Item>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear() // Clear existing data to avoid duplicates on updates
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(Item::class.java)
                    user?.let {
                        items.add(it)
                    }
                }
                dataItems = items
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LazyColumn(modifier = Modifier.fillMaxSize())
            {
                items(dataItems) { item ->
                    Card(onClick = {
                        val url = item.url
                        val webpage = url?.toUri()
                        val intent = Intent(Intent.ACTION_VIEW, webpage)
                        context.startActivity(intent)
                    }) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 12.dp)
                        ) {
                            Text(
                                "${item.title}",
                                modifier = Modifier.padding(vertical = 10.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Text("${item.description}")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }


            Button(onClick = {
                navController.popBackStack()
                val i = Item(
                    title = "Android Basics with Compose",
                    description = " Android Basics with Compose is a self-paced, online course on how to build Android apps using the latest best practices. It covers the basics of building apps with Jetpack Compose, the recommended toolkit for building user interfaces on Android.",
                    url = "https://developer.android.com/courses/android-basics-compose/course"
                )
                ref.push().setValue(i)
                val i2 = Item(
                    title = "Training courses",
                    description = "Whether a new developer, just new to Android, or an experienced professional, grow your skills with training created by Google's Android development experts. Then get certified as an Android developer to grow your career. ",
                    url = "https://developer.android.com/courses"
                )
                ref.push().setValue(i2)
                val i3 = Item(
                    title = "Learn the Basics of Android",
                    description = "Get started developing Android Apps! Get to know the Android programming environment and skills needed to build basic Android apps",
                    url = "https://www.codecademy.com/learn/learn-the-basics-of-android"
                )
                ref.push().setValue(i3)
            }
            ) {
                Text("Back To Home")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(AppDestinations.ANDROID_ROUTE) {
                    popUpTo(AppDestinations.ANDROID_ROUTE)
                }
            }) {
                Text("View Next")
            }
        }
    }
}

data class Item(val title: String? = null, val description: String? = null, val url: String? = null)
