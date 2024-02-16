package com.vinceglb.spacedrop.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Secret(
    val id: String,

    @SerialName("pwd_hash")
    val passwordHash: String,

    val salt: String,

    @SerialName("public_key")
    val publicKey: String,

    @SerialName("secret_key_encrypted")
    val secretKeyEncrypted: String,

    @SerialName("secret_key_nonce")
    val secretKeyNonce: String,
)

@Serializable
data class SecretCreateRequest(
    @SerialName("pwd_hash")
    val passwordHash: String,

    val salt: String,

    @SerialName("public_key")
    val publicKey: String,

    @SerialName("secret_key_encrypted")
    val secretKeyEncrypted: String,

    @SerialName("secret_key_nonce")
    val secretKeyNonce: String,
)
