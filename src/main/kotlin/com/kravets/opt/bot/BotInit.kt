package com.kravets.opt.bot

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession


@Component
class BotInit {
    @Autowired
    var telegramBot: LongPollingBot? = null

    @EventListener(ContextRefreshedEvent::class)
    fun init() {
        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        try {
            telegramBotsApi.registerBot(telegramBot)
        } catch (ignored: Exception) {
        }
    }
}