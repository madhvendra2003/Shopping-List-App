package com.example.shopping

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Shoppingitem(
    val id:Int,
    var name : String,
    var quantity : Int,
    var iseditting: Boolean = false
)



@Composable
fun ShoppingListApp(){
    var sItem by remember {
        mutableStateOf(listOf<Shoppingitem>())
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }






    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { showDialog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "ADD ITEM")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItem){
                  item ->
                  if (item.iseditting) {
                      ShoppingItemEditor(item = item , onEditComplete = {
                          editedname,editedquantity ->
                          sItem = sItem.map{it.copy(iseditting = false)}
                          val editedItem = sItem.find{it.id == item.id}
                          editedItem?.let{
                              it.name = editedname
                              it.quantity = editedquantity
                          }
                      } )


                  }
                else
                  {
                      ShoppingListItem(item = item, onEditClick = {
                          sItem = sItem.map{it.copy(iseditting = it.id==item.id)}

                      },onDeleteClick ={
                          sItem = sItem - item
                      } )
                  }

            }
        }

    }
    if (showDialog)
    {
       AlertDialog(onDismissRequest = {showDialog = false },
           confirmButton = {
                           Row (modifier = Modifier
                               .fillMaxWidth()
                               .padding(8.dp),
                               horizontalArrangement = Arrangement.SpaceBetween){
                              Button(onClick = {
                                  if (itemName.isNotBlank()){
                                      val newItem  = Shoppingitem(
                                          id = sItem.size+1,
                                          name = itemName,
                                          quantity = itemQuantity.toInt()
                                      )
                                      sItem = sItem + newItem
                                      showDialog=  false
                                      itemName = ""
                                      itemQuantity = ""
                                  }
                              }) {
                                  Text(text = "ADD")
                              }
                              Button(onClick = { showDialog = false }) {
                                  Text(text = "CANCEL")
                              }
                           }
           },
           title = { Text(text = "Add Shopping Items")},
           text = {
               Column {
                   OutlinedTextField(value = itemName, onValueChange = {itemName=it},
                       singleLine = true,
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(8.dp)
                       )
                   OutlinedTextField(value = itemQuantity, onValueChange = {itemQuantity=it},
                       singleLine = true,
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(8.dp)
                   )
               }
           }
       )
    }
}

@Composable
fun ShoppingItemEditor(item:Shoppingitem,onEditComplete : (String,Int)->Unit)
{
    var editedName by remember {
        mutableStateOf(item.name)
    }
    var editedquantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.iseditting)
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly )
    {
        Column {
            BasicTextField(value = editedName, onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
            BasicTextField(value = editedquantity, onValueChange = {editedquantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedquantity.toIntOrNull() ?: 1)
        } ) {
             Text(text = "save")
        }
    }
}








@Composable
fun ShoppingListItem(
    item : Shoppingitem,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit
)
{
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)
            )
    ){
        Column {
            Text(text=item.name,modifier = Modifier.padding(8.dp))
            Text(text = item.quantity.toString(),modifier = Modifier.padding(8.dp))
        }

        Row (modifier = Modifier.padding(8.dp)){
              IconButton(onClick = onEditClick) {
                  Icon(imageVector = Icons.Default.Edit,contentDescription = null)
              }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete,contentDescription = null)
            }
        }

    }
}
