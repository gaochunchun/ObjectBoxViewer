package com.ccn.objectbox

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index


@Entity
data class BusinessSampleTable(
    @Id var id: Long = 0,
    @Index var orderNo: String? = null,  //单号
    @Index var code: String? = null,   //数码
    var productName: String? = null,
    var productName2: String? = null,
    var productName3: String? = null,
    var productName4: String? = null,
    var productName5: String? = null,
    var productName6: String? = null,
    var productName7: String? = null,
)
