package com.example.mybook.model

import com.google.gson.annotations.SerializedName
/**
 * @author Azam Khan
 */
data class Formats(
    @SerializedName("application/pdf") val pdf: String?,
    @SerializedName("application/rdf+xml") val rdfXml: String?,
    @SerializedName("text/html; charset=utf-8") val textHtml: String?,
    @SerializedName("text/plain; charset=utf-8") val textPlain: String?,
    @SerializedName("application/epub+zip") val epubZip: String?,
    @SerializedName("application/x-mobipocket-ebook") val mobipocketEbook: String,
    @SerializedName("image/jpeg") val url: String
)