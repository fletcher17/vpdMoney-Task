package com.example.vpdmoneytask.clicklistener

import com.example.vpdmoneytask.model.UserDetailsResponseItem

interface OnItemClicked {

    fun clickedItem(user : UserDetailsResponseItem)
}