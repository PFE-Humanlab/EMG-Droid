package com.example.bluetooth.utils


class ActorThread(val success: (Int) -> Unit) : Thread() {

    @Volatile
    private var running: Boolean = false

    @Volatile
    private var values: MutableList<Int> = mutableListOf()

    fun setRunning(isRunning: Boolean) {
        synchronized(running) {
            this.running = isRunning
        }
    }

    override fun run() {

        while (running) {
            while (!values.isEmpty()) {

                var element: Int

                synchronized(values) {
                    element = values.removeAt(0)
                }

                success(element)

            }
            sleep(100)
        }

    }

    fun add(input: Int) {
        synchronized(values) {
            values.add(input)
        }
    }

}