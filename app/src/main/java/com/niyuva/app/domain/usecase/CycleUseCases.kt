package com.niyuva.app.domain.usecase

import com.niyuva.app.domain.model.Cycle
import com.niyuva.app.domain.repository.CycleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCyclesUseCase @Inject constructor(
    private val repository: CycleRepository
) {
    operator fun invoke(): Flow<List<Cycle>> = repository.getAllCycles()
}

class GetLatestCycleUseCase @Inject constructor(
    private val repository: CycleRepository
) {
    suspend operator fun invoke(): Cycle? = repository.getLatestCycle()
}

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

class GetCycleCountUseCase @Inject constructor(
    private val repository: CycleRepository
) {
    suspend operator fun invoke(): Int = repository.getCycleCount()
}

class GetRecentCyclesUseCase @Inject constructor(
    private val repository: CycleRepository
) {
    suspend operator fun invoke(count: Int): List<Cycle> = repository.getRecentCycles(count)
}
