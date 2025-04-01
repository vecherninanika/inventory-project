package ru.itport.sportinventory.mapper

import com.nimbusds.jose.util.StandardCharset
import io.jmix.core.FileRef
import org.springframework.beans.factory.annotation.Value
import java.net.URLEncoder

fun FileRef.mapToString(domain: String) : String {
    val ref = "s3://${this.path}?name=${this.fileName}"
    val encodedRef = URLEncoder.encode(ref, StandardCharset.UTF_8.toString())
    return "${domain}/rest/files?fileRef=$encodedRef"
}