package ru.spbau.mit.karvozavr.threadpool.kotlin

import ru.spbau.mit.karvozavr.threadpool.LightFuture
import ru.spbau.mit.karvozavr.threadpool.exception.LightExecutionException
import java.security.InvalidParameterException
import java.util.*
import java.lang.*
import java.util.function.Function
import java.util.function.Supplier
import kotlin.reflect.KProperty

class ThreadPoolImpl(threadAmount: Int) {

    private val tasks = LinkedList<LightFutureImpl<*>>()
    private val threads = ArrayList<Thread>()

    private val worker = {
        val lock = java.lang.Object()
        var computation: LightFutureImpl<*>

        out@ while (true) {
            if (Thread.interrupted())
                break

            synchronized(this@ThreadPoolImpl) {
                while (tasks.isEmpty()) {
                    try {
                        Objec
                    } catch (e: InterruptedException) {
                        break@out
                    }

                }

                computation = tasks.peek()
            }

            computation.runForResult()
            computation.notifyAll()
        }
    }

    init {
        if (threadAmount < 1) {
            throw InvalidParameterException("Are you sure, you want to have ThreadPool with " + threadAmount + "threads?")
        }

//        repeat(threadAmount, )
//            threads.add(Thread(worker))
//        }

        for (thread in threads) {
            thread.start()
        }
    }

    fun <T> addTask(computation: Supplier<T>): LightFuture<T> {
        return LightFutureImpl(computation)
    }

    fun shutdown() {
        for (thread in threads)
            thread.interrupt()
    }


    private inner class LightFutureImpl<T>(private val computation: Supplier<T>) : LightFuture<T> {

        private val lock = java.lang.Object()
        @Volatile
        private var ready = false
        @Volatile
        private var result: T? = null
        @Volatile
        private var exception: Exception? = null

        override fun isReady(): Boolean {
            return ready
        }

        override fun get(): T {
            try {
                lock.wait()
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }

            if (exception != null)
                throw LightExecutionException(exception)

            return result!!
        }

        override fun <R : Any?> thenApply(function: Function<in T, out R>) =
                this@ThreadPoolImpl.addTask(Supplier { function.apply(get()) })

        fun runForResult() {
            try {
                result = computation.get()
            } catch (e: Exception) {
                exception = e
            } finally {
                ready = true
            }
        }
    }
}