package com.cardr.cardrandroidsdk

import java.util.UUID
data class SendableMap(
    val value: Map<String, Any>
)

data class DTCResponse(
    var dtcErrorCode: String = "",
    var desc: String = "",
    var status: String = ""
){
    public var name: String = ""
    public var section: String = ""
}

class DTCResponseModel {
    var id: String? = null
    var moduleName: String = ""
    var responseStatus: String? = null
    var identifier: String = ""
    var dtcCodeArray: MutableList<DTCResponse> = mutableListOf()

    // Function to remove duplicate DTCResponses based on dtcErrorCode
   public fun removeDuplicateDTCResponses() {
        val uniqueDTCErrorCodes = mutableSetOf<String>()
        val uniqueDTCResponses = mutableListOf<DTCResponse>()

        for (dtcResponse in dtcCodeArray) {
            if (uniqueDTCErrorCodes.add(dtcResponse.dtcErrorCode)) {
                uniqueDTCResponses.add(dtcResponse)
            }
        }

        dtcCodeArray = uniqueDTCResponses
    }
}

data class VariableData(
    val id: Int?,
    val isDeleted: Int?,
    val recallToken: String?,
    val repairClubToken: String?,
    val autoAppUrl: String?,
    val scan: String?,
    val nhtsaUrl: String?,
    val repairCost: String?,
    val recallApi: String?,
    val repairInfo: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val scanUpdate: String?,
    val server_key : String?,
    val access_token : String?
)


data class VariableResponse(
    val data: VariableData?
)

