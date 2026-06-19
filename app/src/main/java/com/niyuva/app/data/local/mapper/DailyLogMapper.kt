package com.niyuva.app.data.local.mapper

import com.niyuva.app.data.local.entity.DailyLogEntity
import com.niyuva.app.domain.model.BloodColor
import com.niyuva.app.domain.model.ClotSize
import com.niyuva.app.domain.model.DailyLog
import com.niyuva.app.domain.model.DischargeType
import com.niyuva.app.domain.model.EnergyLevel
import com.niyuva.app.domain.model.FlowLevel
import com.niyuva.app.domain.model.LogSource
import com.niyuva.app.domain.model.PainLevel
import com.niyuva.app.domain.model.SleepQuality
import java.time.LocalDateTime

fun DailyLogEntity.toDomain(): DailyLog {
    return DailyLog(
        id = id?.toLong() ?: 0L,
        date = date,
        flowLevel = FlowLevel.fromString(flowLevel),
        bloodColor = BloodColor.fromString(bloodColor),
        clotSize = ClotSize.fromString(clotSize),
        painLevel = PainLevel.fromString(painLevel),
        painTypes = painTypes ?: emptyList(),
        dischargeType = DischargeType.fromString(dischargeType),
        energyLevel = EnergyLevel.fromString(energyLevel),
        sleepQuality = SleepQuality.fromString(sleepQuality),
        moods = mood ?: emptyList(),
        sexualActivity = null,
        birthControl = birthControl,
        source = LogSource.fromString(source)
    )
}

fun DailyLog.toEntity(createdAt: LocalDateTime = LocalDateTime.now()): DailyLogEntity {
    return DailyLogEntity(
        id = if (id == 0L) null else id.toInt(),
        date = date,
        flowLevel = flowLevel?.name?.lowercase(),
        bloodColor = bloodColor?.name?.lowercase(),
        clotSize = clotSize?.name?.lowercase(),
        painLevel = painLevel?.name?.lowercase(),
        painTypes = if (painTypes.isEmpty()) null else painTypes,
        dischargeType = dischargeType?.name?.lowercase(),
        energyLevel = energyLevel?.name?.lowercase(),
        sleepQuality = sleepQuality?.toDbString(),
        mood = if (moods.isEmpty()) null else moods,
        sexualActivity = null,
        birthControl = birthControl,
        source = source.name.lowercase(),
        createdAt = createdAt
    )
}
