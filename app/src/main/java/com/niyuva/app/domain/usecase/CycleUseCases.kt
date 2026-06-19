package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.repository.CycleRepository
import javax.inject.Inject

class SaveCycleUseCase @Inject constructor(
    private val repository: CycleRepository,
    private val detectIrregularCycleUseCase: DetectIrregularCycleUseCase
) {
    suspend operator fun invoke(cycle: Cycle): Long {
        val id = repository.saveCycle(cycle)
        detectIrregularCycleUseCase()
        return id
    }
}

class GetRecentCyclesUseCase @Inject constructor(
    private val repository: CycleRepository
) {
    suspend operator fun invoke(count: Int): List<Cycle> = repository.getRecentCycles(count)
}
