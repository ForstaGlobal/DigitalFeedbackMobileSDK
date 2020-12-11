package com.example.myapp.fragments.viewmodels

import androidx.lifecycle.ViewModel
import com.confirmit.mobilesdk.TriggerSDK
import com.confirmit.mobilesdk.database.externals.Program
import com.example.myapp.domain.TriggerManager
import com.example.myapp.fragments.viewmodels.models.ProgramModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TriggerListViewModel : ViewModel() {
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

    suspend fun downloadAllPrograms() = withContext(Dispatchers.IO) {
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
    }

    suspend fun downloadProgram(serverId: String, programKey: String) = withContext(Dispatchers.IO) {
        TriggerSDK.downloadAsync(serverId, programKey).await()
        try {
            processProgram(TriggerSDK.getPrograms(serverId))
        } catch (_: Exception) {
        }
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