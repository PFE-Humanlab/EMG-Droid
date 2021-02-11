package com.example.bluetooth.utils


class ActorThread(val success: (Int) -> Unit) : Thread() {

    @Volatile
    private var running: Boolean = false

    @Volatile
    private var values: MutableList<Int> = mutableListOf()

    override fun run() {

        while (running) {
            while (values.isNotEmpty()) {

                var element: Int

                synchronized(values) {
                    element = values.removeAt(0)
                }

                success(element)

            }
            sleep(100)
        }

    }

    fun setRunning(isRunning: Boolean) {
        synchronized(running) {
            this.running = isRunning
        }
    }

    fun add(input: Int) {
        synchronized(values) {
            values.add(input)
        }
    }

}