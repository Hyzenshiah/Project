package com.hyzen.multipagecomposeapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults


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
            AndroidbasicsScreen(navController = navController)
        }

        composable(AppDestinations.INTERNAL_ROUTE) {
            InternalcontentScreen(navController = navController)
        }
        composable(AppDestinations.EXTERNAL_ROUTE) {
            ExternalcontentScreen(navController = navController)
        }
        composable(AppDestinations.YOUTUBE_ROUTE) {
            YoutubeChannelScreen(navController = navController)
        }
        composable(AppDestinations.LEARNMORE_ROUTE) {
            LearnMoreScreen(navController = navController)
        }
        composable(AppDestinations.ADD_CONTENT_ROUTE) {
            AddContentScreen(navController = navController)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = { // Add this topBar block
            TopAppBar(
                title = { Text("Home") },
                // navigationIcon = { /* No back button on home usually */ },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = Color.Transparent, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,


            ) {
            Text(
                text = "Hello Welcome!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
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

            Button(onClick = { navController.navigate(AppDestinations.ADD_CONTENT_ROUTE) }) {
                Text("Add Content")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContentScreen(navController: NavController) {
    val firebase = Firebase.database
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("basics") }
    val categories = listOf("basics", "internal", "external", "youtube")

    Surface(

        // Add Surface for background color
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add Content") }
                )
            },
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... (Rest of your AddContentScreen content: TextFields, Dropdown, Button)
                Text("Add New Content", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )

                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL") },
                    modifier = Modifier.fillMaxWidth()
                )

                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = { /* Read-only */ },
                        label = { Text("Category") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Select Category",
                                Modifier.clickable { expanded = true }
                            )
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (title.isNotBlank() && description.isNotBlank() && url.isNotBlank()) {
                            val ref = firebase.getReference(selectedCategory)
                            val newItem = Item(title = title, description = description, url = url)
                            ref.push().setValue(newItem)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Content added successfully!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    title = ""
                                    description = ""
                                    url = ""
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        context,
                                        "Failed to add content: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Content")
                }
                Button(onClick = { navController.popBackStack() }) { // Back button
                    Text("Back To Home")
                }
            }
        }
    }
}

@Composable
fun AndroidbasicsScreen(navController: NavController) {
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

    Scaffold(modifier = Modifier.fillMaxSize() ) { paddingValues ->
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
                            modifier = Modifier
                                .fillMaxWidth()
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
fun InternalcontentScreen(navController: NavController) {
    val firebase = Firebase.database
    val ref = firebase.getReference("internal")
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
                            modifier = Modifier
                                .fillMaxWidth()
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
fun ExternalcontentScreen(navController: NavController) {
    val firebase = Firebase.database
    val ref = firebase.getReference("external")
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
                            modifier = Modifier
                                .fillMaxWidth()
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
fun YoutubeChannelScreen(navController: NavController) {
    val firebase = Firebase.database
    val ref = firebase.getReference("youtube")
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
                            modifier = Modifier
                                .fillMaxWidth()
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
fun LearnMoreScreen(navController: NavController) {
    val firebase = Firebase.database
    val ref = firebase.getReference("learnmore")
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
                            modifier = Modifier
                                .fillMaxWidth()
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
