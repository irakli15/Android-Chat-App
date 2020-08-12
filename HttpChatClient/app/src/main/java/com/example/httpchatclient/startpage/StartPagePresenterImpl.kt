package com.example.httpchatclient.startpage

import com.example.httpchatserver.database.user.User

class StartPagePresenterImpl : StartPageContract.Presenter {
    private val model: StartPageContract.Model = StartPageModelImpl()

    override fun loadUser(userName: String, imageString: String, onUserLoad: (user: User) -> Any) {
        model.getUser(userName, imageString, onUserLoad)
    }
}