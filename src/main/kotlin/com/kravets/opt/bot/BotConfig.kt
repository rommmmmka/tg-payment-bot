package com.kravets.opt.bot

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfig {
    @Value("\${telegram.name}")
    lateinit var name: String

    @Value("\${telegram.token}")
    lateinit var token: String

    @Value("\${telegram.payment-token}")
    lateinit var botPaymentToken: String
}