package com.kravets.dormitoryhelperbot.data

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name="user")
class User (
    @Id
    var id: Long,
    var login: String,
    var fio: String
)