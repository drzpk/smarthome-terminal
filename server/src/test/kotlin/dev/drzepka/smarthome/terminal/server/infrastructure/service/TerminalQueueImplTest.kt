package dev.drzepka.smarthome.terminal.server.infrastructure.service

import dev.drzepka.smarthome.terminal.common.transport.Side
import dev.drzepka.smarthome.terminal.common.transport.message.Message
import dev.drzepka.smarthome.terminal.common.transport.message.MessageResponse
import dev.drzepka.smarthome.terminal.server.domain.service.TerminalQueue
import dev.drzepka.smarthome.terminal.server.domain.entity.Client
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.BDDAssertions.assertThatExceptionOfType
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TerminalQueueImplTest {

    private val handler = object : TerminalQueue.Handler {
        override fun <Response : MessageResponse> processMessage(client: Client, message: Message<Response>): Response {
            val testMessage = message as TestMessage
            @Suppress("UNCHECKED_CAST")
            return TestMessageResponse(testMessage) as Response
        }
    }

    @Test
    fun `should process message from client`() = runBlockingTest {
        val testClient = Client(1, "")
        val message = TestMessage(Side.SERVER)
        val queue = TerminalQueueImpl(handler, coroutineContext)
        val result = async { queue.putMessage(testClient, message) }.asCompletableFuture().join()

        then(result.requestMessageId).isEqualTo(message.id)
    }

    @Test
    fun `should process message from server`() = runBlockingTest {
        val testClient = Client(1, "")
        val firstMessage = TestMessage(Side.CLIENT)
        val secondMessage = TestMessage(Side.CLIENT)
        val queue = TerminalQueueImpl(handler, coroutineContext)

        // Server sends a message for client
        val firstMessageFuture = async { queue.putMessage(testClient, firstMessage) }.asCompletableFuture()
        then(firstMessageFuture.isDone).isFalse()
        wait()

        // Client makes HTTP request to server and downloads messages
        val queuedMessages = queue.getQueuedMessages(testClient)
        then(queuedMessages).containsExactly(firstMessage)

        // Queued messages may be read only once
        then(queue.getQueuedMessages(testClient)).isEmpty()

        // Servers sends another message
        val secondMessageFuture = async { queue.putMessage(testClient, secondMessage) }.asCompletableFuture()
        then(secondMessageFuture.isDone).isFalse()

        // Client responds to the second message
        queue.provideResponses(testClient, listOf(TestMessageResponse(secondMessage)))
        wait()

        // Second message's response should match the original message
        then(secondMessageFuture.isDone).isTrue()
        then(secondMessageFuture.join().requestMessageId).isEqualTo(secondMessage.id)

        // Queue should still contain the first message
        then(queue.getQueuedMessages(testClient, all = true)).containsExactly(firstMessage)

        // Client resonds to the first message
        queue.provideResponses(testClient, listOf(TestMessageResponse(firstMessage)))
        wait()

        // First message's response should match the original message
        then(firstMessageFuture.isDone).isTrue()
        then(firstMessageFuture.join().requestMessageId).isEqualTo(firstMessage.id)
    }

    @Test
    fun `should throw exception after message waiting timeout`() = runBlockingTest {
        val testClient = Client(1, "")
        val message = TestMessage(Side.CLIENT)
        val queue = TerminalQueueImpl(handler, coroutineContext)

        assertThatExceptionOfType(TimeoutCancellationException::class.java).isThrownBy {
            runBlockingTest {
                queue.putMessage(testClient, message)
            }
        }
    }

    private class TestMessage(receiverSide: Side) :
        Message<TestMessageResponse>(receiverSide, TestMessageResponse::class)

    private class TestMessageResponse(requestMessage: TestMessage) : MessageResponse(requestMessage)

    /**
     * Simulate switching between coroutines
     */
    private suspend fun wait() {
        repeat(10) {
            delay(1)
        }
    }
}