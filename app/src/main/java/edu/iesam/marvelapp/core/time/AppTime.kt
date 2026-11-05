package edu.iesam.marvelapp.core.time

interface AppTime {
    fun currentTimeLong(): Long
}

class LocalAppTime : AppTime {
    override fun currentTimeLong(): Long {
        return System.currentTimeMillis()
    }
}