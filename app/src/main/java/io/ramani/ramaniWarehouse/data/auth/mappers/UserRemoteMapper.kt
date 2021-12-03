package io.ramani.ramaniWarehouse.data.auth.mappers

import io.ramani.ramaniWarehouse.data.auth.models.UserRemoteModel
import io.ramani.ramaniWarehouse.domain.auth.model.UserModel
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper

class UserRemoteMapper : ModelMapper<UserRemoteModel, UserModel> {
    override fun mapFrom(from: UserRemoteModel): UserModel =
        UserModel.Builder()
            .uuid(from.uuid)
            .accountType(from.accountType)
            .companyId(from.companyId)
            .companyName(from.companyName)
            .currency(from.currency)
            .fcmToken(from.fcmToken)
            .hasSeenSFAOnboarding(from.hasSeenSFAOnboarding)
            .isAdmin(from.isAdmin)
            .phoneNumber(from.phoneNumber)
            .name(from.name)
            .timeZone(from.timeZone)
            .token(from.token)
            .build()

    override fun mapTo(to: UserModel): UserRemoteModel =
        UserRemoteModel(
            to.fcmToken,
            to.token,
            to.accountType,
            to.companyId,
            to.companyName,
            to.name,
            to.phoneNumber,
            to.uuid,
            to.isAdmin,
            to.hasSeenSFAOnboarding,
            to.currency,
            to.timeZone
        )
}