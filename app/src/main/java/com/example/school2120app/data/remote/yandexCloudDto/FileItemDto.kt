package com.example.school2120app.data.remote.yandexCloudDto


import com.google.gson.annotations.SerializedName

data class FileItemDto(
    @SerializedName("antivirus_status")
    val antivirusStatus: String,
    @SerializedName("comment_ids")
    val commentIdsDto: CommentIdsDto,
    val created: String,
    val exifDto: ExifDto,
    val `file`: String,
    val md5: String,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("mime_type")
    val mimeType: String,
    val modified: String,
    val name: String,
    val path: String,
    val preview: String,
    @SerializedName("public_key")
    val publicKey: String,
    @SerializedName("public_url")
    val publicUrl: String,
    @SerializedName("resource_id")
    val resourceId: String,
    val revision: Long,
    val sha256: String,
    val size: Int,
    val type: String
)