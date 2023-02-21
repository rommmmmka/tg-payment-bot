package com.kravets.opt.bot

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfig {
    @Value("\${telegram.name}")
    var name: String? = null

    @Value("\${telegram.token}")
    var token: String? = null

    @Value("\${telegram.payment-token}")
    var botPaymentToken: String? = null
}