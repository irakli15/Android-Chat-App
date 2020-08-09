package com.example.httpchatclient.startpage

import com.example.httpchatserver.database.user.User

class StartPagePresenterImpl : StartPageContract.Presenter {
    private var model: StartPageContract.Model = StartPageModelImpl()

    override fun loadUser(userName: String, onUserLoad: (user: User) -> Any) {
        model.getUser(userName, onUserLoad)
    }

    val onUserLoad: (User) -> Unit = {

    }
}