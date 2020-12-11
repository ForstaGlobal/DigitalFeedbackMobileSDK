package com.example.myapp.fragments.viewmodels

import androidx.lifecycle.ViewModel
import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.mobilesdk.database.externals.Program
import com.example.myapp.fragments.viewmodels.models.ProgramModel

class TriggerOverviewViewModel : ViewModel() {
    lateinit var program: ProgramModel
        private set

    fun setProgramModel(program: Program) {
        this.program = ProgramModel(program)
    }

    suspend fun downloadCounters() {
        TriggerSDK.downloadCountersAsync(program.program.serverId, program.program.programKey).await()
    }

    fun notifyEvent(event: String, data: Map<String, String?>) {
        TriggerSDK.notifyEvent(program.program.serverId, program.program.programKey, event, data)
    }

    suspend fun downloadProgram() {
        TriggerSDK.downloadAsync(program.program.serverId, program.program.programKey).await()
    }

    fun removeProgram(): Boolean {
        return try {
            TriggerSDK.deleteProgram(program.program.serverId, program.program.programKey, true)
            TriggerSDK.removeCallback(program.program.serverId, program.program.programKey)
            true
        } catch (_: Exception) {
            false
        }
    }
}