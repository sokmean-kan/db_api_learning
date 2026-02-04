package com.example.db_api_learning.mapper

import com.example.db_api_learning.dto.request.UnitRequestCreate
import com.example.db_api_learning.dto.response.Status
import com.example.db_api_learning.dto.response.UnitResForm
import com.example.db_api_learning.dto.response.UnitResponse
import com.example.db_api_learning.model.Unit

fun UnitRequestCreate.toUnit(): Unit{
    return Unit(
        name = name
    )
}

fun Unit.toUnitResForm(): UnitResForm {
    return UnitResForm(
        status = Status(
            errorCode = 0,
            errorMessage = "success hz",
        ),
        unit = Unit(
            id = id,
            name = name
        )
    )
}