package ua.turskyi.rxjavawithroom.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import ua.turskyi.rxjavawithroom.data.entity.Country.Companion.COLUMN_NAME
import ua.turskyi.rxjavawithroom.data.entity.Country.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME , indices = [Index(value = [COLUMN_NAME], unique = true)])
data class Country (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = COLUMN_ID) var id: Int,
    @ColumnInfo(name = COLUMN_NAME) @SerializedName("name") val name: String,
    @ColumnInfo(name = COLUMN_FLAG) @SerializedName("flag") val flag: String
) {
    companion object {
        const val TABLE_NAME = "Countries"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_FLAG = "flag"
    }
}