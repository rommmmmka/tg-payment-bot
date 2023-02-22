package com.kravets.dormitoryhelperbot.repository

import com.kravets.dormitoryhelperbot.data.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
}