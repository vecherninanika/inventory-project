package ru.itport.sportinventory.security

import io.jmix.security.model.EntityAttributePolicyAction
import io.jmix.security.model.EntityPolicyAction
import io.jmix.security.role.annotation.EntityAttributePolicy
import io.jmix.security.role.annotation.EntityPolicy
import io.jmix.security.role.annotation.ResourceRole
import io.jmix.security.role.annotation.SpecificPolicy


@ResourceRole(name = "UserAccessRole", code = UserAccessRole.CODE, scope = ["API"])
interface UserAccessRole {


    companion object {
        const val CODE = "user-access-role"
    }

    @EntityAttributePolicy(entityName = "*", attributes = ["*"], action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityName = "*", actions = [EntityPolicyAction.READ])
    @EntityPolicy(entityName = "BookingJournal", actions = [EntityPolicyAction.READ, EntityPolicyAction.CREATE, EntityPolicyAction.UPDATE, EntityPolicyAction.DELETE])
    @EntityPolicy(entityName = "Inventory", actions = [EntityPolicyAction.READ, EntityPolicyAction.CREATE, EntityPolicyAction.UPDATE, EntityPolicyAction.DELETE])
    @SpecificPolicy(resources = ["rest.enabled", "rest.fileDownload.enabled"])
    fun user()
}