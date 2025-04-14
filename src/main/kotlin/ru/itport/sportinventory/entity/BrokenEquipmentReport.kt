package ru.itport.sportinventory.entity

import io.jmix.core.DeletePolicy
import io.jmix.core.FileRef
import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.entity.annotation.OnDeleteInverse
import io.jmix.core.metamodel.annotation.JmixEntity
import io.jmix.core.metamodel.annotation.InstanceName
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import ru.itport.sportinventory.services.FeedbackService
import java.util.*

@JmixEntity
@Entity
@Table(name = "BROKEN_EQUIPMENT_REPORT")
open class BrokenEquipmentReport {
    @JmixGeneratedValue
    @Id
    @Column(name = "ID", nullable = false)
    var id: UUID? = null

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVENTORY_ID", nullable = false)
    @NotNull
    var inventory: Inventory? = null

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @NotNull
    var user: User? = null

    @Lob
    @Column(name = "DESCRIPTION", nullable = false)
    @NotNull
    var description: String? = null

    @Column(name = "PHOTO")
    var photo: FileRef? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    @NotNull
    var status: ReportStatus? = null

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", nullable = false)
    @NotNull
    var createdDate: Date? = null

}
