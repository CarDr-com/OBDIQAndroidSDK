package com.cardr.cardrandroidsdk

fun getVariableURL(isProductionReady: Boolean): String{
    return if(isProductionReady){
        BuildConfig.PROD_GET_VARIABLE_URL
    }else{
        BuildConfig.GET_VARIABLE_URL
    }
}


fun getBaseURL(isProductionReady: Boolean): String{
    return if(isProductionReady){
        BuildConfig.PROD_BASE_URL
    }else{
        BuildConfig.BASE_URL
    }
}

