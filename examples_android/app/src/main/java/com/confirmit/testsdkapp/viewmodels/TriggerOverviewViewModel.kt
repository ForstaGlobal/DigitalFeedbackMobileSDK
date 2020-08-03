package com.confirmit.testsdkapp.viewmodels

import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.testsdkapp.viewmodels.models.ProgramModel
import com.confirmit.testsdkapp.viewmodels.models.TriggerProgram
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class TriggerOverviewViewModel(program: TriggerProgram) {
    var program: ProgramModel
        private set

    init {
        this.program = ProgramModel(program)
    }

    fun downloadCountersAsync() = GlobalScope.async {
        TriggerSDK.downloadCountersAsync(program.program.serverId, program.program.programKey)
    }

    fun notifyEvent(event: String, data: Map<String, String?>) {
        TriggerSDK.notifyEvent(program.program.serverId, program.program.programKey, event, data)
    }

    fun downloadProgramAsync(completion: () -> Unit) = GlobalScope.async {
        TriggerSDK.downloadAsync(program.program.serverId, program.program.programKey).await()
        completion()
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