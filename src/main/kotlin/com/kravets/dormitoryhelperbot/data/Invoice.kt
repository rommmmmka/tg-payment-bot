package com.kravets.dormitoryhelperbot.data


import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name="invoice")
class Invoice(
    @Id
    var id: Long,
    @ManyToOne
    var user: User,
    var month: Int,
    var year: Int,
    var sum: BigDecimal,
    var paymentDate: Date?
)