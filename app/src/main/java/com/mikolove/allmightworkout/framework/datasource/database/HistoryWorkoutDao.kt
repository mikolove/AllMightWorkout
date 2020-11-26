package com.mikolove.allmightworkout.framework.datasource.database

const val HISTORY_WORKOUT_ORDER_ASC: String = ""
const val HISTORY_WORKOUT_ORDER_DESC: String = "-"
const val HISTORY_WORKOUT_FILTER_NAME = "name"
const val HISTORY_WORKOUT_FILTER_DATE_CREATED = "createdAt"

const val HISTORY_WORKOUT_ORDER_BY_ASC_DATE_UPDATED = HISTORY_WORKOUT_ORDER_ASC + HISTORY_WORKOUT_FILTER_DATE_CREATED
const val HISTORY_WORKOUT_ORDER_BY_DESC_DATE_UPDATED = HISTORY_WORKOUT_ORDER_DESC + HISTORY_WORKOUT_FILTER_DATE_CREATED
const val HISTORY_WORKOUT_ORDER_BY_ASC_NAME = HISTORY_WORKOUT_ORDER_ASC + HISTORY_WORKOUT_FILTER_NAME
const val HISTORY_WORKOUT_ORDER_BY_DESC_NAME = HISTORY_WORKOUT_ORDER_DESC + HISTORY_WORKOUT_FILTER_NAME

const val HISTORY_WORKOUT_PAGINATION_PAGE_SIZE = 30