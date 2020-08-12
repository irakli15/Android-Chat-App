package com.example.httpchatclient.startpage

import com.example.httpchatserver.database.user.User

interface StartPageContract {
    interface View {
        fun showLoad()
        fun hideLoad()
    }

    interface Model {
        fun getUser(
            userName: String,
            onUserLoad1: String,
            onUserLoad: (User) -> Any
        )
    }

    interface Presenter {
        fun loadUser(userName: String, imageString: String, onUserLoad: (User) -> Any)
    }
}