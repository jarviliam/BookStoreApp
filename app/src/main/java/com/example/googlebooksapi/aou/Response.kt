package com.example.googlebooksapi.aou

import android.graphics.Color

data class ResponseO(val kind: String, val items: List<ItemObj>, val totalItems: Int)

data class Temp(val color: Int)

data class ItemObj(
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: Vinfo
)

data class Vinfo(
    val title: String,
    val authors: List<String>,
    val description: String,
    val publishedDate: String,
    val industryIdentifiers: List<Identify>,
    val readingModes: ReadingModes,
    val pageCount: Int,
    val printType: String,
    val categories: List<String>,
    val maturityRating: String,
    val allowAnonLogging: Boolean,
    val contentVersion: String,
    val panelizationSummary: PanelObj,
    val imageLinks: ImgLinks,
    val language: String,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String,
    val searchInfo: String
)

data class sInfo(
    val country: String,
    val saleability: String,
    val isEbook: Boolean,
    val buyLink: String,
    val accessInfo: AccessInfo
)

data class AccessInfo(
    val country: String,
    val viewability: String,
    val embeddable: Boolean,
    val publicDomain: Boolean,
    val textToSpeechPermissionval: String,
    val ePub: epub,
    val pdf: pdf,
    val webReaderLink: String,
    val accessViewStatus: String,
    val quoteSharingAllowed: Boolean
)

data class epub(val isAvailable: Boolean, val downloadLink: String)
data class pdf(val isAvailable: Boolean, val downloadLink: String)

data class Identify(val type: String, val identifier: String)
data class ReadingModes(val text: Boolean, val image: Boolean)
data class PanelObj(val containsEpubBubbles: Boolean, val containsImageBubbles: Boolean)
data class ImgLinks(val smallThumbnail: String, val thumbnail: String)