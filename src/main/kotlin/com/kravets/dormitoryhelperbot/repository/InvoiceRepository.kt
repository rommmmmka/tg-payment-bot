package com.kravets.dormitoryhelperbot.repository

import com.kravets.dormitoryhelperbot.data.Invoice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InvoiceRepository : JpaRepository<Invoice, Long> {
}