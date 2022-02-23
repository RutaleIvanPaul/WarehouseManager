package io.ramani.ramaniWarehouse.domainCore.prefs

import io.ramani.ramaniWarehouse.app.assignstock.presentation.products.model.ProductsUIModel

/**
 * Created by Amr on 10/5/17.
 */
interface Prefs {
    var currentUser: String
    var accessToken: String
    val hasAccessToken: Boolean
    var refreshToken: String
    var currentWarehouse:String
    var invalidate_cache_company_products:Boolean
    var invalidate_cache_available_products: Boolean
    var invalidate_cache_assignments_reports: Boolean

}