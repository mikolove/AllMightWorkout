package com.mikolove.core.presentation.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikolove.core.presentation.designsystem.AddIcon
import com.mikolove.core.presentation.designsystem.AmwTheme
import com.mikolove.core.presentation.designsystem.CheckIcon

@Composable
fun AmwChip(
    modifier: Modifier = Modifier,
    title : String,
    selected : Boolean = false,
    onClick : () -> Unit = {}
){
    FilterChip(
        modifier = modifier,
        selected = selected,
        leadingIcon = if(selected){
            {
                Icon(
                    imageVector = CheckIcon,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize))
            }
        }else{ null } ,
        label = {
            Text(text = title)
        },
        onClick = {onClick()}
    )
}

 @Composable
 fun AmwAddChip(
     modifier: Modifier = Modifier,
     title : String,
     selected : Boolean = false,
     onClick : () -> Unit = {}
 ){
     FilterChip(
         modifier = modifier,
         selected = selected,
         leadingIcon = null ,
         trailingIcon = {
             Icon(
                 imageVector = AddIcon,
                 contentDescription = null,
                 modifier = Modifier.size(FilterChipDefaults.IconSize))
         },
         label = {
             Text(text = title)
         },
         onClick = {onClick()}
     )
 }

@Preview
@Composable
fun AmwChipPreview( ){
    AmwTheme {
        AmwChip(
            modifier = Modifier,
            "Test",
            selected = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun AmwChipUnSelectedPreview(){
    AmwTheme {
        AmwChip(
            modifier = Modifier,
            "Test",
            selected = false,
            onClick = {}
        )
    }
}