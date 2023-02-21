package com.kravets.opt.data

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="user")
class User (
    @Id
    var id: Long,
    var login: String,
    var fio: String
)