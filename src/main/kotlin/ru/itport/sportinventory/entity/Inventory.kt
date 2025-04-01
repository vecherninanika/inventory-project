package ru.itport.sportinventory.entity

import io.jmix.core.DeletePolicy
import io.jmix.core.FileRef
import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.entity.annotation.OnDeleteInverse
import io.jmix.core.metamodel.annotation.InstanceName
import io.jmix.core.metamodel.annotation.JmixEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.*

@JmixEntity
@Table(name = "INVENTORY", indexes = [
    Index(name = "IDX_INVENTORY_CATEGORY", columnList = "CATEGORY_ID")
])
@Entity
open class Inventory {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    var id: UUID? = null

    @InstanceName
    @Column(name = "NAME", nullable = false, length = 50)
    @NotNull
    var name: String? = null

    @OnDeleteInverse(DeletePolicy.UNLINK)
    @JoinColumn(name = "CATEGORY_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    var category: Category? = null

    @Lob
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "PHOTO")
    var photo: FileRef? = null

    @Column(name = "MAX_RESERVATION_PERIOD")
    var maxReservationPeriod: Int? = null

    @Column(name = "QUANTITY", nullable = false)
    @NotNull
    var quantity: Int? = null

    @Column(name = "QUANTITY_FREE")
    var quantityFree: Int? = null
}