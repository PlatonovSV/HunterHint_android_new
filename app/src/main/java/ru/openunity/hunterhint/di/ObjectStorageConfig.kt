package ru.openunity.hunterhint.di

data class ObjectStorageConfig(
    val bucketName: String = "hunterhint",
    val accessKeyId: String = "4YMiTuyRp1aBTi8cQt33Fr",
    val secretAccessKey: String = "i5ovtnYzVrGkYEsBWLuas23adE9JyRkykwByPqnGrNcJ",
    val endpoint: String = "https://hb.vkcs.cloud"
)

enum class BucketsFolders {
    COMMENT
}