package com.mexator.petfinder_client.utils

/**
 * Class that is used to create SQLite query string with where clause.
 */
class WhereBuilder(private var _query: String) {
    public val query: String
        get() = _query

    private var hasConditions = false

    fun addAndCondition(condition: String) {
        addCondition(condition, "and ")
    }

    fun addOrCondition(condition: String) {
        addCondition(condition, "or ")
    }

    private fun addCondition(condition: String, andOr: String) {
        _query += if (hasConditions) {
            andOr
        } else {
            "where "
        }
        hasConditions = true
        _query += condition

        if (!query.endsWith(" ")) {
            _query += " "
        }
    }
}