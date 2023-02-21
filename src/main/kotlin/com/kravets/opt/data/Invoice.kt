package com.kravets.opt.data

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.*

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