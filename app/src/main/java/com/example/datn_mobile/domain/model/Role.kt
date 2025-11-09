package com.example.datn_mobile.domain.model

import androidx.datastore.preferences.protobuf.EmptyOrBuilder

enum class Role {
    ADMIN,
    EMPLOYEE_MANAGER,
    SHOP_MANAGER,
    USER,
    UNKNOWN;

    /**
     * safety convert from String to Role
     * return UNKNOWN if not match
     */
    companion object {
        fun fromString(value: String?) : Role {
            return when (value?.uppercase()) {
                "ADMIN" -> ADMIN
                "EMPLOYEE_MANAGER" -> EMPLOYEE_MANAGER
                "SHOP_MANAGER" -> SHOP_MANAGER
                "USER" -> USER
                else -> UNKNOWN
            }
        }
    }
}