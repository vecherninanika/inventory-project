package ru.itport.sportinventory.entity

import io.jmix.core.DeletePolicy
import io.jmix.core.FileRef
import io.jmix.core.entity.annotation.JmixGeneratedValue
import io.jmix.core.entity.annotation.OnDeleteInverse
import io.jmix.core.metamodel.annotation.JmixEntity
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.util.*

@JmixEntity
@Table(name = "BOOKING_JOURNAL", indexes = [
    Index(name = "IDX_BOOKING_JOURNAL_USER", columnList = "USER_ID"),
    Index(name = "IDX_BOOKING_JOURNAL_INVENTORY", columnList = "INVENTORY_ID")
])
@Entity
open class BookingJournal {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    var id: UUID? = null

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @JoinColumn(name = "USER_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var user: User? = null

    @JoinColumn(name = "INVENTORY_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var inventory: Inventory? = null

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", nullable = false)
    var startDate: Date? = null

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", nullable = false)
    var endDate: Date? = null

    @Column(name = "PHOTO", length = 1024)
    var photo: FileRef? = null

}