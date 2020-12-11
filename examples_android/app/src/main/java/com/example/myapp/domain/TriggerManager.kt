package com.example.myapp.domain

import com.confirmit.mobilesdk.ConfirmitServer
import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.mobilesdk.database.externals.Program

class TriggerManager {
    fun getPrograms(): List<Program> {
        val programs: MutableList<Program> = mutableListOf()
        val servers = ConfirmitServer.getServers()
        for (server in servers) {
            try {
                val program = TriggerSDK.getPrograms(server.serverId)
                programs.addAll(program)
            } catch (_: Exception) {
            }
        }

        return programs.sortedByDescending { it.serverId }
    }

    fun setAllDelegate() {
        for (program in getPrograms()) {
            setDelegate(program.serverId, program.programKey)
        }
    }

    fun setDelegate(serverId: String, programKey: String) {
        TriggerSDK.setCallback(serverId, programKey, TriggerCallback())
    }
}