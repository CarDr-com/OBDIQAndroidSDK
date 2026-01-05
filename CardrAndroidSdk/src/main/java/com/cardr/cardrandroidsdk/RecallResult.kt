package com.cardr.obdiqandroidsdk

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



data class RecallResponse(
    var vin: String? = null,
    var year: String? = null,
    var makeName: String? = null,
    var modelName: String? = null,
    var styleName: String? = null,
    var sourceIdentifier: String? = null,
    var storeIdentifier: String? = null,
    var vehicleNotes: String? = null,
    var results: List<RecallResult> = emptyList()
) {
    fun safetyRecalls(): List<RecallResult> =
        results.filter { it.isSafetyRecall() }

    fun stopSaleRecalls(): List<RecallResult> =
        results.filter { it.isStopSale() }
}

data class RecallResult(
    // OLD API
    val Manufacturer: String? = null,
    val NHTSACampaignNumber: String? = null,
    val NHTSAActionNumber: String? = null,
    val ReportReceivedDate: String? = null,
    val Component: String? = null,
    val Summary: String? = null,
    val Consequence: String? = null,
    val Remedy: String? = null,
    val Notes: String? = null,
    val ModelYear: String? = null,
    val Make: String? = null,
    val Model: String? = null,

    // NEW API
    val status: String? = null,
    val noRemedy: Boolean? = null,
    val recallTypeCode: String? = null,
    val nhtsaCampaignNumber: String? = null,
    val mfgCampaignNumber: String? = null,
    val bulletinNumber: String? = null,
    val componentDescription: String? = null,
    val subject: String? = null,
    val emissionsRelated: Boolean? = null,
    val mfgName: String? = null,
    val mfgText: String? = null,
    val defectSummary: String? = null,
    val consequenceSummary: String? = null,
    val correctiveSummary: String? = null,
    val recallNotes: String? = null,
    val fmvss: String? = null,
    val stopSale: String? = null,
    val nhtsaRecallDate: String? = null,
    val vehicleRecallUuid: String? = null,
)
fun RecallResult.recallDate(): Date? {
    val newFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val oldFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    return when {
        nhtsaRecallDate != null -> newFormat.parse(nhtsaRecallDate!!)
        ReportReceivedDate != null -> oldFormat.parse(ReportReceivedDate!!)
        else -> null
    }
}

fun RecallResult.isSafetyRecall(): Boolean =
    recallTypeCode?.uppercase() == "V"

fun RecallResult.isStopSale(): Boolean =
    stopSale?.uppercase() == "YES"