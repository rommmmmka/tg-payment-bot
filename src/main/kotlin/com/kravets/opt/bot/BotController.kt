package com.kravets.opt.bot

import com.kravets.opt.data.Invoice
import com.kravets.opt.exception.InvoiceAlreadyPayedException
import com.kravets.opt.exception.InvoiceNotFoundException
import com.kravets.opt.repository.InvoiceRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment
import java.math.BigDecimal
import java.util.*


@Component
class BotController(
    private val botConfig: BotConfig,
    private val invoiceRepository: InvoiceRepository,
) : TelegramLongPollingBot(botConfig.token) {

    override fun getBotUsername(): String {
        return botConfig.name
    }

    override fun onUpdateReceived(update: Update) {
        if (update.hasMessage()) {
            val chatId = update.message.chatId.toString()
            if (update.message.hasText()) {
                val messageText = update.message.text
                if (messageText.length > 7 && messageText.startsWith("/start ")) {
                    startCommandReceived(messageText.substring(7), chatId)
                }
            } else if (update.message.hasSuccessfulPayment()) {
                successfulPaymentReceived(update.message.successfulPayment, chatId)
            }
        } else if (update.hasPreCheckoutQuery()) {
            preCheckoutQueryReceived(update.preCheckoutQuery)
        }
    }

    private fun startCommandReceived(parameter: String, chatId: String) {
        try {
            val invoiceId = parameter.toLong()
            val invoice: Invoice = invoiceRepository.findByIdOrNull(invoiceId) ?: throw InvoiceNotFoundException()
            if (invoice.paymentDate != null) {
                throw InvoiceAlreadyPayedException()
            }

            execute(
                SendMessage.builder()
                    .chatId(chatId)
                    .parseMode("Markdown")
                    .text(
                        """
                        *${invoice.user.fio}*
                        ${invoice.user.login}
                        
                        *Сумма оплаты за ${String.format("%02d", invoice.month)}.${invoice.year}:* ${invoice.sum} руб.
                        
                        *Тестовый режим оплаты*
                        Для оплаты воспользуйтесь тестовой картой:
                        `4242424242424242`
                        Введите любой валидный срок действия и CVC
                        """.trimIndent()
                    )
                    .build()
            )

            val title: String = String.format("Оплата за %02d.%d", invoice.month, invoice.year)

            var description = "${invoice.user.fio} ${invoice.user.login}"
            if (description.length > 255) {
                description = description.substring(253) + "..."
            }

            val cost = invoice.sum.multiply(BigDecimal.valueOf(100)).intValueExact()

            execute(
                SendInvoice.builder()
                    .chatId(chatId)
                    .currency("BYN")
                    .providerToken(botConfig.botPaymentToken)
                    .title(title)
                    .description(description)
                    .payload(parameter)
                    .startParameter(parameter)
                    .price(LabeledPrice("Оплата проживания", cost))
                    .build()
            )
        } catch (e: Exception) {
            println(e.message)
            execute(
                SendMessage.builder()
                    .chatId(chatId)
                    .text(e.message ?: "Ошибка!")
                    .build()
            )
        }
    }

    private fun successfulPaymentReceived(successfulPayment: SuccessfulPayment, chatId: String) {
        try {
            val invoiceId = successfulPayment.invoicePayload.toLong()
            val invoice: Invoice = invoiceRepository.findByIdOrNull(invoiceId)!!
            invoice.paymentDate = Calendar.getInstance().time
            invoiceRepository.save(invoice)
            execute(
                SendMessage.builder()
                    .chatId(chatId)
                    .text(String.format("Проживание за %02d.%d успешно оплачено!", invoice.month, invoice.year))
                    .build()
            )
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun preCheckoutQueryReceived(preCheckoutQuery: PreCheckoutQuery) {
        try {
            execute(
                AnswerPreCheckoutQuery.builder()
                    .preCheckoutQueryId(preCheckoutQuery.id)
                    .ok(true)
                    .build()
            )
        } catch (e: Exception) {
            println(e.message)
        }
    }
}