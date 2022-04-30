package com.ccn.objectbox.table

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index


@Entity
data class BusinessSampleTable2(
    @Id var id: Long = 0,
    @Index var orderNo: String? = null,
    var code: String? = null,
    var productName: String? = null,
)
