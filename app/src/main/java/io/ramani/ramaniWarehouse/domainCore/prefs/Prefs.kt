package io.ramani.ramaniWarehouse.core.domain.prefs

/**
 * Created by Amr on 10/5/17.
 */
interface Prefs {
    var currentUser: String
    var currentInstitutionId: String
    var isCurrentInstituteSelected: Boolean
    var accessToken: String
    val hasAccessToken: Boolean
    var refreshToken: String
    var lastUserEmail: String
    var language: String
    var languageDisplay: String
    var languageID: Int
    var selectedLanguageId: Int
    var notificationToken: String
    var programsLastUpdateDate: String
    var eyoUpToDate: Boolean
    var coelUpToDate: Boolean
    var appVersionName: String

}