package com.confirmit.testsdkapp.viewmodels

import androidx.fragment.app.FragmentActivity
import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.mobilesdk.database.externals.Program
import com.confirmit.mobilesdk.database.externals.Server
import com.confirmit.testsdkapp.AppConfigs
import com.confirmit.testsdkapp.domain.TriggerManager
import com.confirmit.testsdkapp.utils.ServerManager
import com.confirmit.testsdkapp.utils.ServerPref
import com.confirmit.testsdkapp.viewmodels.models.ProgramModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ProgramTriggerResult(var success: Boolean, var status: Boolean)

class TriggerListViewModel(private val context: FragmentActivity) {
    private val triggerManager: TriggerManager = TriggerManager()
    var programs: MutableList<ProgramModel> = mutableListOf()
    var selectedProgram: ProgramModel? = null

    init {
        try {
            for (program in triggerManager.getPrograms()) {
                programs.add(ProgramModel(program))
            }
        } catch (_: Exception) {
        }
    }

    fun reloadPrograms() {
        val exists = mutableListOf<String>()
        val curPrograms = triggerManager.getPrograms()
        for (program in curPrograms) {
            exists.add("${program.serverId}_${program.programKey}")
        }

        programs.removeAll {
            val key = "${it.program.serverId}_${it.program.programKey}"
            !exists.contains(key)
        }

        processProgram(curPrograms)
    }

    fun downloadAllProgramsAsync(completion: () -> Unit) = GlobalScope.async {
        TriggerSDK.testMode = AppConfigs.triggerTestMode
        val updatedSurveys = mutableListOf<Program>()
        for (program in programs) {
            TriggerSDK.downloadAsync(program.program.serverId, program.program.programKey).await()
            try {
                val programs = TriggerSDK.getPrograms(program.program.serverId)
                updatedSurveys.addAll(programs)
            } catch (_: Exception) {
            }
        }
        processProgram(updatedSurveys)
        completion()
    }

    fun downloadProgramAsync(serverId: String, programKey: String, completion: (Boolean) -> Unit) = GlobalScope.async {
        TriggerSDK.testMode = AppConfigs.triggerTestMode
        TriggerSDK.downloadAsync(serverId, programKey).await()
        try {
            processProgram(TriggerSDK.getPrograms(serverId))
            completion(true)
        } catch (_: Exception) {
        }
    }

    fun setNewServer(serverId: String, clientId: String, clientSecret: String): Server {
        val server =  ServerManager.setNewConfiguration(serverId, clientId, clientSecret)
        ServerManager.writeServerToPref(serverId, ServerPref(clientId, clientSecret))
        return server
    }

    private fun processProgram(updatePrograms: List<Program>) {
        for (program in updatePrograms) {
            try {
                val exists = programs.indexOfFirst { it.program.serverId == program.serverId && it.program.programKey == program.programKey }
                if (exists >= 0) {
                    programs[exists].update(program)
                } else {
                    programs.add(ProgramModel(program))
                }
            } catch (_: Exception) {
            }
        }
    }
}