package ru.openunity.hunterhint.data.offer

data class FindOffersParams(
    val groundsId: Int = -1
) {
    fun getParams(): MutableMap<String, String> {
        val params= mutableMapOf<String, String>()
        if (groundsId != -1) {
            params["groundsId"] = groundsId.toString()
        }
        return params
    }
}